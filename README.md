# AirGuard Mobile

Application mobile multiplateforme de surveillance de la qualite de l'air au Cameroun.

## Stack technique

- Kotlin Multiplatform (KMP)
- Compose Multiplatform (UI partagee Android + iOS)
- Material Design 3
- Ktor (HTTP client)
- Kotlinx Serialization (JSON)
- Koin (injection de dependances)
- Navigation Compose (navigation type-safe)
- DataStore (stockage local tokens)
- Google Credential Manager (Google Sign-In)
- Firebase Cloud Messaging (notifications push)

## Fonctionnalites

| Fonctionnalite | Description |
|----------------|-------------|
| Connexion | Email/mot de passe + Google Sign-In (Credential Manager) |
| Inscription | Nom, email, mot de passe, choix de ville de residence |
| Onboarding | Ecran choix de ville apres Google auth |
| Dashboard citoyen | Qualite air en langage clair, carte prediction demain |
| Liste des 40 villes | Recherche, filtre AQI, indicateurs sante |
| Detail ville | AQI actuel, predictions ML (3 risques), previsions semaine |
| Alertes | Alertes actives avec recommandations |
| Chatbot IA | Interface messaging integree |
| Profil | Edition nom, ville, langue, deconnexion |
| i18n | Systeme complet (Strings.kt), switch de langue en temps reel |
| UX citoyen | Labels sante (pas de chiffres AQI bruts), icones Material Design |
| Auto-refresh | Rafraichissement silencieux toutes les 2 minutes |
| Securite | Redirection automatique vers login si token expire |
| Notifications | Push Firebase (Android) |

## Predictions

| Vue | Emplacement |
|-----|-------------|
| Demain | Carte sur l'ecran d'accueil |
| Semaine | Previsions detaillees sur l'ecran detail ville |

## Ecrans

| Ecran | Description |
|-------|-------------|
| Login | Email/mdp + Google Sign-In |
| Inscription | Nom, email, ville de residence |
| Onboarding | Choix de ville apres Google auth |
| Accueil | KPIs, alertes, villes surveillees, prediction demain |
| Villes | Liste 40 villes avec recherche + filtre AQI + indicateur sante |
| Detail ville | AQI, predictions 3 risques, previsions semaine |
| Alertes | Alertes actives avec recommandations |
| Chat | Chatbot IA qualite de l'air |
| Profil | Edition nom/ville/langue, deconnexion |

## Build

```bash
# Android debug
./gradlew :composeApp:assembleDebug

# Android release
./gradlew :composeApp:assembleRelease
```

## Configuration requise

- `google-services.json` dans `composeApp/`
- `airguard-release.jks` a la racine (release signing)
- SHA-1 debug et release dans Firebase Console

## API

Production : https://api.airguard-cm.duckdns.org/api/v1

## Equipe

ML Masters — Hackathon IndabaX Cameroun 2026
