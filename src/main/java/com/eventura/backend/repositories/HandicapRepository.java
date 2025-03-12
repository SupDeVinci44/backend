package com.eventura.backend.repositories;

import com.eventura.backend.entities.Handicap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HandicapRepository extends JpaRepository<Handicap, Long> {
    Optional<Handicap> findByNomHandicap(String nomHandicap);
}