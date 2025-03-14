package com.eventura.backend.controllers;

import com.eventura.backend.entities.Evenement;
import com.eventura.backend.services.CommentService;
import com.eventura.backend.services.EvenementService;
import com.eventura.backend.services.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/evenements")
@RequiredArgsConstructor
@Slf4j
public class EvenementController {

    private final EvenementService evenementService;
    private final CommentService commentService;
    private final LikeService likeService;

    /**
     * Récupère les événements à venir
     */
    @GetMapping
    public ResponseEntity<Page<Evenement>> getUpcomingEvents(Pageable pageable) {
        Page<Evenement> events = evenementService.getUpcomingEvents(pageable);
        return ResponseEntity.ok(events);
    }

    /**
     * Recherche des événements par mot-clé (titre ou description)
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Evenement>> searchEvents(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<Evenement> events = evenementService.searchEvents(keyword, pageable);
        return ResponseEntity.ok(events);
    }

    /**
     * Récupère les événements par ville
     */
    @GetMapping("/ville/{ville}")
    public ResponseEntity<Page<Evenement>> getEventsByVille(
            @PathVariable String ville,
            Pageable pageable) {
        Page<Evenement> events = evenementService.findByVille(ville, pageable);
        return ResponseEntity.ok(events);
    }

    /**
     * Récupère les événements par type d'accessibilité (handicap)
     */
    @GetMapping("/accessibilite/{accessibilite}")
    public ResponseEntity<Page<Evenement>> getEventsByAccessibilite(
            @PathVariable String accessibilite,
            Pageable pageable) {
        Page<Evenement> events = evenementService.findByAccessibilite(accessibilite, pageable);
        return ResponseEntity.ok(events);
    }

    /**
     * Récupère les détails d'un événement spécifique
     */
    @GetMapping("/{id}")
    public ResponseEntity<Evenement> getEventById(@PathVariable Long id) {
        return evenementService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les statistiques d'un événement (nombre de commentaires, de likes, etc.)
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getEventStats(@PathVariable Long id) {
        if (!evenementService.getEventById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        long commentCount = commentService.countCommentsByEventId(id);
        long likeCount = likeService.countLikesByEventId(id);
        boolean currentUserLiked = likeService.hasUserLiked(id);

        Map<String, Object> stats = new HashMap<>();
        stats.put("commentCount", commentCount);
        stats.put("likeCount", likeCount);
        stats.put("liked", currentUserLiked);

        return ResponseEntity.ok(stats);
    }

    /**
     * Récupère des événements sur une période spécifique
     */
    @GetMapping("/periode")
    public ResponseEntity<Page<Evenement>> getEventsByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            Pageable pageable) {
        Page<Evenement> events = evenementService.findByPeriode(debut, fin, pageable);
        return ResponseEntity.ok(events);
    }
}