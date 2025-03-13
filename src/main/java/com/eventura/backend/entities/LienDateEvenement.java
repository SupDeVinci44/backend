package com.eventura.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lien_date_evenement")
public class LienDateEvenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_date")
    private Calendrier date;

    @ManyToOne
    @JoinColumn(name = "id_evenement")
    @JsonBackReference
    private Evenement evenement;

    @Column(name = "heure_debut")
    private String heureDebut;

    @Column(name = "heure_fin")
    private String heureFin;
}