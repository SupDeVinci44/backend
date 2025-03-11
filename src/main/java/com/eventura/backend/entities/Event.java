package com.eventura.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String location;

    private String imageUrl;

    @Column(unique = true)
    private String externalId;  // ID de l'événement dans l'API externe

    // Relations
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();
    
    // Méthodes utilitaires
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setEvent(this);
    }
    
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setEvent(null);
    }
    
    public void addLike(Like like) {
        likes.add(like);
        like.setEvent(this);
    }
    
    public void removeLike(Like like) {
        likes.remove(like);
        like.setEvent(null);
    }
    
    public int getLikeCount() {
        return likes.size();
    }
} 