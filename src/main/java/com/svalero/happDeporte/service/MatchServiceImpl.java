package com.svalero.happDeporte.service;

import com.svalero.happDeporte.domain.Match;
import com.svalero.happDeporte.domain.Team;
import com.svalero.happDeporte.exception.MatchNotFoundException;
import com.svalero.happDeporte.exception.TeamNotFoundException;
import com.svalero.happDeporte.repository.MatchRepository;
import com.svalero.happDeporte.repository.TeamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/** 3) Para implementar la interface de cada service
 * @Service: Para que spring boot sepa que es la capa del service y donde está la lógica
 */
@Service
public class MatchServiceImpl implements MatchService{

    /**
     * @Autowired: Para autoconectar con la BBDD
     * le pasamos el repository (DAO) y el nombre que asociamos asi la tendra acceso a todos los métodos del repositorio
     */
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Match addMatch(Match match, long teamId) throws TeamNotFoundException {
        Match newMatch = match; //Creamos un objeto Match
        Team team = teamRepository.findById(teamId) //Para buscar el usuario si existe
                //User user = UserRepository.findById(userId) //Para buscar el usuario que existe en la relacion cuando nos viene por objeto y no por URL
                .orElseThrow(TeamNotFoundException::new);
        newMatch.setTeamInMatch(team); //El bus nuevo esta relacionado con la linea x

        return matchRepository.save(newMatch); //conectamos con la BBDD mediante el repositorio
    }

    @Override
    public void deteteMatch(long id) throws MatchNotFoundException {
        Match match = matchRepository.findById(id)
                .orElseThrow(MatchNotFoundException::new);
        matchRepository.delete(match);
    }

    @Override
    public Match modifyMatch(long idMatches, long idTeams, Match newMatch) throws MatchNotFoundException, TeamNotFoundException {
        Match existingMatch = matchRepository.findById(idMatches)
                .orElseThrow(MatchNotFoundException::new);
        Team existingTeam = teamRepository.findById(idTeams)
                        .orElseThrow(TeamNotFoundException::new);

        modelMapper.map(newMatch, existingMatch);
        existingMatch.setId(idMatches);
        existingMatch.setTeamInMatch(existingTeam);

        return matchRepository.save(existingMatch);
    }

    @Override
    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    @Override
    public Match findById(long id) throws MatchNotFoundException {
        return matchRepository.findById(id)
                .orElseThrow(MatchNotFoundException::new);
    }

    @Override
    public List<Match> findByTeamInMatch(long teamInMatch) throws MatchNotFoundException {
        Optional<Team> team = teamRepository.findById(teamInMatch);
        return matchRepository.findByTeamInMatch(team);
    }

    @Override
    public List<Match> findByTeamInMatchAndDateMatch(long teamInMatch, LocalDate dateMatch) throws MatchNotFoundException {
        Optional<Team> team = teamRepository.findById(teamInMatch);
        return matchRepository.findByTeamInMatchAndDateMatch(team, dateMatch);
    }

    @Override
    public List<Match> findByTeamInMatchAndDateMatchAndHourMatch(long teamInMatch, LocalDate dateMatch, LocalTime hourMatch) throws MatchNotFoundException{
        Optional<Team> team = teamRepository.findById(teamInMatch);
        return matchRepository.findByTeamInMatchAndDateMatchAndHourMatch(team, dateMatch, hourMatch);
    }
}
