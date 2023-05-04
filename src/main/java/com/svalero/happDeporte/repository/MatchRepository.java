package com.svalero.happDeporte.repository;

import com.svalero.happDeporte.domain.Match;
import com.svalero.happDeporte.domain.Team;
import com.svalero.happDeporte.exception.MatchNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/** 1) Son los métodos que conectan con la BBDD
 * @Repository para decirle que es un DAO y que extiende de CrudRepository
 * interface: hacemos interface con la anotación específica
 * así solo con escribir el nombre de los métodos, sprinboot sabe que métodos son
 * extends CrudRepository<TipoDato, ClaveId>
 */
@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

    /**
     * Query Methods: Métodos de las sentencias SQL con el nombre ya resuelve la consulta
     */
    List<Match> findAll();
    List<Match> findByTeamInMatch(Optional<Team> teamInMatch) throws MatchNotFoundException; //Para poder recibir el objeto Team
    List<Match> findByTeamInMatchAndDateMatch(Optional<Team> teamInMatch, LocalDate dateMatch) throws MatchNotFoundException; //Para poder recibir el objeto Team y buscar por fecha
    List<Match> findByTeamInMatchAndDateMatchAndHourMatch(Optional<Team> team, LocalDate dateMatch, LocalTime hourMatch); //Para poder recibir el objeto Team, buscar por fecha y hora

}
