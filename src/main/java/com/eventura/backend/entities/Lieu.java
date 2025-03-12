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
@Table(name = "lieu")
public class Lieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lieu")
    private Long idLieu;

    @Column(name = "nom_lieu")
    private String nomLieu;

    private String adresse;

    @Column(name = "code_postal")
    private String codePostal;

    private String ville;

    private String departement;

    private String region;

    private String pays;

    @Column(name = "telephone_lieu")
    private String telephoneLieu;

    @Column(name = "site_web_lieu")
    private String siteWebLieu;

    private String acces;

    @OneToMany(mappedBy = "lieu")
    private List<Evenement> evenements;
}