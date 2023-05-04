package com.svalero.happDeporte.controller;

import com.svalero.happDeporte.domain.Player;
import com.svalero.happDeporte.exception.ErrorMessage;
import com.svalero.happDeporte.exception.PlayerNotFoundException;
import com.svalero.happDeporte.exception.UserNotFoundException;
import com.svalero.happDeporte.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.svalero.happDeporte.Util.Literal.*;

/** 4) Las clases que expongan la lógica de la Aplicación al exterior
 * parecido a los jsp antiguos, capa visible
 * @RestController: para que spring boot sepa que es la capa que se ve al exterior
 */
@RestController
public class PlayerController {

    /**
     * Llamamos al PlayerService el cual llama al PlayerRepository y a su vez este llama a la BBDD
     */
    @Autowired
    private PlayerService playerService;
    private final Logger logger = LoggerFactory.getLogger(PlayerController.class); //Creamos el objeto capaz de pintar las trazas en el log y lo asociamos a la clase que queremos controlar

    /**
     * ResponseEntity<Player>: Devolvemos el objeto y un 201
     * @PostMapping("/users/{userId}/players"): Método para dar de alta en la BBDD players
     * @RequestBody: Los datos van en el cuerpo de la llamada como codificados
     * @Valid Para decir que valide los campos a la hora de añadir un nuevo objeto,  los campos los definidos en el domain de que forma no pueden ser introducidos o dejados en blanco por ejemplo en la BBDD
     */
    @PostMapping("/users/{userId}/players")
    @Validated
    public ResponseEntity<Player> addPlayer(@Valid @PathVariable long userId, @RequestBody Player player) throws UserNotFoundException {
        logger.debug(LITERAL_BEGIN_ADD + PLAYER); //Indicamos que el método ha sido llamado y lo registramos en el log
        Player newPlayer = playerService.addPlayer(player, userId);
        logger.debug(LITERAL_END_ADD + PLAYER); //Indicamos que el método ha sido llamado y lo registramos en el log
        //return ResponseEntity.status(200).body(newPlayer); Opcion a mano le pasamos el código y los datos del Objeto creado
        return new ResponseEntity<>(newPlayer, HttpStatus.CREATED); //Tambien podemos usar la opción rápida
    }

    /**
     * ResponseEntity<Void>: Vacio, solo tiene código de estado
     * @DeleteMapping("/players/{id}"): Método para dar borrar por id
     * @PathVariable: Para indicar que el parámetro que le pasamos por la url
     */
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable long id) throws PlayerNotFoundException {
        logger.debug(LITERAL_BEGIN_DELETE + PLAYER);
        playerService.deletePlayer(id);
        logger.debug(LITERAL_END_DELETE + PLAYER);

        return ResponseEntity.noContent().build();
    }

    /**
     * @PutMapping("/players/{idPlayer}/users/{idUser}"): Método para modificar
     * @PathVariable: Para indicar que el parámetro que le pasamos
     * @RequestBody Player player para pasarle los datos del objeto a modificar
     */
    @PutMapping("/players/{idPlayer}/users/{idUser}")
    public ResponseEntity<Player> modifyPlayer(@PathVariable long idPlayer, @PathVariable long idUser, @RequestBody Player player) throws PlayerNotFoundException, UserNotFoundException {
        logger.debug(LITERAL_BEGIN_MODIFY + PLAYER);
        Player modifiedPlayer = playerService.modifyPlayer(idPlayer, idUser, player);
        logger.debug(LITERAL_END_MODIFY + PLAYER);

        return ResponseEntity.status(HttpStatus.OK).body(modifiedPlayer);
    }

