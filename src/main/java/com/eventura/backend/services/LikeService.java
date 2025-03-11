package com.eventura.backend.services;

import com.eventura.backend.entities.Event;
import com.eventura.backend.entities.Like;
import com.eventura.backend.entities.User;
import com.eventura.backend.repositories.EventRepository;
import com.eventura.backend.repositories.LikeRepository;
import com.eventura.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public boolean toggleLike(Long eventId) {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));
        
        // Récupérer l'événement
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Événement non trouvé"));
        
        // Vérifier si l'utilisateur a déjà aimé cet événement
        boolean hasLiked = likeRepository.existsByUserIdAndEventId(user.getId(), eventId);
        
        if (hasLiked) {
            // Supprimer le like
            likeRepository.deleteByUserIdAndEventId(user.getId(), eventId);
            return false; // L'utilisateur n'aime plus l'événement
        } else {
            // Ajouter un nouveau like
            Like like = new Like();
            like.setUser(user);
            like.setEvent(event);
            like.setCreatedAt(LocalDateTime.now());
            likeRepository.save(like);
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
        
        return likeRepository.existsByUserIdAndEventId(user.getId(), eventId);
    }
    
    @Transactional(readOnly = true)
    public long countLikesByEventId(Long eventId) {
        return likeRepository.countByEventId(eventId);
    }
}