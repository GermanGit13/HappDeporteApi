package com.svalero.happDeporte.service;

import com.svalero.happDeporte.domain.Player;
import com.svalero.happDeporte.domain.User;
import com.svalero.happDeporte.exception.PlayerNotFoundException;
import com.svalero.happDeporte.exception.UserNotFoundException;
import com.svalero.happDeporte.repository.PlayerRepository;
import com.svalero.happDeporte.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/** 3) Para implementar la interface de cada service
 * @Service: Para que spring boot sepa que es la capa del service y donde está la lógica
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    /**
     * @Autowired: Para autoconectar con la BBDD
     * le pasamos el repository (DAO) y el nombre que asociamos asi la tendra acceso a todos los métodos del repositorio
     */
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private UserRepository userRepository; //Para poder hacerme con el id de un User y asociarlo al player
    @Autowired
    private ModelMapper modelMapper; //Mapear entre listas

    @Override
    public Player addPlayer(Player player, long userId) throws UserNotFoundException {
        Player newPlayer = player; //Creamos un objeto Player
        User user = userRepository.findById(userId) //Para buscar el usuario si existe
                .orElseThrow(UserNotFoundException::new);
        newPlayer.setUserInPlayer(user); //El bus nuevo esta relacionado con la linea x

        return playerRepository.save(newPlayer); //conectamos con la BBDD mediante el repositorio
    }

    @Override
    public void deletePlayer(long id) throws PlayerNotFoundException {
        Player player = playerRepository.findById(id) //recogemos el user en concreto si existe, sino saltara la excepción
                .orElseThrow(PlayerNotFoundException::new);
        playerRepository.delete(player); //Si existe y llega aquí lo borramos
    }

    @Override
    public Player modifyPlayer(long idPlayer, long idUser, Player newPlayer) throws PlayerNotFoundException, UserNotFoundException {
        Player existingPlayer = playerRepository.findById(idPlayer)
                .orElseThrow(PlayerNotFoundException::new);
        User existingUser = userRepository.findById(idUser)
                        .orElseThrow(UserNotFoundException::new);
//        long userId = existingPlayer.getId();
//        User existingUser = userRepository.findById(userId)
//                .orElseThrow(UserNotFoundException::new);

//        modelMapper.map(newPlayer, existingPlayer);
        existingPlayer.setId(idPlayer);
        existingPlayer.setUserInPlayer(existingUser);
        existingPlayer.setName(newPlayer.getName());
        existingPlayer.setSurname(newPlayer.getSurname());
        existingPlayer.setDni(newPlayer.getDni());
        existingPlayer.setBirthDate(newPlayer.getBirthDate());
        existingPlayer.setAllergy(newPlayer.getAllergy());
        existingPlayer.setDorsal(newPlayer.getDorsal());
        existingPlayer.setSex(newPlayer.getSex());
        existingPlayer.setActive(newPlayer.isActive());

        return playerRepository.save(existingPlayer);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public Player findById(long id) throws PlayerNotFoundException {
        return playerRepository.findById(id)
                .orElseThrow(PlayerNotFoundException::new);
    }

    /**
     * Para buscar jugadores por usuario
     */
    @Override
    public List<Player> findByUserInPlayer(long userInPlayer) {
        Optional<User> user = userRepository.findById(userInPlayer);
        return playerRepository.findByUserInPlayer(user);
    }

    /**
     * Para buscar jugadores por usuario y nombre
     */
    @Override
    public Object findByUserInPlayerAndName(long userInPlayer, String name) throws PlayerNotFoundException {
        Optional<User> user = userRepository.findById(userInPlayer);
        return playerRepository.findByUserInPlayerAndName(user, name);
    }

    /**
     * Para buscar jugadores por usuario, nombre y active
     */
    @Override
    public List<Player> findByUserInPlayerAndNameAndActive(long userInPlayer, String name, boolean active) throws PlayerNotFoundException {
        Optional<User> user = userRepository.findById(userInPlayer);
        return playerRepository.findByUserInPlayerAndNameAndActive(user, name, active);
    }

    @Override
    public List<Player> findSexOrder(boolean active) {
        return playerRepository.findSexOrder(active);
    }

    @Override
    public List<Player> findPlayerByUser(long userInPlayer) {
        return playerRepository.findPlayerByUser(userInPlayer);
    }

}
