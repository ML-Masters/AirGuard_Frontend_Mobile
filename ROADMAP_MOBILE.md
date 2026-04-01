# AirGuard Mobile — Roadmap d'implémentation

## Architecture

```
commonMain/ (code partagé Android + iOS)
├── data/
│   ├── remote/          API client (Ktor)
│   ├── model/           Data classes (DTOs)
│   └── repository/      Repositories
├── domain/              Logique métier
├── ui/
│   ├── theme/           Couleurs, typo, thème Material3
│   ├── navigation/      Navigation (écrans)
│   ├── components/      Composants réutilisables
│   └── screens/
│       ├── login/       Écran connexion
│       ├── home/        Dashboard (KPIs + carte)
│       ├── cities/      Liste des villes + détail
│       ├── alerts/      Alertes actives
│       ├── predictions/ Prédictions ML par ville
│       └── chat/        Chatbot IA
└── di/                  Injection de dépendances (Koin)
```

## Dépendances

| Lib | Rôle |
|---|---|
| Ktor Client | Requêtes HTTP multiplateforme |
| Kotlinx Serialization | JSON parsing |
| Koin | Injection de dépendances |
| Navigation Compose | Navigation entre écrans |
| DataStore | Stockage local (tokens JWT) |

---

## Phase 1 — Fondations
**Statut : Terminee** (build Android OK)

- [x] Configuration Gradle (Ktor, Serialization, Koin, DataStore)
- [x] Permission INTERNET dans AndroidManifest
- [x] Theme AirGuard (couleurs, typo, shapes Material3)
- [x] Modeles de donnees (Ville, AirQuality, Alert, KPIs, Prediction, Chat, Auth)
- [x] API Client Ktor (base URL, JWT interceptor, auto-refresh token)
- [x] Stockage local des tokens (DataStore)
- [x] Injection de dependances (Koin)

## Phase 2 — Ecrans principaux
**Statut : Terminee** (build Android OK)

- [x] Ecran Login (email, mot de passe avec eye toggle, logo, gradient)
- [x] Ecran Home/Dashboard (4 KPI cards, liste villes avec AQI, alertes recentes)
- [x] Bottom Navigation (Accueil, Villes, Alertes, Chat)
- [x] Composants reutilisables (AQIBadge, SeverityBadge, KPICard, LoadingState/ErrorState/EmptyState)
- [x] ViewModels (Login, Home, Cities, CityDetail, Alerts, Chat)
- [x] Koin DI integre (Android + iOS)
- [x] Navigation type-safe (kotlinx.serialization routes)

## Phase 3 — Fonctionnalites
**Statut : Terminee** (implemente dans Phase 2)

- [x] Ecran Liste des villes (42 villes, recherche, filtre, AQI badge)
- [x] Ecran Detail ville (AQI actuel, predictions ML 3 risques)
- [x] Ecran Alertes (alertes actives, severite, recommandations, duree)
- [x] Ecran Chat IA (interface messaging, envoi/reception, loading indicator)

## Phase 4 — Polish
**Statut : Terminee** (build Android OK)

- [x] Animations de transition (fade 300ms entre ecrans)
- [x] Loading states + etats vides + etats erreur (avec retry)
- [x] Bouton actualiser sur Home, Villes, Alertes
- [x] Deconnexion depuis le dashboard
- [ ] Notifications push Firebase (Android) — optionnel
- [ ] Cache hors-ligne basique — optionnel
- [ ] Multilingue FR/EN dans l'app — optionnel

---

## Écrans et API

| Écran | Priorité | API |
|---|---|---|
| Login | P0 | `POST /login/` |
| Home/Dashboard | P0 | `GET /villes/`, `GET /air-quality/`, `GET /national_kpis/`, `GET /alerts/active/` |
| Liste villes | P0 | `GET /villes/`, `GET /air-quality/` |
| Détail ville | P1 | `POST /predict/`, `GET /air-quality/?ville__nom=X` |
| Alertes | P0 | `GET /alerts/active/` |
| Chat IA | P1 | `POST /chat/` |
| Paramètres | P2 | Langue, notifications |

---

## API Base URL

Production : `https://api.airguard-cm.duckdns.org/api/v1`

## Couleurs

| Nom | Hex |
|---|---|
| Primary | #0F766E |
| PrimaryLight | #14B8A6 |
| PrimaryDark | #134E4A |
| PrimaryBg | #F0FDFA |
| Surface | #FFFFFF |
| Border | #E2E8F0 |
| Text | #1E293B |
| TextSecondary | #64748B |
| Bon | #22C55E |
| Modéré | #EAB308 |
| Sensible | #F97316 |
| Malsain | #EF4444 |
| Très malsain | #7C3AED |
| Dangereux | #991B1B |
