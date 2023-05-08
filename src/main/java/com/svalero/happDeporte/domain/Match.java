package com.svalero.happDeporte.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static com.svalero.happDeporte.Util.Literal.LITERAL_NOT_BLANK;
import static com.svalero.happDeporte.Util.Literal.LITERAL_NOT_NULL;

/**
 * Anotación Lombok: Genera los Getters, Setters, constructor vacío y constructor con todos los argumentos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "team_b")
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private String teamB;

    @Column(name = "marker_a")
//    @NotBlank(message = LITERAL_NOT_BLANK)
//    @NotNull(message = LITERAL_NOT_NULL)
    private int markerA;

    @Column(name = "marker_b")
//    @NotBlank(message = LITERAL_NOT_BLANK)
//    @NotNull(message = LITERAL_NOT_NULL)
    private int markerB;

    @Column
    private String analysis;

    @Column
    private double latitude;
    @Column
    private double longitude;

    @Column(name = "date_match")
//    @NotBlank(message = LITERAL_NOT_BLANK)
//    @NotNull(message = LITERAL_NOT_NULL)
    private String dateMatch;

    @Column(name = "hour_match")
//    @NotBlank(message = LITERAL_NOT_BLANK)
//    @NotNull(message = LITERAL_NOT_NULL)
    private String hourMatch;

    /**
     * Siempre en las N:1 (ManyToOne se define la clave ajena en el lado N (Many)
     * Para relacionar un equipo con un partido
     * @ManyToOne: Muchos partidos asociados a un equipo N:1
     * @JoinColumn(name = "team_id") como queremos que se llame la tabla de la relación N:1
     */
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team teamInMatch;
}
