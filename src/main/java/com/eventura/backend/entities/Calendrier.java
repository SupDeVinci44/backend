package com.eventura.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "calendrier")
public class Calendrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_date")
    private Long idDate;

    private Integer jour;

    @Column(name = "jour_semaine")
    private String jourSemaine;

    private Integer mois;

    @Column(name = "mois_lettre")
    private String moisLettre;

    private Integer annee;

    @Column(name = "est_weekend")
    private Boolean estWeekend;

    @OneToMany(mappedBy = "date")
    private List<LienDateEvenement> evenements;
}