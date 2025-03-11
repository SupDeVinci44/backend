package com.eventura.backend.controllers;

import com.eventura.backend.entities.Comment;
import com.eventura.backend.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    
    @GetMapping("/event/{eventId}")
    public ResponseEntity<Page<Comment>> getCommentsByEventId(
            @PathVariable Long eventId,
            Pageable pageable) {
        Page<Comment> comments = commentService.getCommentsByEventId(eventId, pageable);
        return ResponseEntity.ok(comments);
    }
    
    @PostMapping("/event/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long eventId,
            @RequestBody Map<String, String> payload) {
        String content = payload.get("content");
        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        
        Comment comment = commentService.addComment(eventId, content);
        return ResponseEntity.ok(comment);
    }
    
    @PutMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long commentId,
            @RequestBody Map<String, String> payload) {
        String content = payload.get("content");
        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Comment comment = commentService.updateComment(commentId, content);
            return ResponseEntity.ok(comment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }
    
    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }
}