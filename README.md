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

- Connexion email/mot de passe + Google Sign-In
- Inscription avec choix de ville de residence
- Tableau de bord citoyen (qualite air en langage clair)
- Liste des 40 villes avec indicateurs sante
- Detail ville avec predictions ML (3 risques)
- Alertes actives avec recommandations
- Chatbot IA integre
- Profil utilisateur (infos, preferences, deconnexion)
- Design adapte aux citoyens (pas de jargon technique)

## Ecrans

| Ecran | Description |
|-------|-------------|
| Login | Email/mdp + Google Sign-In |
| Inscription | Nom, email, ville de residence |
| Onboarding | Choix de ville apres Google auth |
| Accueil | KPIs, alertes, villes surveillees |
| Villes | Liste avec recherche + indicateur sante |
| Detail ville | AQI, predictions, conseils sante |
| Alertes | Alertes actives avec recommandations |
| Chat | Chatbot IA qualite de l'air |
| Profil | Infos, preferences, deconnexion |

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
