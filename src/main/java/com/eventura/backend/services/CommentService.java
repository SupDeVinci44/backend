package com.eventura.backend.services;

import com.eventura.backend.entities.Comment;
import com.eventura.backend.entities.Event;
import com.eventura.backend.entities.User;
import com.eventura.backend.repositories.CommentRepository;
import com.eventura.backend.repositories.EventRepository;
import com.eventura.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByEventId(Long eventId, Pageable pageable) {
        return commentRepository.findByEventIdOrderByCreatedAtDesc(eventId, pageable);
    }
    
    @Transactional
    public Comment addComment(Long eventId, String content) {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));
        
        // Récupérer l'événement
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Événement non trouvé"));
        
        // Créer et sauvegarder le commentaire
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        //comment.setEvent(event);
        comment.setCreatedAt(LocalDateTime.now());
        
        return commentRepository.save(comment);
    }
    
    @Transactional
    public Comment updateComment(Long commentId, String content) {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Commentaire non trouvé"));
        
        // Vérifier que l'utilisateur est bien le propriétaire du commentaire
        if (!comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier ce commentaire");
        }
        
        comment.setContent(content);
        // La date de mise à jour sera automatiquement définie par @PreUpdate
        
        return commentRepository.save(comment);
    }
    
    @Transactional
    public void deleteComment(Long commentId) {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Commentaire non trouvé"));
        
        // Vérifier que l'utilisateur est le propriétaire du commentaire ou un administrateur
        if (!comment.getUser().getUsername().equals(username) && !isAdmin) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à supprimer ce commentaire");
        }
        
        commentRepository.delete(comment);
    }
    
    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }
    
    @Transactional(readOnly = true)
    public Long countCommentsByEventId(Long eventId) {
        return commentRepository.countByEventId(eventId);
    }
}