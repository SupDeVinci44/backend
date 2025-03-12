package com.eventura.backend.controllers;

import com.eventura.backend.services.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/evenement/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long eventId) {
        try {
            boolean liked = likeService.toggleLike(eventId);
            long likeCount = likeService.countLikesByEventId(eventId);

            Map<String, Object> response = new HashMap<>();
            response.put("liked", liked);
            response.put("likeCount", likeCount);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erreur lors du toggle like: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erreur inattendue lors du toggle like: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/evenement/{eventId}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getLikeStatus(@PathVariable Long eventId) {
        try {
            boolean liked = likeService.hasUserLiked(eventId);
            long likeCount = likeService.countLikesByEventId(eventId);

            Map<String, Object> response = new HashMap<>();
            response.put("liked", liked);
            response.put("likeCount", likeCount);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erreur lors de la récupération du statut like: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la récupération du statut like: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}