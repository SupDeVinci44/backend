package com.eventura.backend.entities;

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
    private Evenement evenement;

    @Column(name = "heure_debut")
    private Integer heureDebut;

    @Column(name = "heure_fin")
    private Integer heureFin;
}