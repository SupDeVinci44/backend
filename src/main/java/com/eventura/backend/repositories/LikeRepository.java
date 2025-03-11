package com.eventura.backend.repositories;

import com.eventura.backend.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    Optional<Like> findByUserIdAndEventId(Long userId, Long eventId);
    
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    
    long countByEventId(Long eventId);
    
    void deleteByUserIdAndEventId(Long userId, Long eventId);
} 