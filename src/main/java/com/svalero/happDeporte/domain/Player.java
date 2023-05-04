package com.svalero.happDeporte.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.svalero.happDeporte.Util.Literal.LITERAL_NOT_BLANK;
import static com.svalero.happDeporte.Util.Literal.LITERAL_NOT_NULL;

/**
 * Anotación Lombok: Genera los Getters, Setters, constructor vacío y constructor con todos los argumentos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private String name;

    @Column
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private String surname;

    @Column//(unique = true) //Falla en H2
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private String dni;


    @Column
    @Past
//    @NotBlank(message = LITERAL_NOT_BLANK)
//    @NotNull(message = LITERAL_NOT_NULL)
    private LocalDate birthDate;

    @Column
    private String allergy;

    @Column
    private int dorsal;

    @Column
//    @Pattern(regexp = "M")
    @NotNull(message = LITERAL_NOT_NULL)
    private char sex;

    @Column//(columnDefinition = "true") //Falla en H2
    @NotNull(message = LITERAL_NOT_NULL)
    private boolean active;

    /**
     * Siempre en las N:1 (ManyToOne se define la clave ajena en el lado N (Many)
     * Para relacionar un jugador con un usuario (Padre o Tutor)
     * @ManyToOne: Muchos jugadores asociados a un usuario N:1
     * @JoinColumn(name = "user_id") como queremos que se llame la tabla de la relación N:1
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userInPlayer;

    /**
     * Para relacionar los players con la ropa: Un usuario puede tener x ropa solicitada
     * @OneToMany(mappedBy = "user"): Indicamos que ya está mapeado en la Clase Clothes que es donde se genera la columna con id de los usuarios
     * @JsonBackReference(value = "player_clothes"): Es para cortar la recursión infinita, por el lado del jugador para que no se siga mostrando el objeto clothes completo. Evitar bucle infinito
     */
    @OneToMany(mappedBy = "playerInClothes")
    @JsonBackReference(value = "player_clothes")
    private List<Clothes> clothes;
}
