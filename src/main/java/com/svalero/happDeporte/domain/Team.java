package com.svalero.happDeporte.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

import static com.svalero.happDeporte.Util.Literal.LITERAL_NOT_BLANK;
import static com.svalero.happDeporte.Util.Literal.LITERAL_NOT_NULL;

/**
 * Anotación Lombok: Genera los Getters, Setters, constructor vacío y constructor con todos los argumentos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private String category;

    @Column
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private String competition;

    @Column
    private double cuota;

    @Column(name ="day_train")
    private String dayTrain;

    @Column(name = "start_train")
    private String startTrain;

    @Column(name = "end_train")
    private String endTrain;

    @Column//(columnDefinition = "TRUE") //Falla en H2
    @NotNull(message = LITERAL_NOT_NULL)
    private boolean active;

    /**
     * Para relacionar los usuarios (entrenadores) con los equipos: Un Entrenador puede entrenar x equipos y un equipo puede ser entrenado x entrenadores
     * @ManyToMany:
     * @JoinTable(name = "train", ----> "Creamos la tabla intermedia por ser una relación N:N"
     *             joinColumns = @JoinColumn(name = "team_id"), ----> "Con una columna compuesta por el id de los equipos" "Clase en la que estamos"
     *             inverseJoinColumns = @JoinColumn(name = "user_id")) ----> "Con una columna compuesta por el id de usuarios" "El otro lado de las clases"
     */
//    @ManyToMany
//    @JoinTable(name = "train",
//            joinColumns = @JoinColumn(name = "team_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private List<User> users;


    /**
     * Siempre en las N:1 (ManyToOne se define la clave ajena en el lado N (Many)
     * Para relacionar un jugador con un usuario (Padre o Tutor)
     * @ManyToOne: Muchos jugadores asociados a un usuario N:1
     * @JoinColumn(name = "user_id") como queremos que se llame la tabla de la relación N:1
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userInTeam;

    /**
     * Para relacionar los equipos con los partidos: Un partido puede tener x equipos
     * @OneToMany(mappedBy = "match"): Indicamos que ya está mapeado en la Clase Match que es donde se genera la columna con id de los equipos
     * @JsonBackReference(value = "team_match"): Es para cortar la recursión infinita, por el lado del equipo para que no se siga mostrando el objeto match completo. Evitar bucle infinito
     */
    @OneToMany(mappedBy = "teamInMatch")
    @JsonBackReference(value = "team_match")
    private List<Match> matches;
}
