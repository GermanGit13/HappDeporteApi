package com.svalero.happDeporte.service;

import com.svalero.happDeporte.domain.Match;
import com.svalero.happDeporte.exception.MatchNotFoundException;
import com.svalero.happDeporte.exception.TeamNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MatchService {

    /** 2) Capa donde va a estar la lógica, tendremos una interface por cada clase Java del domain
     * con los métodos aquí estará el grueso de la aplicación
     */
    Match addMatch(Match match, long teamId) throws TeamNotFoundException;

    void deteteMatch(long id) throws MatchNotFoundException;
    Match modifyMatch(long idMatches, long idTeams, Match match) throws MatchNotFoundException, TeamNotFoundException;
    List<Match> findAll();
    Match findById(long id) throws MatchNotFoundException;
    List<Match> findByTeamInMatch(long teamInMatch) throws MatchNotFoundException;
    List<Match> findByTeamInMatchAndDateMatch(long teamInMatch, LocalDate dateMatch) throws MatchNotFoundException; //Para poder recibir el objeto Team y buscar por fecha
    List<Match> findByTeamInMatchAndDateMatchAndHourMatch(long teamInMatch, LocalDate dateMatch, LocalTime hourMatch) throws MatchNotFoundException; //Para poder recibir el objeto Team, buscar por fecha y hora
}
