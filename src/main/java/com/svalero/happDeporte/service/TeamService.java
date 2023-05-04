package com.svalero.happDeporte.service;

import com.svalero.happDeporte.domain.Team;
import com.svalero.happDeporte.exception.TeamNotFoundException;
import com.svalero.happDeporte.exception.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;

/** 2) Capa donde va a estar la lógica, tendremos una interface por cada clase Java del domain
 * con los métodos aquí estará el grueso de la aplicación
 */
public interface TeamService {

    Team addTeam(Team team, @Valid long userId) throws UserNotFoundException;
    void deleteTeam(long id) throws TeamNotFoundException;
    Team modifyTeam(long idTeam, long idUser, Team newTeam) throws TeamNotFoundException, UserNotFoundException;
    List<Team> findAll();
    Team findById(long id) throws TeamNotFoundException;
    List<Team> findByCategory(String category);
    List<Team> findByCategoryAndCompetition(String category, String competition);
    List<Team> findByCategoryAndCompetitionAndActive(String category, String competition, boolean active);
    List<Team> findTeamAndActiveByUserId(long userInTeam, boolean active);
}
