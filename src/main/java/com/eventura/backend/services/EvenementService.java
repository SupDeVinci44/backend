package com.eventura.backend.services;

import com.eventura.backend.entities.*;
import com.eventura.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvenementService {

    private final EvenementRepository evenementRepository;
    private final LieuRepository lieuRepository;
    private final HandicapRepository handicapRepository;
    private final CalendrierRepository calendrierRepository;
    private final LienDateEvenementRepository lienDateEvenementRepository;
    private final LienHandicapEvenementRepository lienHandicapEvenementRepository;

    /**
     * Récupère les événements à venir
     */
    @Transactional(readOnly = true)
    public Page<Evenement> getUpcomingEvents(Pageable pageable) {
        LocalDate now = LocalDate.now();
        return evenementRepository.findUpcomingEvents(
                now.getDayOfMonth(), now.getMonthValue(), now.getYear(), pageable);
    }

    /**
     * Recherche des événements par mot-clé
     */
    @Transactional(readOnly = true)
    public Page<Evenement> searchEvents(String keyword, Pageable pageable) {
        return evenementRepository.searchByTitleOrDescription(keyword, pageable);
    }

    /**
     * Récupère les événements par ville
     */
    @Transactional(readOnly = true)
    public Page<Evenement> findByVille(String ville, Pageable pageable) {
        return evenementRepository.findByVille(ville, pageable);
    }

    /**
     * Récupère les événements par accessibilité (handicap)
     */
    @Transactional(readOnly = true)
    public Page<Evenement> findByAccessibilite(String accessibilite, Pageable pageable) {
        return evenementRepository.findByAccessibilite(accessibilite, pageable);
    }

    /**
     * Récupère un événement par son ID
     */
    @Transactional(readOnly = true)
    public Optional<Evenement> getEventById(Long id) {
        return evenementRepository.findById(id);
    }

    /**
     * Récupère les événements sur une période spécifique
     */
    @Transactional(readOnly = true)
    public Page<Evenement> findByPeriode(LocalDate debut, LocalDate fin, Pageable pageable) {
        // Trouver les IDs des événements qui ont des dates dans la période spécifiée
        List<Long> eventIds = lienDateEvenementRepository.findAll().stream()
                .filter(lien -> {
                    Calendrier cal = lien.getDate();
                    LocalDate eventDate = LocalDate.of(cal.getAnnee(), cal.getMois(), cal.getJour());
                    return !eventDate.isBefore(debut) && !eventDate.isAfter(fin);
                })
                .map(lien -> lien.getEvenement().getIdEvenement())
                .distinct()
                .collect(Collectors.toList());

        // Récupérer les événements correspondants
        List<Evenement> events = eventIds.isEmpty() ?
                new ArrayList<>() :
                evenementRepository.findAllById(eventIds);

        // Paginer manuellement
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), events.size());

        return new PageImpl<>(
                events.subList(start, end),
                pageable,
                events.size()
        );
    }

    /**
     * Sauvegarde un événement
     */
    @Transactional
    public Evenement saveEvenement(Evenement evenement) {
        return evenementRepository.save(evenement);
    }
}