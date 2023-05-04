package com.svalero.happDeporte.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

import static com.svalero.happDeporte.Util.Literal.LITERAL_NOT_BLANK;
import static com.svalero.happDeporte.Util.Literal.LITERAL_NOT_NULL;

/**
 * Anotación Lombok: Genera los Getters, Setters, constructor vacío y constructor con todos los argumentos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "clothes")
public class Clothes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotNull(message = LITERAL_NOT_NULL)
    private boolean equipment;

    @Column
    @NotNull(message = LITERAL_NOT_NULL)
    private boolean sweatshirt;

    @Column
    private String sizeEquipment;

    @Column
    private String sizeSweatshirt;

    @Column
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private String serygrafhy;

    @Column
    @NotNull(message = LITERAL_NOT_NULL)
    @Min(1)
    @Max(99)
    private int dorsal;

    @Column
    private double priceEquipment;

    @Column
    private double priceSweatshirt;

    /**
     * Para relacionar un jugador con un pedido de ropa
     * @ManyToOne: Muchos pedidos asociados a un jugador N:1
     * @JoinColumn(name = "player_id") como queremos que se llame la tabla de la relación N:1
     */
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player playerInClothes;


//    /**
//     * Para relacionar un usuario con un pedido de ropa
//     * @ManyToOne: Muchos pedidos asociados a un usuario N:1
//     * @JoinColumn(name = "user_id") como queremos que se llame la tabla de la relación N:1
//     */
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User userInClothes;
}
