package com.eventura.backend.repositories;

import com.eventura.backend.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    Optional<Event> findByExternalId(String externalId);
    
    Page<Event> findByStartDateTimeAfterOrderByStartDateTimeAsc(LocalDateTime dateTime, Pageable pageable);
    
    @Query("SELECT e FROM Event e WHERE e.startDateTime BETWEEN ?1 AND ?2 ORDER BY e.startDateTime ASC")
    List<Event> findByDateRange(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Event> searchByTitleOrDescription(String keyword, Pageable pageable);
}