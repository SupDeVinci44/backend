package com.eventura.backend.controllers;

import com.eventura.backend.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    
    @PostMapping("/event/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long eventId) {
        boolean liked = likeService.toggleLike(eventId);
        long likeCount = likeService.countLikesByEventId(eventId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);
        response.put("likeCount", likeCount);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/event/{eventId}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getLikeStatus(@PathVariable Long eventId) {
        boolean liked = likeService.hasUserLiked(eventId);
        long likeCount = likeService.countLikesByEventId(eventId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);
        response.put("likeCount", likeCount);
        
        return ResponseEntity.ok(response);
    }
}