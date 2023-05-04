package com.svalero.happDeporte.repository;

import com.svalero.happDeporte.domain.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 1) Son los métodos que conectan con la BBDD
 * @Repository para decirle que es un DAO y que extiende de CrudRepository
 * interface: hacemos interface con la anotación específica
 * así solo con escribir el nombre de los métodos, sprinboot sabe que métodos son
 * extends CrudRepository<TipoDato, ClaveId>
 */
@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    /**
     * Query Methods: Métodos de las sentencias SQL con el nombre ya resuelve la consulta
     */
    List<Team> findAll();
    List<Team> findByCategory(String category);
    List<Team> findByCategoryAndCompetition(String category, String competition);
    List<Team> findByCategoryAndCompetitionAndActive(String category, String competition, boolean active);
    /**
     * nativeQuery: Buscar Entrenadores por equipos activos ordenados por categoria
     */
    @Query(value = "SELECT * FROM  \"teams\" WHERE \"teams\".\"user_id\" = :paramUserInTeam AND \"teams\".\"active\" = :paramActive ORDER BY \"teams\".\"category\"", nativeQuery = true)
    List<Team> findTeamAndActiveByUserId(@Param("paramUserInTeam") long userInPlayer, @Param("paramActive") boolean active);
}
