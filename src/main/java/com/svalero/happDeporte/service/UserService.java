package com.svalero.happDeporte.service;

import com.svalero.happDeporte.domain.User;
import com.svalero.happDeporte.exception.UserNotFoundException;

import javax.persistence.RollbackException;
import java.util.List;

/** 2) Capa donde va a estar la lógica, tendremos una interface por cada clase Java del domain
 * con los métodos aquí estará el grueso de la aplicación
 */
public interface UserService {

    User addUser(User user);
    void deleteUser(long id) throws UserNotFoundException;
    User modifyUser(long id, User newUser) throws UserNotFoundException, RollbackException;
    List<User> findAll();
    User findById(long id) throws UserNotFoundException;
    User findByUsername(String username) throws UserNotFoundException;
    List<User> findByName(String Name);
    List<User> findByNameAndRol(String name, String rol);
    List<User> findByRol(String rol);
    List<User> findByNameAndRolAndAndCoach(String Username, String rol, boolean coach);
}
