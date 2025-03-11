package com.eventura.backend.services;

import com.eventura.backend.entities.Event;
import com.eventura.backend.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    
    // URL de l'API externe (à configurer dans application.properties)
    private final String apiUrl = "${external.api.url}";
    
    @Transactional(readOnly = true)
    public Page<Event> getUpcomingEvents(Pageable pageable) {
        return eventRepository.findByStartDateTimeAfterOrderByStartDateTimeAsc(
                LocalDateTime.now(), pageable);
    }
    
    @Transactional(readOnly = true)
    public List<Event> getEventsByDateRange(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByDateRange(start, end);
    }
    
    @Transactional(readOnly = true)
    public Page<Event> searchEvents(String keyword, Pageable pageable) {
        return eventRepository.searchByTitleOrDescription(keyword, pageable);
    }
    
    @Transactional(readOnly = true)
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Event> getEventByExternalId(String externalId) {
        return eventRepository.findByExternalId(externalId);
    }
    
    @Transactional
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }
    
    /**
     * Méthode pour synchroniser les événements depuis l'API externe
     * Exécutée automatiquement toutes les 24 heures
     */
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    @Transactional
    public void syncEventsFromExternalApi() {
        // Logique pour récupérer les événements depuis l'API externe
        // et les sauvegarder dans notre base de données
        
        // Exemple : 
        // ExternalEventDto[] externalEvents = restTemplate.getForObject(apiUrl, ExternalEventDto[].class);
        // 
        // for (ExternalEventDto externalEvent : externalEvents) {
        //     Optional<Event> existingEvent = eventRepository.findByExternalId(externalEvent.getId());
        //     
        //     if (existingEvent.isPresent()) {
        //         // Mettre à jour l'événement existant
        //         Event event = existingEvent.get();
        //         updateEventFromDto(event, externalEvent);
        //         eventRepository.save(event);
        //     } else {
        //         // Créer un nouvel événement
        //         Event newEvent = convertToEvent(externalEvent);
        //         eventRepository.save(newEvent);
        //     }
        // }
    }
    
    // Méthodes privées pour convertir les données de l'API externe en entités Event
    // private Event convertToEvent(ExternalEventDto dto) { ... }
    // private void updateEventFromDto(Event event, ExternalEventDto dto) { ... }
}
