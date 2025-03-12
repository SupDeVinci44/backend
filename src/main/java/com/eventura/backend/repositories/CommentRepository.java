package com.eventura.backend.repositories;

import com.eventura.backend.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByEvenementIdEvenementOrderByCreatedAtDesc(Long idEvenement, Pageable pageable);

    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Long countByEvenementIdEvenement(Long idEvenement);
}
