package com.svalero.happDeporte.repository;

import com.svalero.happDeporte.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 1) Son los métodos que conectan con la BBDD
 * @Repository para decirle que es un DAO y que extiende de CrudRepository
 * interface: hacemos interface con la anotación específica
 * así solo con escribir el nombre de los métodos, sprinboot sabe que métodos son
 * extends CrudRepository<TipoDato, ClaveId>
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Query Methods: Métodos de las sentencias SQL con el nombre ya resuelve la consulta
     */

    List<User> findAll();
    List<User> findAllByCoach(boolean coach);
    User findByUsername(String username);
    List<User> findByName(String name);
    List<User> findByRol(String rol);
    List<User> findByNameAndRol(String name, String rol);
    List<User> findByNameAndRolAndAndCoach(String Username, String rol, boolean coach);
}
