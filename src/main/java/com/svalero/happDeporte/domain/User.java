package com.svalero.happDeporte.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.svalero.happDeporte.Util.Literal.*;

/**
 * Anotación Lombok: Genera los Getters, Setters, constructor vacío y constructor con todos los argumentos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column//(unique = true) //Falla en H2
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    @Size(min = 3, max = 9)
    private String username;

    @Column
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    @Size(min = 6, max = 16)
    private String pass;

    @Column//(columnDefinition = ROL_DEFAULT) //Falla en H2
//    @NotBlank(message = LITERAL_NOT_BLANK)
//    @NotNull(message = LITERAL_NOT_NULL)
    private String rol;

    //TODO revisar como ponerlo por defecto en false -> el admin lo cambiará a true
    @Column//(columnDefinition = BOOLEAN_DEFAULT) //Falla en H2
//    @NotNull(message = LITERAL_NOT_NULL)
    private boolean coach;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String address;

    @Column
    @Email
    private String mail;

    @Column
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    @Size(min = 9)
    private String phone;

    /**
     * Para relacionar los usuarios con los jugadores: Un usuario puede tener x jugadores a creados a su cargo
     * @OneToMany(mappedBy = "user"): Indicamos que ya está mapeado en la Clase Player que es donde se genera la columna con id de los usuarios
     * @JsonBackReference(value = "user_players"): Es para cortar la recursión infinita, por el lado del usuario para que no se siga mostrando el objeto player completo. Evitar bucle infinito
     */
    @OneToMany(mappedBy = "userInPlayer")
    @JsonBackReference(value = "user_players")
    private List<Player> players;

    /**
     * Para relacionar los usuarios con los equipos: Un usuario puede entrenar x equipos
     * @OneToMany(mappedBy = "user"): Indicamos que ya está mapeado en la Clase Team que es donde se genera la columna con id de los usuarios
     * @JsonBackReference(value = "user_teams"): Es para cortar la recursión infinita, por el lado del usuario para que no se siga mostrando el objeto team completo. Evitar bucle infinito
     */
    @OneToMany(mappedBy = "userInTeam")
    @JsonBackReference(value = "user_teams")
    private List<Team>  teams;

//    /**
//     * Para relacionar los usuarios con la ropa: Un usuario puede tener x ropa solicitada
//     * @OneToMany(mappedBy = "user"): Indicamos que ya está mapeado en la Clase Clothes que es donde se genera la columna con id de los usuarios
//     * @JsonBackReference(value = "user_clothes"): Es para cortar la recursión infinita, por el lado del usuario para que no se siga mostrando el objeto player completo. Evitar bucle infinito
//     */
//    @OneToMany(mappedBy = "userInClothes")
//    @JsonBackReference(value = "user_clothes")
//    private List<Clothes> clothes;
//
////    public boolean isCoach() {
////        return coach;
////    }
}
