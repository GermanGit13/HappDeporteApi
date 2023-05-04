package com.svalero.happDeporte.controller;

import com.svalero.happDeporte.domain.Player;
import com.svalero.happDeporte.domain.Team;
import com.svalero.happDeporte.domain.User;
import com.svalero.happDeporte.exception.ErrorMessage;
import com.svalero.happDeporte.exception.PlayerNotFoundException;
import com.svalero.happDeporte.exception.TeamNotFoundException;
import com.svalero.happDeporte.exception.UserNotFoundException;
import com.svalero.happDeporte.service.TeamService;
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
public class TeamController {

    /**
     * Llamamos al TeamService el cual llama al TeamRepository y a su vez este llama a la BBDD
     */
    @Autowired
    private TeamService teamService;
    private final Logger logger = LoggerFactory.getLogger(TeamController.class);

    /**
     * ResponseEntity<Team>: Devolvemos el objeto y un 201
     * @PostMapping("/users/{userId}/teams"): Método para dar de alta en la BBDD players
     * @RequestBody: Los datos van en el cuerpo de la llamada como codificados
     * @Valid Para decir que valide los campos a la hora de añadir un nuevo objeto,  los campos los definidos en el domain de que forma no pueden ser introducidos o dejados en blanco por ejemplo en la BBDD
     */
    @PostMapping("/users/{userId}/teams")
    @Validated
    public ResponseEntity<Team> addTeam(@Valid @PathVariable long userId, @RequestBody Team team) throws UserNotFoundException {
        logger.debug(LITERAL_BEGIN_ADD + TEAM); //Indicamos que el método ha sido llamado y lo registramos en el log
        Team newTeam = teamService.addTeam(team, userId);
        logger.debug(LITERAL_END_ADD + PLAYER); //Indicamos que el método ha sido llamado y lo registramos en el log
        return new ResponseEntity<>(newTeam, HttpStatus.CREATED); //Tambien podemos usar la opción rápida
    }

    /**
     * ResponseEntity<Void>: Vacio, solo tiene código de estado
     * @DeleteMapping("/teams/{id}"): Método para dar borrar por id
     * @PathVariable: Para indicar que el parámetro que le pasamos por la url
     */
    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(long id) throws TeamNotFoundException {
        logger.debug(LITERAL_BEGIN_DELETE + TEAM);
        teamService.deleteTeam(id);
        logger.debug(LITERAL_END_DELETE + TEAM);

        return ResponseEntity.noContent().build();
    }

    /**
     * @PutMapping("/teams/{idTeam}/users/{idUser}"): Método para modificar pasandole por la URL el idTeam y idUser
     * @PathVariable: Para indicar que el parámetro que le pasamos
     * @RequestBody Team team para pasarle los datos del objeto a modificar
     */
    @PutMapping("/teams/{idTeam}/users/{idUser}")
    public ResponseEntity<Team> modifyTeam(@PathVariable long idTeam, @PathVariable  long idUser, @RequestBody Team team) throws TeamNotFoundException, UserNotFoundException {
        logger.debug(LITERAL_BEGIN_MODIFY + TEAM);
        Team modifTeam = teamService.modifyTeam(idTeam, idUser, team);
        logger.debug(LITERAL_END_MODIFY + TEAM);

        return ResponseEntity.status(HttpStatus.OK).body(modifTeam);
    }

    /**
     * ResponseEntity: Para devolver una respuesta con los datos y el código de estado de forma explícita
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/teams"): URL donde se devolverán los datos
     */
    @GetMapping("/teams")
    public ResponseEntity<Object> getTeams(@RequestParam (name = "category", defaultValue = "", required = false) String category,
                                           @RequestParam (name = "competition", defaultValue = "", required = false) String competition,
                                           @RequestParam (name = "active", defaultValue = "", required = false) String  active) {

        logger.debug(LITERAL_BEGIN_GET + TEAM);
        boolean activeNew = Boolean.parseBoolean(active);

        if (category.equals("") && competition.equals("") && active.equals("")) {
            logger.debug(LITERAL_END_GET + TEAM);
            return ResponseEntity.ok(teamService.findAll());
        } else if (competition.equals("") && active.equals("") ) {
            logger.debug(LITERAL_END_GET + TEAM);
            return ResponseEntity.ok(teamService.findByCategory(category));
        } else if (active.equals("")) {
            logger.debug(LITERAL_END_GET + TEAM);
            return ResponseEntity.ok(teamService.findByCategoryAndCompetition(category, competition));
        }
        logger.debug(LITERAL_END_GET + TEAM);
        List<Team> teams = teamService.findByCategoryAndCompetitionAndActive(category, competition, activeNew);
        return ResponseEntity.ok(teams);
    }

    /**
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/teams/id"): URL donde se devolverán los datos por el código Id
     * @PathVariable: Para indicar que el parámetro que le pasamos en el String es que debe ir en la URL
     * throws TeamNotFoundException: capturamos la exception y se la mandamos al manejador de excepciones creado más abajo @ExceptionHandler
     */
    @GetMapping("/teams/{id}")
    public ResponseEntity<Team> findById(@PathVariable long id) throws TeamNotFoundException {
        logger.debug(LITERAL_BEGIN_GET + TEAM + "Id");
        Team team = teamService.findById(id);
        logger.debug(LITERAL_END_GET + TEAM + "Id");

        return ResponseEntity.ok(team);
    }

    /**
     * ResponseEntity: Para devolver una respuesta con los datos y el código de estado de forma explícita
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/teams"): URL donde se devolverán los datos
     */
    @GetMapping("/team")
    public ResponseEntity<List<Team>> getTeamByUser(@RequestParam (name = "userInTeam", defaultValue = "", required = false) String userInTeam,
                                                    @RequestParam(name = "active", defaultValue = "", required = false) String active) throws TeamNotFoundException {
        logger.debug((LITERAL_BEGIN_GET + TEAM));
        boolean activeNew = Boolean.parseBoolean(active);

        if (!userInTeam.equals("") && !active.equals("")) {
            List<Team> teams = teamService.findTeamAndActiveByUserId(Long.parseLong(userInTeam), activeNew);
            logger.debug(LITERAL_END_GET + TEAM );
            return ResponseEntity.ok(teams);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // le pasamos
    }

    /** Capturamos la excepcion para las validaciones y así devolvemos un 404 Not Found
     * @ExceptionHandler(TeamNotFoundException.class): manejador de excepciones, recoge la que le pasamos por parametro en este caso TeamNotFoundException.class
     * ResponseEntity<?>: Con el interrogante porque no sabe que nos devolver
     * @return
     */
    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleTeamNotFoundException(TeamNotFoundException tnfe) {
        logger.error(tnfe.getMessage(), tnfe); //Mandamos la traza de la exception al log, con su mensaje y su traza
        //unfe.printStackTrace(); //Traza por consola del error
        tnfe.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(404, tnfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // le pasamos el error y el 404 de not found
    }

    /** Capturamos la excepcion para las validaciones y así devolvemos un 400 Bad Request alguien llama a la API de forma incorrecta
     *@ExceptionHandler(MethodArgumentNotValidException.class) Para capturar la excepcion de las validaciones que hacemos al dar de alta un objeto
     * le pasamos un mesnaje personalizado de ErrorMessage
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
        //exception.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(500, "Internal Server Error"); //asi no damos pistas de como está programa como si pasaba usando e.getMessage()
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR); // le pasamos el error y el 500 error en el servidor no controlado, no sé que ha pasado jajaja
    }
}
