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
@Table(name = "lien_handicap_evenement")
public class LienHandicapEvenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_evenement")
    @JsonBackReference
    private Evenement evenement;

    @ManyToOne
    @JoinColumn(name = "id_handicap")
    private Handicap handicap;
}