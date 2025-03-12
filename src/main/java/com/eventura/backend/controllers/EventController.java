package com.eventura.backend.controllers;

import com.eventura.backend.entities.Event;
import com.eventura.backend.services.CommentService;
import com.eventura.backend.services.EventService;
import com.eventura.backend.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final CommentService commentService;
    private final LikeService likeService;
    
    @GetMapping
    public ResponseEntity<Page<Event>> getUpcomingEvents(Pageable pageable) {
        Page<Event> events = eventService.getUpcomingEvents(pageable);
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Event>> searchEvents(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<Event> events = eventService.searchEvents(keyword, pageable);
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/range")
    public ResponseEntity<List<Event>> getEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Event> events = eventService.getEventsByDateRange(start, end);
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getEventStats(@PathVariable Long id) {
        if (!eventService.getEventById(id).isPresent()) {
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
}