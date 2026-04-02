# AirGuard Mobile — Roadmap d'implementation

## Architecture

```
commonMain/ (code partage Android + iOS)
├── data/
│   ├── remote/          API client (Ktor)
│   ├── model/           Data classes (DTOs)
│   └── repository/      Repositories
├── domain/              Logique metier
├── ui/
│   ├── theme/           Couleurs, typo, theme Material3
│   ├── navigation/      Navigation (ecrans)
│   ├── components/      Composants reutilisables
│   └── screens/
│       ├── login/       Ecran connexion
│       ├── register/    Ecran inscription
│       ├── onboarding/  Choix de ville (Google auth)
│       ├── home/        Dashboard (KPIs + villes + prediction demain)
│       ├── cities/      Liste des villes + detail + previsions semaine
│       ├── alerts/      Alertes actives
│       ├── chat/        Chatbot IA
│       └── profile/     Profil utilisateur (edition nom/ville/langue)
└── di/                  Injection de dependances (Koin)
```

## Dependances

| Lib | Role |
|---|---|
| Ktor Client | Requetes HTTP multiplateforme |
| Kotlinx Serialization | JSON parsing |
| Koin | Injection de dependances |
| Navigation Compose | Navigation entre ecrans |
| DataStore | Stockage local (tokens JWT) |
| Google Credential Manager | Google Sign-In |
| Firebase Cloud Messaging | Notifications push |

---

## Phase 1 — Fondations
**Statut : Terminee**

- [x] Configuration Gradle (Ktor, Serialization, Koin, DataStore)
- [x] Permission INTERNET dans AndroidManifest
- [x] Theme AirGuard (couleurs, typo, shapes Material3)
- [x] Modeles de donnees (Ville, AirQuality, Alert, KPIs, Prediction, Chat, Auth)
- [x] API Client Ktor (base URL, JWT interceptor, auto-refresh token)
- [x] Stockage local des tokens (DataStore)
- [x] Injection de dependances (Koin)

## Phase 2 — Ecrans principaux
**Statut : Terminee**

- [x] Ecran Login (email, mot de passe avec eye toggle, logo, gradient)
- [x] Ecran Home/Dashboard (4 KPI cards, liste villes avec AQI, alertes recentes)
- [x] Bottom Navigation (Accueil, Villes, Alertes, Chat, Profil)
- [x] Composants reutilisables (AQIBadge, SeverityBadge, KPICard, LoadingState/ErrorState/EmptyState)
- [x] ViewModels (Login, Home, Cities, CityDetail, Alerts, Chat, Profile)
- [x] Koin DI integre (Android + iOS)
- [x] Navigation type-safe (kotlinx.serialization routes)

## Phase 3 — Fonctionnalites
**Statut : Terminee**

- [x] Ecran Liste des villes (40 villes, recherche, filtre AQI, badge sante)
- [x] Ecran Detail ville (AQI actuel, predictions ML 3 risques, previsions semaine)
- [x] Ecran Alertes (alertes actives, severite, recommandations, duree)
- [x] Ecran Chat IA (interface messaging, envoi/reception, loading indicator)
- [x] Pagination des alertes (correction du bug de chargement)

## Phase 4 — Authentification avancee
**Statut : Terminee**

- [x] Google Sign-In via Credential Manager
- [x] Ecran Inscription (nom, email, mot de passe, ville de residence)
- [x] Ecran Onboarding (choix de ville apres Google auth)
- [x] Gestion des flux d'authentification (nouveau vs existant)

## Phase 5 — Polish et finitions
**Statut : Terminee**

- [x] Ecran Profil utilisateur (edition nom, ville, langue, deconnexion)
- [x] Icones Material Design (remplacement des emojis par icones Material)
- [x] Animations de transition (fade 300ms entre ecrans)
- [x] Loading states + etats vides + etats erreur (avec retry)
- [x] Bouton actualiser sur Home, Villes, Alertes
- [x] Notifications push Firebase (Android)

## Phase 6 — i18n et predictions
**Statut : Terminee**

- [x] Systeme i18n complet (Strings.kt, toutes les chaines traduites)
- [x] Switch de langue en temps reel (FR/EN) depuis le profil
- [x] UX citoyen (labels sante lisibles, pas de chiffres AQI bruts)
- [x] Prediction demain sur carte accueil
- [x] Previsions semaine sur ecran detail ville
- [x] Auto-refresh silencieux (toutes les 2 minutes)
- [x] Redirection automatique vers login si token expire

---

## Ecrans et API

| Ecran | API |
|---|---|
| Login | `POST /login/`, `POST /auth/google/` |
| Inscription | `POST /register/` |
| Onboarding | `POST /register/` (completion profil) |
| Home/Dashboard | `GET /villes/`, `GET /air-quality/`, `GET /national_kpis/`, `GET /alerts/active/`, `GET /predictions/tomorrow/` |
| Liste villes | `GET /villes/`, `GET /air-quality/` |
| Detail ville | `POST /predict/`, `GET /air-quality/?ville__nom=X`, `GET /predictions/week/` |
| Alertes | `GET /alerts/active/` |
| Chat IA | `POST /chat/` |
| Profil | `GET /users/me/`, `PATCH /users/me/` |

---

## API Base URL

Production : `https://api.airguard-cm.duckdns.org/api/v1`

## Equipe

ML Masters — Hackathon IndabaX Cameroun 2026