//    /**
//     * ResponseEntity: Para devolver una respuesta con los datos y el código de estado de forma explícita
//     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
//     * @GetMapping("/players"): URL donde se devolverán los datos
//     */
////    @GetMapping("/players")
////    public ResponseEntity<List<Player>> getPlayers() {
////        logger.debug(LITERAL_BEGIN_GET + PLAYER);
////        List<Player> players = playerService.findAll();
////        logger.debug(LITERAL_END_GET + PLAYER);
////
////        return ResponseEntity.ok(players);
////    }

    /**
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/players/id"): URL donde se devolverán los datos por el código Id
     * @PathVariable: Para indicar que el parámetro que le pasamos en el String es que debe ir en la URL
     * throws PlayerNotFoundException: capturamos la exception y se la mandamos al manejador de excepciones creado más abajo @ExceptionHandler
     */
    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayerId(@PathVariable long id) throws PlayerNotFoundException {
        logger.debug(LITERAL_END_GET + PLAYER + "Id");
        Player player = playerService.findById(id);
        logger.debug(LITERAL_END_GET + PLAYER + "Id");

        return ResponseEntity.ok(player);
    }

    /**
     * Buscar todos los jugadores
     * Buscar por tres campos
     * @GetMapping("/players/"): URL donde se devolverán los datos por el código Id
     * @RequestParam: Son las QueryParam se usa para poder hacer filtrados en las busquedas "Where"
     */
    @GetMapping("/players")
    public ResponseEntity<Object> getPlayers(@RequestParam (name = "userInPlayer", defaultValue = "", required = false) String userInPlayer,
                                                     @RequestParam (name = "name", defaultValue = "", required = false) String name,
                                                     @RequestParam (name = "active", defaultValue = "", required = false) String  active) throws PlayerNotFoundException {
        logger.debug(LITERAL_BEGIN_GET + PLAYER);
        boolean activeNew = Boolean.parseBoolean(active);

        if (userInPlayer.equals("") && name.equals("") && active.equals("")) {
            logger.debug(LITERAL_END_GET + PLAYER);
            return ResponseEntity.ok(playerService.findAll());
        } else if (name.equals("") && active.equals("") ) {
            logger.debug(LITERAL_END_GET + PLAYER);
            return ResponseEntity.ok(playerService.findByUserInPlayer(Long.parseLong(userInPlayer)));
        } else if (active.equals("")) {
            logger.debug(LITERAL_END_GET + PLAYER);
            return ResponseEntity.ok(playerService.findByUserInPlayerAndName(Long.parseLong(userInPlayer), name));
        }
        logger.debug(LITERAL_END_GET + PLAYER);
        List<Player> players = playerService.findByUserInPlayerAndNameAndActive(Long.parseLong(userInPlayer), name, activeNew);
        return ResponseEntity.ok(players);
    }

    /**
     * Buscar por NativeQuery
     * @GetMapping("/players/"): URL donde se devolverán los datos por el código Id
     * @RequestParam: Son las QueryParam se usa para poder hacer filtrados en las busquedas "Where"
     */
    @GetMapping("/player")
    public ResponseEntity<List<Player>> getPlayerSexOrder(@RequestParam (name = "active", defaultValue = "", required = false) String active,
                                                          @RequestParam(name = "userInPlayer", defaultValue = "", required = false) String userInPlayer) throws PlayerNotFoundException {
        logger.debug((LITERAL_BEGIN_GET + PLAYER));
        boolean activeNew = Boolean.parseBoolean(active);

        if (userInPlayer.equals("")) {
            List<Player> players = playerService.findSexOrder(activeNew);
            logger.debug(LITERAL_END_GET + PLAYER );
            return ResponseEntity.ok(players);
        } else if (active.equals("")) {
            List<Player> players = playerService.findPlayerByUser(Long.parseLong(userInPlayer));
            logger.debug(LITERAL_END_GET + PLAYER );
            return ResponseEntity.ok(players);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

        /** Capturamos la excepcion para las validaciones y así devolvemos un 404 Not Found
         * @ExceptionHandler(PlayerNotFoundException.class): manejador de excepciones, recoge la que le pasamos por parametro en este caso PlayerNotFoundException.class
         * ResponseEntity<?>: Con el interrogante porque no sabe que nos devolver
         * @return
         */
        @ExceptionHandler(PlayerNotFoundException.class)
        public ResponseEntity<ErrorMessage> handlePlayerNotFoundException (PlayerNotFoundException pnfe){
            logger.error(pnfe.getMessage(), pnfe); //Mandamos la traza de la exception al log, con su mensaje y su traza
            pnfe.printStackTrace(); //Para la trazabilidad de la exception
            ErrorMessage errorMessage = new ErrorMessage(404, pnfe.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // le pasamos el error y el 404 de not found
        }

        /** Capturamos la excepcion para las validaciones y así devolvemos un 400 Bad Request alguien llama a la API de forma incorrecta
         *@ExceptionHandler(MethodArgumentNotValidException.class) Para capturar la excepcion de las validaciones que hacemos al dar de alta un objeto
         * le pasamos un mesnaje personalizado de ErrorMessage
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorMessage> handleBadRequestException (MethodArgumentNotValidException manve){
            logger.error(manve.getMessage(), manve); //Mandamos la traza de la exception al log, con su mensaje y su traza
            manve.printStackTrace(); //Para la trazabilidad de la exception
            /**
             * Código que extrae que campos no han pasado la validación
             */
            Map<String, String> errors = new HashMap<>(); //Montamos un Map de errores
            manve.getBindingResult().getAllErrors().forEach(error -> { //para la exception manve recorremos todos los campos
                String fieldName = ((FieldError) error).getField(); //Extraemos con getField el nombre del campo que no ha pasado la validación
                String message = error.getDefaultMessage(); // y el mensaje asociado
                errors.put(fieldName, message);
            });
            /**
             * FIN Código que extrae que campos no han pasado la validación
             */

            ErrorMessage errorMessage = new ErrorMessage(400, "Bad Request", errors); //Podemos pasarle código y mensaje o añadir los códigos de error del Map sacamos los campos que han fallado
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST); // le pasamos el error y el 400 de not found
        }

        /**
         * Lo usamos para contralar las excepciones en general para pillar los errors 500
         * @param exception
         * @return
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorMessage> handleException (Exception exception){
            logger.error(exception.getMessage(), exception); //Mandamos la traza de la exception al log, con su mensaje y su traza
            exception.printStackTrace(); //Para la trazabilidad de la exception
            ErrorMessage errorMessage = new ErrorMessage(500, "Internal Server Error"); //asi no damos pistas de como está programa como si pasaba usando e.getMessage()
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR); // le pasamos el error y el 500 error en el servidor no controlado, no sé que ha pasado jajaja
        }
}
