package com.eventura.backend.repositories;

import com.eventura.backend.entities.LienHandicapEvenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LienHandicapEvenementRepository extends JpaRepository<LienHandicapEvenement, Long> {
    List<LienHandicapEvenement> findByEvenementIdEvenement(Long idEvenement);
}