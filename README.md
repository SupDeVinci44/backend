# Eventura - Backend

## Description

Eventura est une application d'agenda d'événements qui permet aux utilisateurs de découvrir, suivre et interagir avec des événements à venir. Ce dépôt contient le backend de l'application, développé avec Spring Boot en Java 21.

## Fonctionnalités principales

-   **Gestion des événements** : Récupération, recherche et filtrage d'événements
-   **Authentification JWT** : Système sécurisé pour l'inscription et la connexion des utilisateurs
-   **Interactions sociales** : Commentaires et likes sur les événements
-   **Modération de contenu** : Filtrage automatique des commentaires inappropriés via l'API OpenAI
-   **Synchronisation externe** : Intégration avec une API externe pour récupérer de nouveaux événements

## Prérequis techniques

-   Java 21
-   Maven
-   PostgreSQL
-   Une clé API OpenAI (pour la modération des commentaires)

## Installation et configuration

1.  **Cloner le dépôt** :

bash

Copy
git clone https://github.com/votre-organisation/eventura-backend.git
cd eventura-backend

1.  **Configurer la base de données** : Créez une base de données PostgreSQL nommée `eventura` et ajustez les paramètres de connexion dans `application.properties` :

properties

Copy
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000

spring.datasource.url=jdbc:postgresql://${POSTGRESQL_ADDON_HOST}:${POSTGRESQL_ADDON_PORT}/${POSTGRESQL_ADDON_DB}
spring.datasource.username=${POSTGRESQL_ADDON_USER}
spring.datasource.password=${POSTGRESQL_ADDON_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
springdoc.show-actuator=true
spring.jackson.serialization.fail-on-empty-beans=false

1.  **Configurer les secrets** : Ajoutez vos clés secrètes dans `application.properties` :

properties

Copy
# JWT Configuration
jwt.secret=votre\_cle\_secrete\_jwt
jwt.expiration=86400000

# OpenAI Configuration (pour la modération)
openai.api-key=votre\_cle\_api\_openai

1.  **Compiler et exécuter l'application** :

bash

Copy
mvn clean install
mvn spring-boot:run

1.  **Vérifier l'installation** : Accédez à `http://localhost:8080/swagger-ui.html` pour consulter la documentation de l'API.

## Structure du code

-   `com.eventura.backendconfiguration` : Configuration de Spring, sécurité, etc.
-   `controllers` : Points d'entrée REST API
-   `entities` : Modèles de données
-   `repositories` : Accès aux données
-   `services` : Logique métier
-   `security` : Configuration de sécurité et JWT
-   `exceptions` : Gestion des exceptions

## API Endpoints
-  `POST /actuator/health

### Authentification

-   `POST /api/auth/register` : Inscription d'un nouvel utilisateur
-   `POST /api/auth/login` : Connexion et obtention d'un token JWT

### Événements

-   `GET /api/evenements` : Liste des événements à venir
-   `GET /api/evenements/search?keyword=...` : Recherche d'événements par mot-clé
-   `GET /api/evenements/{id}` : Détails d'un événement spécifique
-   `GET /api/evenements/ville/{ville}` : Événements filtrés par ville
-   `GET /api/evenements/region/{region}` : Événements filtrés par région

### Commentaires

-   `GET /api/comments/evenement/{eventId}` : Commentaires d'un événement
-   `POST /api/comments/evenement/{eventId}` : Ajouter un commentaire
-   `PUT /api/comments/{commentId}` : Modifier un commentaire
-   `DELETE /api/comments/{commentId}` : Supprimer un commentaire

### Likes

-   `POST /api/likes/evenement/{eventId}` : Aimer/Ne plus aimer un événement
-   `GET /api/likes/evenement/{eventId}/status` : Vérifier le statut de like

## Modèle de données

Le backend utilise plusieurs entités principales :

-   **Evenement** : Détails d'un événement (titre, description, lieu, dates, etc.)
-   **Lieu** : Informations sur l'emplacement d'un événement
-   **LienDateEvenement** : Association entre événements et dates
-   **Handicap** : Types d'accessibilité pour les événements
-   **User** : Utilisateurs de l'application
-   **Comment** : Commentaires sur les événements
-   **Like** : "J'aime" sur les événements

## Sécurité

L'application utilise JWT (JSON Web Tokens) pour l'authentification. Chaque requête aux endpoints protégés doit inclure un token JWT valide dans l'en-tête Authorization :

Copy
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

## Monitoring et santé

L'application expose des endpoints Actuator pour le monitoring :

-   `GET /actuator/health` : État de santé de l'application
-   `GET /actuator/info` : Informations sur l'application

## Déploiement

L'application peut être déployée comme un service Spring Boot standard. Assurez-vous de configurer correctement les variables d'environnement ou les propriétés pour chaque environnement (développement, test, production).

## Contribution

1.  Créez une branche à partir de `dev`
2.  Implémentez vos modifications
3.  Créez une Pull Request vers `dev`
