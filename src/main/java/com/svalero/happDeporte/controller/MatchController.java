package com.svalero.happDeporte.controller;


import com.svalero.happDeporte.domain.Match;
import com.svalero.happDeporte.domain.Player;
import com.svalero.happDeporte.domain.Team;
import com.svalero.happDeporte.exception.*;
import com.svalero.happDeporte.service.MatchService;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.svalero.happDeporte.Util.Literal.*;

/** 4) Las clases que expongan la lógica de la Aplicación al exterior
 * parecido a los jsp antiguos, capa visible
 * @RestController: para que spring boot sepa que es la capa que se ve al exterior
 */
@RestController
public class MatchController {

    /**
     * Llamamos al MatchService el cual llama al MatchRepository y a su vez este llama a la BBDD
     */
    @Autowired
    private MatchService matchService;
    private final Logger logger = LoggerFactory.getLogger(MatchController.class);

    /**
     * ResponseEntity<Match>: Devolvemos el objeto y un 201
     * @PostMapping("/matches"): Método para dar de alta en la BBDD players
     * @RequestBody: Los datos van en el cuerpo de la llamada como codificados
     * @Valid Para decir que valide los campos a la hora de añadir un nuevo objeto,  los campos los definidos en el domain de que forma no pueden ser introducidos o dejados en blanco por ejemplo en la BBDD
     */
    @PostMapping("/teams/{teamId}/matches")
    @Validated
    public ResponseEntity<Match> addMatch(@Valid @PathVariable long teamId, @RequestBody Match match) throws TeamNotFoundException {
        logger.debug(LITERAL_BEGIN_ADD + TEAM);
        Match newMatch = matchService.addMatch(match, teamId);
        logger.debug(LITERAL_END_ADD + PLAYER);
        //return ResponseEntity.status(200).body(newPlayer); Opcion a mano le pasamos el código y los datos del Objeto creado
        return new ResponseEntity<>(newMatch, HttpStatus.CREATED); //Tambien podemos usar la opción rápida
    }

    /**
     * ResponseEntity<Void>: Vacio, solo tiene código de estado
     * @DeleteMapping("/matches/{id}"): Método para dar borrar por id
     * @PathVariable: Para indicar que el parámetro que le pasamos por la url
     */
    @DeleteMapping("/matches/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable long id) throws MatchNotFoundException {
        logger.debug(LITERAL_BEGIN_DELETE + MATCH);
        matchService.deteteMatch(id);
        logger.debug(LITERAL_END_DELETE + MATCH);

        return ResponseEntity.noContent().build();
    }

    /**
     * @PutMapping("/matches/{idMatches}/teams/{idTeams}"): Método para modificar
     * @PathVariable: Para indicar que el parámetro que le pasamos
     * @RequestBody Match match para pasarle los datos del objeto a modificar
     */
    @PutMapping("/matches/{idMatches}/teams/{idTeams}")
    public ResponseEntity<Match> modifyMatch(@PathVariable long idMatches, @PathVariable long idTeams, @RequestBody Match match) throws MatchNotFoundException, TeamNotFoundException {
        logger.debug(LITERAL_BEGIN_MODIFY + MATCH);
        Match modifiedMatch = matchService.modifyMatch(idMatches, idTeams, match);
        logger.debug(LITERAL_END_MODIFY + MATCH);

        return ResponseEntity.status(HttpStatus.OK).body(modifiedMatch);
    }

    /**
     * ResponseEntity: Para devolver una respuesta con los datos y el código de estado de forma explícita
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/matches"): URL donde se devolverán los datos
     */
    @GetMapping("/matches")
    public ResponseEntity<Object> getMatches(@RequestParam (name = "teamInMatch", defaultValue = "", required = false) String teamInMatch,
                                             @RequestParam (name = "dateMatch", defaultValue = "", required = false) String dateMatch,
                                             @RequestParam (name = "hourMatch", defaultValue = "", required = false) String  hourMatch) throws MatchNotFoundException {
        logger.debug(LITERAL_BEGIN_GET + MATCH);

        if (teamInMatch.equals("") && dateMatch.equals("") && hourMatch.equals("")) {
            logger.debug(LITERAL_END_GET + MATCH);
            return ResponseEntity.ok(matchService.findAll());
        } else if (dateMatch.equals("") && hourMatch.equals("") ) {
            logger.debug(LITERAL_END_GET + MATCH);
            return ResponseEntity.ok(matchService.findByTeamInMatch(Long.parseLong(teamInMatch)));
        } else if (hourMatch.equals("")) {
            logger.debug(LITERAL_END_GET + MATCH);
            return ResponseEntity.ok(matchService.findByTeamInMatchAndDateMatch(Long.parseLong(teamInMatch), LocalDate.parse(dateMatch)));
        }
        logger.debug(LITERAL_END_GET + MATCH);
        List<Match> matches = matchService.findByTeamInMatchAndDateMatchAndHourMatch(Long.parseLong(teamInMatch), LocalDate.parse(dateMatch), LocalTime.parse(hourMatch));
        return ResponseEntity.ok(matches);
    }

    /**
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/matches/id"): URL donde se devolverán los datos por el código Id
     * @PathVariable: Para indicar que el parámetro que le pasamos en el String es que debe ir en la URL
     * throws MatchNotFoundException: capturamos la exception y se la mandamos al manejador de excepciones creado más abajo @ExceptionHandler
     */
    @GetMapping("/matches/{id}")
    public ResponseEntity<Match> findById(@PathVariable long id) throws MatchNotFoundException {
        logger.debug(LITERAL_BEGIN_GET + MATCH + "Id");
        Match match = matchService.findById(id);
        logger.debug(LITERAL_END_GET + MATCH + "Id");

        return ResponseEntity.ok(match);
    }

    /** Capturamos la excepcion para las validaciones y así devolvemos un 404 Not Found
     * @ExceptionHandler(MatchNotFoundException.class): manejador de excepciones, recoge la que le pasamos por parametro en este caso MatchNotFoundException.class
     * ResponseEntity<?>: Con el interrogante porque no sabe que nos devolver
     * @return
     */
    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleMatchNotFoundException(MatchNotFoundException mnfe) {
        logger.error(mnfe.getMessage(), mnfe); //Mandamos la traza de la exception al log, con su mensaje y su traza
        mnfe.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(404, mnfe.getMessage());
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
        exception.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(500, "Internal Server Error"); //asi no damos pistas de como está programa como si pasaba usando e.getMessage()
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR); // le pasamos el error y el 500 error en el servidor no controlado, no sé que ha pasado jajaja
    }

}
