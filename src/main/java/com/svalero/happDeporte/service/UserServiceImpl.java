package com.svalero.happDeporte.service;

import com.svalero.happDeporte.domain.Player;
import com.svalero.happDeporte.domain.Team;
import com.svalero.happDeporte.domain.User;
import com.svalero.happDeporte.exception.TeamNotFoundException;
import com.svalero.happDeporte.exception.UserNotFoundException;
import com.svalero.happDeporte.repository.TeamRepository;
import com.svalero.happDeporte.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.RollbackException;
import java.util.List;

/** 3) Para implementar la interface de cada service
 * @Service: Para que spring boot sepa que es la capa del service y donde está la lógica
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * @Autowired: Para autoconectar con la BBDD
     * le pasamos el repository (DAO) y el nombre que asociamos asi la tendra acceso a todos los métodos del repositorio
     */
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ModelMapper modelMapper; //Mapear entre listas

    @Override
    public User addUser(User user) {
        return userRepository.save(user); //conectamos con la BBDD mediante el repositorio
    }


    @Override
    public void deleteUser(long id) throws UserNotFoundException {
        User user = userRepository.findById(id) //recogemos el user en concreto si existe, sino saltara la excepción
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user); //Si llega aquí es que existe y lo borramos
    }

    @Override
    public User modifyUser(long id, User newUser) throws UserNotFoundException, RollbackException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        //TODO REVISAR COMO ACTUALIZAR DATOS CON MODELMAPPER
        newUser.setId(id);
//        if (newUser.setPass(equals(existingUser.setPass()))) {
//            newUser.setPass(newUser.getPass());
//        } else {
//            newUser.setPass(newUser.getPass());
//        }
        modelMapper.map(newUser, existingUser);
//        existingUser.setUsername(newUser.getUsername();
//        existingUser.setRol(newUser.getRol());
//        existingUser.setCoach(newUser.isCoach());
//        existingUser.setName(newUser.getName());
//        existingUser.setSurname(newUser.getSurname());
//        existingUser.setAddress(newUser.getAddress());
//        existingUser.setMail(newUser.getMail());
//        existingUser.setPhone(newUser.getPhone());

        return userRepository.save(existingUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    //TODO revisar la excepción

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public List<User> findByNameAndRol(String name, String rol) {
        return userRepository.findByNameAndRol(name, rol);
    }

    @Override
    public List<User> findByRol(String rol) {
        return userRepository.findByRol(rol);
    }

    @Override
    public List<User> findByNameAndRolAndAndCoach(String name, String rol, boolean coach) {
        return userRepository.findByNameAndRolAndAndCoach(name, rol, coach);
    }
}
