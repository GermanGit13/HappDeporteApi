package com.svalero.happDeporte.repository;

import com.svalero.happDeporte.domain.Player;
import com.svalero.happDeporte.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** 1) Son los métodos que conectan con la BBDD
 * @Repository para decirle que es un DAO y que extiende de CrudRepository
 * interface: hacemos interface con la anotación específica
 * así solo con escribir el nombre de los métodos, sprinboot sabe que métodos son
 * extends CrudRepository<TipoDato, ClaveId>
 */
@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    /**
     * Query Methods: Métodos de las sentencias SQL con el nombre ya resuelve la consulta
     */

    List<Player> findAll();
    List<Player> findByUserInPlayer(Optional<User> user); //Para poder recibir el objeto User
    List<Player> findByUserInPlayerAndName(Optional<User> user, String name); //Para poder recibir el objeto User y buscar por nombre
    List<Player> findByUserInPlayerAndNameAndActive(Optional<User> user, String name, boolean active); //Para poder recibir el objeto User, buscar por nombre y active

    /**
     * nativeQuery: Buscar Jugadores Activos ordenados por sexo
     */
    @Query(value = "SELECT * FROM  \"players\" WHERE \"players\".\"active\" = :paramActive ORDER BY \"players\".\"sex\"", nativeQuery = true)
    List<Player> findSexOrder(@Param("paramActive") boolean active);
    /**
     * nativeQuery: Buscar Jugadores por Usuario ordenados por name
     */
    @Query(value = "SELECT * FROM  \"players\" WHERE \"players\".\"user_id\" = :paramUserInPlayer ORDER BY \"players\".\"name\"", nativeQuery = true)
    List<Player> findPlayerByUser(@Param("paramUserInPlayer") long userInPlayer);

//    /**
//     * JPQL
//     */
//    @Query("SELECT p FROM Player p WHERE p.active = :paramActive")
//    List<Player> getAllPlayersActive(@Param("paramActive") boolean active); //devuelve una lista por nombre y apellido
//


//    @Query(value = "SELECT * FROM players WHERE players.name LIKE %:search% OR players.surname LIKE %:search%", nativeQuery = true)
//    List<Player> searchPlayer(@Param("search") String search);
//    List<Player> findByUserInPlayerAndActive(User user, boolean active);
//    List<Player> findByUserInPlayer();
//    List<Player> findByDni();
}
