package com.eventura.backend.services;

import com.eventura.backend.entities.Evenement;
import com.eventura.backend.entities.Like;
import com.eventura.backend.entities.User;
import com.eventura.backend.repositories.EvenementRepository;
import com.eventura.backend.repositories.LikeRepository;
import com.eventura.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;
    private final EvenementRepository evenementRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleLike(Long eventId) {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));

        // Récupérer l'événement
        Evenement evenement = evenementRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Événement non trouvé"));

        // Vérifier si l'utilisateur a déjà aimé cet événement
        boolean hasLiked = likeRepository.existsByUserIdAndEvenementIdEvenement(user.getId(), eventId);

        if (hasLiked) {
            // Supprimer le like
            likeRepository.deleteByUserIdAndEvenementIdEvenement(user.getId(), eventId);
            log.info("Like supprimé par l'utilisateur {} pour l'événement {}", username, eventId);
            return false; // L'utilisateur n'aime plus l'événement
        } else {
            // Ajouter un nouveau like
            Like like = new Like();
            like.setUser(user);
            like.setEvenement(evenement);
            like.setCreatedAt(LocalDateTime.now());
            likeRepository.save(like);
            log.info("Nouveau like ajouté par l'utilisateur {} pour l'événement {}", username, eventId);
            return true; // L'utilisateur aime maintenant l'événement
        }
    }

    @Transactional(readOnly = true)
    public boolean hasUserLiked(Long eventId) {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));

        return likeRepository.existsByUserIdAndEvenementIdEvenement(user.getId(), eventId);
    }

    @Transactional(readOnly = true)
    public long countLikesByEventId(Long eventId) {
        return likeRepository.countByEvenementIdEvenement(eventId);
    }
}