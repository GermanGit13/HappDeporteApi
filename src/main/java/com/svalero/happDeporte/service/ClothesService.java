package com.svalero.happDeporte.service;

import com.svalero.happDeporte.domain.Clothes;
import com.svalero.happDeporte.exception.ClothesNotFoundException;
import com.svalero.happDeporte.exception.PlayerNotFoundException;

import java.util.List;

/** 2) Capa donde va a estar la lógica, tendremos una interface por cada clase Java del domain
 * con los métodos aquí estará el grueso de la aplicación
 */
public interface ClothesService {

    Clothes addClothes(Clothes clothes, long playerId) throws PlayerNotFoundException;

    Clothes addClothes(Clothes clothes);
    void deleteClothes(long id) throws ClothesNotFoundException;
    Clothes modifyClothes(long idClothes, long idPlayer, Clothes clothes) throws ClothesNotFoundException, PlayerNotFoundException;
    List<Clothes> findAll();
    Clothes findById(long id) throws ClothesNotFoundException;

    List<Clothes> findByPlayerInClothes(long playerInClothes) throws ClothesNotFoundException;
    Object findByPlayerInClothesAndSizeEquipment(long playerInClothes, String sizeEquipment) throws ClothesNotFoundException;

    List<Clothes> findByPlayerInClothesAndSizeEquipmentAndDorsal(long playerInClothes, String sizeEquipment, int dorsal) throws ClothesNotFoundException;
}
