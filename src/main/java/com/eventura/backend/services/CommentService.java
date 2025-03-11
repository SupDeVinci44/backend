package com.eventura.backend.services;

import com.eventura.backend.entities.Comment;
import com.eventura.backend.entities.Evenement;
import com.eventura.backend.entities.User;
import com.eventura.backend.exceptions.InappropriateContentException;
import com.eventura.backend.repositories.CommentRepository;
import com.eventura.backend.repositories.EvenementRepository;
import com.eventura.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final EvenementRepository evenementRepository;
    private final UserRepository userRepository;
    private final CommentModerationService moderationService;
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByEventId(Long eventId, Pageable pageable) {
        return commentRepository.findByEvenementIdEvenementOrderByCreatedAtDesc(eventId, pageable);
    }

    @Transactional
    public Comment addComment(Long eventId, String content) {
        // Vérifier si le contenu est approprié
        if (!moderationService.isContentAppropriate(content)) {
            throw new InappropriateContentException("Le commentaire contient du contenu inapproprié");
        }

        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));

        // Récupérer l'événement
        Evenement evenement = evenementRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Événement non trouvé"));

        // Créer et sauvegarder le commentaire
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setEvenement(evenement);
        comment.setCreatedAt(LocalDateTime.now());

        log.info("Nouveau commentaire ajouté par l'utilisateur {} pour l'événement {}", username, eventId);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long commentId, String content) {
        // Vérifier si le contenu est approprié
        if (!moderationService.isContentAppropriate(content)) {
            throw new InappropriateContentException("Le commentaire contient du contenu inapproprié");
        }

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

        log.info("Commentaire {} mis à jour par l'utilisateur {}", commentId, username);
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
        return commentRepository.countByEvenementIdEvenement(eventId);
    }
}