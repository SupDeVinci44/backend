package com.eventura.backend.repositories;

import com.eventura.backend.entities.Evenement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    @Query("SELECT e, d \n" + //
                "FROM Evenement e\n" + //
                "JOIN e.dates lde\n" + //
                "JOIN lde.date d\n" + //
                "")
    Page<Evenement> findUpcomingEvents(@Param("jour") int jour, @Param("mois") int mois, @Param("annee") int annee, Pageable pageable);

    @Query("SELECT e FROM Evenement e WHERE LOWER(e.titre) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Evenement> searchByTitleOrDescription(String keyword, Pageable pageable);

    @Query("SELECT e FROM Evenement e JOIN e.lieu l WHERE LOWER(l.ville) = LOWER(:ville)")
    Page<Evenement> findByVille(@Param("ville") String ville, Pageable pageable);

    @Query("SELECT e FROM Evenement e JOIN e.handicaps h JOIN h.handicap hc WHERE LOWER(hc.nomHandicap) = LOWER(:accessibilite)")
    Page<Evenement> findByAccessibilite(@Param("accessibilite") String accessibilite, Pageable pageable);

    // Pour filtrer par type (Physique/En_Ligne)
    @Query("SELECT e FROM Evenement e WHERE LOWER(e.physiqueEnLigne) = LOWER(:type)")
    Page<Evenement> findByPhysiqueEnLigne(@Param("type") String type, Pageable pageable);

    // Pour filtrer par tranche d'âge
    @Query("SELECT e FROM Evenement e WHERE (e.ageMinimum IS NULL OR :age >= e.ageMinimum) AND (e.ageMaximum IS NULL OR :age <= e.ageMaximum)")
    Page<Evenement> findByAgeRange(@Param("age") int age, Pageable pageable);
}