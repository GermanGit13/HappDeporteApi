package com.svalero.happDeporte.controller;

import com.svalero.happDeporte.domain.Clothes;
import com.svalero.happDeporte.exception.*;
import com.svalero.happDeporte.service.ClothesService;
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
public class ClothesController {

    /**
     * Llamamos al ClothesService el cual llama al ClothesRepository y a su vez este llama a la BBDD
     */
    @Autowired
    private ClothesService clothesService;
    private final Logger logger = LoggerFactory.getLogger(ClothesController.class);

    /**
     * ResponseEntity<Clothes>: Devolvemos el objeto y un 201
     * @PostMapping("/clothes"): Método para dar de alta en la BBDD clothes
     * @RequestBody: Los datos van en el cuerpo de la llamada como codificados
     * @Valid Para decir que valide los campos a la hora de añadir un nuevo objeto,  los campos los definidos en el domain de que forma no pueden ser introducidos o dejados en blanco por ejemplo en la BBDD
     */
    @PostMapping("/players/{playerId}/clothes")
    @Validated
    public ResponseEntity<Clothes> addClothes(@Valid @PathVariable long playerId, @RequestBody Clothes clothes) throws PlayerNotFoundException {
        logger.debug(LITERAL_BEGIN_ADD + CLOTHES);
        Clothes newClothes = clothesService.addClothes(clothes, playerId);
        logger.debug(LITERAL_END_ADD + CLOTHES);

        return new ResponseEntity<>(newClothes, HttpStatus.CREATED);
    }
//    @PostMapping("/clothes")
//    @Validated
//    public ResponseEntity<Clothes> addClothes(@Valid @RequestBody Clothes clothes) throws PlayerNotFoundException {
//        logger.debug(LITERAL_BEGIN_ADD + CLOTHES);
//        Clothes newClothes = clothesService.addClothes(clothes);
//        logger.debug(LITERAL_END_ADD + CLOTHES);
//
//        return new ResponseEntity<>(newClothes, HttpStatus.CREATED);
//    }

    /**
     * ResponseEntity<Void>: Vacio, solo tiene código de estado
     * @DeleteMapping("/clothes/{id}"): Método para dar borrar por id
     * @PathVariable: Para indicar que el parámetro que le pasamos por la url
     */
    @DeleteMapping("/clothes/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) throws ClothesNotFoundException {
        logger.debug(LITERAL_BEGIN_DELETE + CLOTHES);
        clothesService.deleteClothes(id);
        logger.debug(LITERAL_END_DELETE + CLOTHES);

        return ResponseEntity.noContent().build();
    }

    /**
     * @PutMapping("/clothes/{id}"): Método para modificar
     * @PathVariable: Para indicar que el parámetro que le pasamos
     * @RequestBody Player player para pasarle los datos del objeto a modificar
     */
    @PutMapping("/clothes/{idClothes}/players/{idPlayers}")
    public ResponseEntity<Clothes> modifyClothes(@PathVariable long idClothes, @PathVariable long idPlayers, @RequestBody Clothes clothes) throws  ClothesNotFoundException, PlayerNotFoundException {
        logger.debug(LITERAL_BEGIN_DELETE + CLOTHES);
        Clothes modifiedClothes = clothesService.modifyClothes(idClothes, idPlayers, clothes);
        logger.debug(LITERAL_END_MODIFY + CLOTHES);
        return ResponseEntity.status(HttpStatus.OK).body(modifiedClothes);
    }

    /**
     * ResponseEntity: Para devolver una respuesta con los datos y el código de estado de forma explícita
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/clothes"): URL donde se devolverán los datos
     */
    @GetMapping("/clothes")
    public ResponseEntity<Object> getClothes(@RequestParam (name = "playerInClothes", defaultValue = "", required = false) String playerInClothes,
                                             @RequestParam (name = "sizeEquipment", defaultValue = "", required = false) String sizeEquipment,
                                             @RequestParam (name = "dorsal", defaultValue = "", required = false) String  dorsal) throws ClothesNotFoundException {
        logger.debug(LITERAL_BEGIN_GET + CLOTHES);

        if (playerInClothes.equals("") && sizeEquipment.equals("") && dorsal.equals("")) {
            logger.debug(LITERAL_END_GET + CLOTHES);
            return ResponseEntity.ok(clothesService.findAll());
        } else if (sizeEquipment.equals("") && dorsal.equals("") ) {
            logger.debug(LITERAL_END_GET + CLOTHES);
            return ResponseEntity.ok(clothesService.findByPlayerInClothes(Long.parseLong(playerInClothes)));
        } else if (dorsal.equals("")) {
            logger.debug(LITERAL_END_GET + CLOTHES);
            return ResponseEntity.ok(clothesService.findByPlayerInClothesAndSizeEquipment(Long.parseLong(playerInClothes), sizeEquipment));
        }
        logger.debug(LITERAL_END_GET + CLOTHES);
        List<Clothes> clothes = clothesService.findByPlayerInClothesAndSizeEquipmentAndDorsal(Long.parseLong(playerInClothes), sizeEquipment, Integer.parseInt(dorsal));
        return ResponseEntity.ok(clothes);
    }

    /**
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/clothes/id"): URL donde se devolverán los datos por el código Id
     * @PathVariable: Para indicar que el parámetro que le pasamos en el String es que debe ir en la URL
     * throws ClothesNotFoundException: capturamos la exception y se la mandamos al manejador de excepciones creado más abajo @ExceptionHandler
     */
    @GetMapping("/clothes/{id}")
    public ResponseEntity<Clothes> getClothesId(@PathVariable long id) throws ClothesNotFoundException {
        logger.debug(LITERAL_BEGIN_GET + CLOTHES + "Id");
        Clothes clothes = clothesService.findById(id);
        logger.debug(LITERAL_END_GET + CLOTHES + "Id");

        return ResponseEntity.ok(clothes);
    }

    /**
     * @ExceptionHandler(ClothesNotFoundException.class): manejador de excepciones, recoge la que le pasamos por parametro en este caso ClothesNotFoundException.class
     * ResponseEntity<?>: Con el interrogante porque no sabe que nos devolver
     * @return
     */
    @ExceptionHandler(ClothesNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleClothesNotFoundException(ClothesNotFoundException cnfe) {
        logger.error(cnfe.getMessage(), cnfe); //Mandamos la traza de la exception al log, con su mensaje y su traza
        cnfe.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(404, cnfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // le pasamos el error y el 404 de not found
    }

    /** Capturamos la excepcion para las validaciones y así devolvemos un 400 Bad Request alguien llama a la API de forma incorrecta
     *@ExceptionHandler(MethodArgumentNotValidException.class) Para capturar la excepcion de las validaciones que hacemos al dar de alta un objeto
     * le pasamos un mensaje personalizado de ErrorMessage
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(MethodArgumentNotValidException manve) {
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
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception); //Mandamos la traza de la exception al log, con su mensaje y su traza
        exception.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(500, "Internal Server Error"); //asi no damos pistas de como está programa como si pasaba usando e.getMessage()
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR); // le pasamos el error y el 500 error en el servidor no controlado, no sé que ha pasado jajaja
    }
}
