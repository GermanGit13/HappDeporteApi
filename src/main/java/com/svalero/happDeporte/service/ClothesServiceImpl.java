package com.svalero.happDeporte.service;

import com.svalero.happDeporte.domain.Clothes;
import com.svalero.happDeporte.domain.Player;
import com.svalero.happDeporte.exception.ClothesNotFoundException;
import com.svalero.happDeporte.exception.PlayerNotFoundException;
import com.svalero.happDeporte.repository.ClothesRepository;
import com.svalero.happDeporte.repository.PlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.svalero.happDeporte.Util.Constants.PRICE_EQUIPMENT;
import static com.svalero.happDeporte.Util.Constants.PRICE_SWEATSHIRT;

/** 3) Para implementar la interface de cada service
 * @Service: Para que spring boot sepa que es la capa del service y donde está la lógica
 */
@Service
public class ClothesServiceImpl implements ClothesService{

    /**
     * @Autowired: Para autoconectar con la BBDD
     * le pasamos el repository (DAO) y el nombre que asociamos asi la tendra acceso a todos los métodos del repositorio
     */
    @Autowired
    private ClothesRepository clothesRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Clothes addClothes(Clothes clothes, long playerId) throws PlayerNotFoundException {

        clothes.setPriceEquipment(PRICE_EQUIPMENT);
        clothes.setPriceSweatshirt(PRICE_SWEATSHIRT);
        Player player = playerRepository.findById(playerId) //Para buscar el jugador si existe
                .orElseThrow(PlayerNotFoundException::new);
        clothes.setPlayerInClothes(player); //El bus nuevo esta relacionado con la linea x

        return clothesRepository.save(clothes); //conectamos con la BBDD mediante el repositorio
    }

    @Override
    public Clothes addClothes(Clothes clothes) {
        clothes.setPriceEquipment(PRICE_EQUIPMENT);
        clothes.setPriceSweatshirt(PRICE_SWEATSHIRT);

        return clothesRepository.save(clothes);
    }

    @Override
    public void deleteClothes(long id) throws ClothesNotFoundException {
        Clothes clothes = clothesRepository.findById(id)
                .orElseThrow(ClothesNotFoundException::new);
        clothesRepository.delete(clothes);
    }

    @Override
    public Clothes modifyClothes(long idClothes, long idPlayer, Clothes newclothes) throws ClothesNotFoundException, PlayerNotFoundException {
        Clothes modifiedClothes = clothesRepository.findById(idClothes)
                .orElseThrow(ClothesNotFoundException::new);
        Player existPlayer = playerRepository.findById(idPlayer)
                        .orElseThrow(PlayerNotFoundException::new);

        modelMapper.map(newclothes, modifiedClothes);
        modifiedClothes.setId(idClothes);
        modifiedClothes.setPlayerInClothes(existPlayer);
        modifiedClothes.setPriceEquipment(PRICE_EQUIPMENT);
        modifiedClothes.setPriceSweatshirt(PRICE_SWEATSHIRT);
        return clothesRepository.save(modifiedClothes);
    }

    @Override
    public List<Clothes> findAll() {
        return clothesRepository.findAll();
    }

    @Override
    public Clothes findById(long id) throws ClothesNotFoundException {
        return clothesRepository.findById(id)
                .orElseThrow(ClothesNotFoundException::new);
    }

    @Override
    public List<Clothes> findByPlayerInClothes(long playerInClothes) throws ClothesNotFoundException {
        Optional<Player> player = playerRepository.findById(playerInClothes);
        return clothesRepository.findByPlayerInClothes(player);
    }

    @Override
    public Object findByPlayerInClothesAndSizeEquipment(long playerInClothes, String sizeEquipment) throws ClothesNotFoundException {
        Optional<Player> player = playerRepository.findById(playerInClothes);
        return clothesRepository.findByPlayerInClothesAndSizeEquipment(player, sizeEquipment);
    }

    @Override
    public List<Clothes> findByPlayerInClothesAndSizeEquipmentAndDorsal(long playerInClothes, String sizeEquipment, int dorsal) throws ClothesNotFoundException {
        Optional<Player> player = playerRepository.findById(playerInClothes);
        return clothesRepository.findByPlayerInClothesAndSizeEquipmentAndDorsal(player, sizeEquipment, dorsal);
    }
}
