package com.eventura.backend.repositories;

import com.eventura.backend.entities.LienDateEvenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LienDateEvenementRepository extends JpaRepository<LienDateEvenement, Long> {
    List<LienDateEvenement> findByEvenementIdEvenement(Long idEvenement);
}