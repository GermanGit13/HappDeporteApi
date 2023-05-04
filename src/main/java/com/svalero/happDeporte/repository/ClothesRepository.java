package com.svalero.happDeporte.repository;

import com.svalero.happDeporte.domain.Clothes;
import com.svalero.happDeporte.domain.Player;
import com.svalero.happDeporte.domain.User;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.repository.CrudRepository;
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
public interface ClothesRepository extends CrudRepository<Clothes, Long> {

    /**
     * Query Methods: Métodos de las sentencias SQL con el nombre ya resuelve la consulta
     */
    List<Clothes> findAll();
    List<Clothes> findByPlayerInClothes(Optional<Player> player); //Para poder recibir el objeto Player
    List<Clothes> findByPlayerInClothesAndSizeEquipment(Optional<Player> player, String sizeEquipment); //Para poder recibir el objeto User y talla
    List<Clothes> findByPlayerInClothesAndSizeEquipmentAndDorsal(Optional<Player> player, String sizeEquipment, int dorsal); //Para poder recibir el objeto Player, buscar por talla y active
}
