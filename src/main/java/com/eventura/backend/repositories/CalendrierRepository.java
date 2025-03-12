package com.eventura.backend.repositories;

import com.eventura.backend.entities.Calendrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendrierRepository extends JpaRepository<Calendrier, Long> {
    Optional<Calendrier> findByJourAndMoisAndAnnee(int jour, int mois, int annee);
}