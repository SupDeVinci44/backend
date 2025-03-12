package com.eventura.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evenement")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evenement")
    private Long idEvenement;

    @Column(nullable = false)
    private String titre;

    private String resume;

    @Column(length = 2000)
    private String description;

    @Column(name = "physique_en_ligne")
    private String physiqueEnLigne;

    @Column(name = "details_condition")
    private String detailsCondition;

    @Column(name = "lien_image_evenement")
    private String lienImageEvenement;

    @Column(name = "credit_image")
    private String creditImage;

    @Column(name = "resume_horaire")
    private String resumeHoraire;

    private String etat;

    @Column(name = "age_minimum")
    private Integer ageMinimum;

    @Column(name = "age_maximum")
    private Integer ageMaximum;

    @Column(name = "lien_acces_en_ligne")
    private String lienAccesEnLigne;

    @Column(name = "lien_inscription")
    private String lienInscription;

    @Column(name = "email_inscription")
    private String emailInscription;

    @Column(name = "telephone_insciption")
    private String telephoneInscription;

    @ManyToOne
    @JoinColumn(name = "id_lieu")
    private Lieu lieu;

    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LienDateEvenement> dates;

    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LienHandicapEvenement> handicaps;

    // Relations pour les commentaires et likes (fonctionnalités ajoutées)
    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    // Méthodes utilitaires
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setEvenement(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setEvenement(null);
    }

    public void addLike(Like like) {
        likes.add(like);
        like.setEvenement(this);
    }

    public void removeLike(Like like) {
        likes.remove(like);
        like.setEvenement(null);
    }

    public int getLikeCount() {
        return likes.size();
    }
}