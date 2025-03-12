package com.eventura.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.moderation.ModerationPrompt;
import org.springframework.ai.moderation.ModerationResponse;
import org.springframework.ai.moderation.Moderation;
import org.springframework.ai.moderation.ModerationResult;
import org.springframework.ai.moderation.Categories;
import org.springframework.ai.moderation.CategoryScores;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.ai.openai.OpenAiModerationOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentModerationService {

    private final OpenAiModerationModel moderationModel;

    /**
     * Vérifie si le contenu est approprié en utilisant l'API de modération d'OpenAI
     *
     * @param content Le contenu à modérer
     * @return true si le contenu est approprié, false sinon
     */
    public boolean isContentAppropriate(String content) {
        try {
            // Créer les options de modération
            OpenAiModerationOptions moderationOptions = OpenAiModerationOptions.builder()
                    .model("text-moderation-latest")
                    .build();

            // Créer le prompt de modération
            ModerationPrompt moderationPrompt = new ModerationPrompt(content, moderationOptions);

            // Appeler le modèle de modération
            ModerationResponse moderationResponse = moderationModel.call(moderationPrompt);

            // Accéder aux résultats de modération
            Moderation moderation = moderationResponse.getResult().getOutput();

            // Vérifier si le contenu a été signalé comme inapproprié
            // Il y a généralement un seul résultat, mais c'est une liste
            if (moderation.getResults().isEmpty()) {
                log.warn("Pas de résultat de modération obtenu");
                return true; // Accepter par défaut
            }

            ModerationResult result = moderation.getResults().get(0);
            boolean isFlagged = result.isFlagged();

            if (isFlagged) {
                log.warn("Contenu inapproprié détecté: {}", content);
                // Journaliser les catégories spécifiques signalées
                Categories categories = result.getCategories();
                log.debug("Catégories flaggées:");
                if (categories.isSexual()) log.debug("- Contenu sexuel");
                if (categories.isHate()) log.debug("- Discours haineux");
                if (categories.isHarassment()) log.debug("- Harcèlement");
                if (categories.isSelfHarm()) log.debug("- Auto-mutilation");
                if (categories.isViolence()) log.debug("- Violence");

                // Journaliser les scores de catégories
                CategoryScores scores = result.getCategoryScores();
                log.debug("Scores: sexual={}, hate={}, harassment={}, selfHarm={}, violence={}",
                        scores.getSexual(), scores.getHate(), scores.getHarassment(),
                        scores.getSelfHarm(), scores.getViolence());
            }

            return !isFlagged;
        } catch (Exception e) {
            log.error("Erreur lors de la modération du contenu", e);
            // En cas d'erreur, on accepte le contenu par défaut pour éviter de bloquer les utilisateurs
            // Vous pourriez choisir une stratégie différente selon vos besoins
            return true;
        }
    }
}