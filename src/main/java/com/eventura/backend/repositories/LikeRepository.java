package com.eventura.backend.repositories;

import com.eventura.backend.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndEvenementIdEvenement(Long userId, Long idEvenement);

    boolean existsByUserIdAndEvenementIdEvenement(Long userId, Long idEvenement);

    long countByEvenementIdEvenement(Long idEvenement);

    void deleteByUserIdAndEvenementIdEvenement(Long userId, Long idEvenement);
}
