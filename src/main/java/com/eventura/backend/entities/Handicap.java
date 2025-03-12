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
@Table(name = "handicap")
public class Handicap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_handicap")
    private Long idHandicap;

    @Column(name = "nom_handicap")
    private String nomHandicap;

    @OneToMany(mappedBy = "handicap")
    private List<LienHandicapEvenement> evenements;
}