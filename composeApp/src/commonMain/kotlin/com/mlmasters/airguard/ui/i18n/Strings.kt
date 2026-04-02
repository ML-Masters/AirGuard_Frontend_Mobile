package com.mlmasters.airguard.ui.i18n

import androidx.compose.runtime.*

object AppLanguage {
    var current: String by mutableStateOf("fr")
}

object S {
    // Common
    val loading get() = tr("Chargement...", "Loading...")
    val retry get() = tr("Réessayer", "Retry")
    val save get() = tr("Enregistrer", "Save")
    val cancel get() = tr("Annuler", "Cancel")
    val close get() = tr("Fermer", "Close")
    val or get() = tr("ou", "or")
    val search get() = tr("Rechercher...", "Search...")

    // Login
    val appName get() = "AirGuard"
    val loginSubtitle get() = tr("Surveillez l'air que vous respirez", "Monitor the air you breathe")
    val email get() = tr("Email", "Email")
    val password get() = tr("Mot de passe", "Password")
    val passwordMin get() = tr("Mot de passe (6 car. min.)", "Password (6 char. min.)")
    val login get() = tr("Se connecter", "Sign in")
    val continueGoogle get() = tr("Continuer avec Google", "Continue with Google")
    val noAccount get() = tr("Pas de compte ? ", "No account? ")
    val signUp get() = tr("S'inscrire", "Sign up")
    val alreadyAccount get() = tr("Déjà un compte ? ", "Already have an account? ")
    val signIn get() = tr("Se connecter", "Sign in")
    val hackathon get() = tr("Hackathon IndabaX Cameroun 2026", "Hackathon IndabaX Cameroon 2026")
    val team get() = "ML Masters"

    // Register
    val createAccount get() = tr("Créez votre compte", "Create your account")
    val register get() = tr("Inscription", "Registration")
    val firstName get() = tr("Prénom", "First name")
    val lastName get() = tr("Nom", "Last name")
    val signUpGoogle get() = tr("S'inscrire avec Google", "Sign up with Google")
    val yourCity get() = tr("Votre ville de résidence", "Your city of residence")

    // Onboarding
    val whereDoYouLive get() = tr("Où habitez-vous ?", "Where do you live?")
    val chooseCityDesc get() = tr(
        "Choisissez votre ville pour recevoir les alertes et suivre la qualité de l'air chez vous.",
        "Choose your city to receive alerts and track air quality in your area."
    )
    val searchCity get() = tr("Rechercher une ville...", "Search for a city...")
    val continueBtn get() = tr("Continuer", "Continue")
    val skipStep get() = tr("Passer cette étape", "Skip this step")

    // Home
    fun greeting(name: String) = if (name.isNotEmpty()) tr("Bonjour $name", "Hello $name") else tr("Bonjour", "Hello")
    val airToday get() = tr("Comment est l'air aujourd'hui ?", "How is the air today?")
    val dangerousAir get() = tr("Air dangereux", "Dangerous air")
    val citiesAtRisk get() = tr("à risque", "at risk")
    fun nationalAvg(avg: Int, count: Int) = tr(
        "Indice national moyen : $avg · $count villes",
        "National average index: $avg · $count cities"
    )

    val alerts get() = tr("Alertes", "Alerts")
    val ongoing get() = tr("en cours", "ongoing")
    val activeAlerts get() = tr("Alertes en cours", "Active alerts")
    val tomorrowForecast get() = tr("Prévision pour demain", "Tomorrow's forecast")

    // Cities
    val cities get() = tr("Villes", "Cities")
    val monitoredCities get() = tr("villes surveillées", "monitored cities")
    val all get() = tr("Tout", "All")
    val good get() = tr("Bon", "Good")
    val acceptable get() = tr("Acceptable", "Acceptable")
    val degraded get() = tr("Dégradé", "Degraded")
    val unhealthy get() = tr("Malsain", "Unhealthy")

    // City detail
    val currentAirQuality get() = tr("Qualité de l'air actuelle", "Current air quality")
    val weekForecast get() = tr("Prévisions de la semaine", "Weekly forecast")
    val tomorrowPredictions get() = tr("Prévisions pour demain", "Tomorrow's predictions")
    val airQualityPred get() = tr("Prévision air", "Air forecast")
    val pollutionForecast get() = tr("Pollution prévue", "Pollution forecast")
    val estimatedAqi get() = tr("Indice prévu", "Estimated index")
    val category get() = tr("Catégorie", "Category")
    val heatHealth get() = tr("Température ressentie", "Perceived temperature")
    val feelsLike get() = tr("Ressenti", "Feels like")
    val heatRisk get() = tr("Risque chaleur", "Heat risk")
    val warning get() = tr("Avertissement", "Warning")
    val naturalRisks get() = tr("Risques météo", "Weather risks")
    val drought get() = tr("Sécheresse", "Drought")
    val floodRisk get() = tr("Inondation", "Flooding")

    // Alerts
    val activeAlertsTitle get() = tr("Alertes actives", "Active alerts")
    val noAlerts get() = tr("Aucune alerte active", "No active alerts")
    val recommendations get() = tr("Recommandations", "Recommendations")
    val estimatedDuration get() = tr("Durée estimée", "Estimated duration")
    fun alertCount(n: Int) = tr(
        "$n alerte${if (n > 1) "s" else ""}",
        "$n alert${if (n > 1) "s" else ""}"
    )

    // Chat
    val chatBot get() = "AirGuard Bot"
    val askQuestion get() = tr("Posez votre question...", "Ask your question...")
    val chatGreeting get() = tr(
        "Bonjour ! Je suis AirGuard Bot. Posez-moi vos questions sur la qualité de l'air au Cameroun.",
        "Hello! I'm AirGuard Bot. Ask me your questions about air quality in Cameroon."
    )
    val chatUnavailable get() = tr("Désolé, je suis temporairement indisponible.", "Sorry, I'm temporarily unavailable.")

    // Profile
    val myProfile get() = tr("Mon profil", "My profile")
    val manageInfo get() = tr("Gérez vos informations", "Manage your information")
    val account get() = tr("Compte", "Account")
    val personalInfo get() = tr("Informations personnelles", "Personal information")
    val editNameSub get() = tr("Modifier nom et prénom", "Edit first and last name")
    val emailAddress get() = tr("Adresse email", "Email address")
    val cityOfResidence get() = tr("Ville de résidence", "City of residence")
    val changeCity get() = tr("Changer de ville", "Change city")
    val preferences get() = tr("Préférences", "Preferences")
    val notifications get() = tr("Notifications", "Notifications")
    val airAlerts get() = tr("Alertes qualité de l'air", "Air quality alerts")
    val language get() = tr("Langue", "Language")
    val french get() = tr("Français", "French")
    val english get() = tr("Anglais", "English")
    val other get() = tr("Autres", "Other")
    val about get() = tr("À propos", "About")
    val logout get() = tr("Se déconnecter", "Log out")
    val editName get() = tr("Modifier le nom", "Edit name")
    val changeLanguage get() = tr("Choisir la langue", "Choose language")
    val notSpecified get() = tr("Non renseigné", "Not specified")

    // Navigation
    val navHome get() = tr("Accueil", "Home")
    val navCities get() = tr("Villes", "Cities")
    val navAlerts get() = tr("Alertes", "Alerts")
    val navChat get() = tr("Chat", "Chat")
    val navProfile get() = tr("Profil", "Profile")

    // AQI labels
    fun aqiLabel(categorie: String) = when (categorie) {
        "Bon" -> tr("Air pur", "Clean air")
        "Modere", "Modéré" -> tr("Air acceptable", "Acceptable air")
        "Sensible" -> tr("Air dégradé", "Degraded air")
        "Malsain" -> tr("Air malsain", "Unhealthy air")
        "Tres_malsain", "Très malsain" -> tr("Air très malsain", "Very unhealthy air")
        "Dangereux" -> tr("Air dangereux", "Dangerous air")
        else -> categorie
    }

    fun aqiDescription(categorie: String) = when (categorie) {
        "Bon" -> tr("La qualité de l'air est excellente", "Air quality is excellent")
        "Modere", "Modéré" -> tr("La qualité de l'air est correcte", "Air quality is fair")
        "Sensible" -> tr("Peut affecter les personnes sensibles", "May affect sensitive individuals")
        "Malsain" -> tr("Risque pour la santé de tous", "Health risk for everyone")
        "Tres_malsain", "Très malsain" -> tr("Danger pour la santé", "Health danger")
        "Dangereux" -> tr("Urgence sanitaire", "Health emergency")
        else -> ""
    }

    fun aqiAdvice(categorie: String) = when (categorie) {
        "Bon" -> tr("Profitez de vos activités en plein air !", "Enjoy your outdoor activities!")
        "Modere", "Modéré" -> tr("Les personnes sensibles doivent rester vigilantes.", "Sensitive people should be cautious.")
        "Sensible" -> tr(
            "Limitez les efforts physiques prolongés en extérieur. Enfants et personnes âgées : restez prudents.",
            "Limit prolonged outdoor physical exertion. Children and elderly: stay cautious."
        )
        "Malsain" -> tr(
            "Évitez les activités en extérieur. Fermez les fenêtres. Portez un masque si vous sortez.",
            "Avoid outdoor activities. Close windows. Wear a mask if you go outside."
        )
        "Tres_malsain", "Très malsain" -> tr(
            "Restez à l'intérieur. Ne faites aucun effort physique dehors. Protégez les enfants et personnes âgées.",
            "Stay indoors. No physical exertion outside. Protect children and the elderly."
        )
        "Dangereux" -> tr(
            "NE SORTEZ PAS. Fermez portes et fenêtres. Appelez le 119 en cas de difficulté respiratoire.",
            "DO NOT go outside. Close doors and windows. Call 119 for breathing difficulties."
        )
        else -> ""
    }

    private fun tr(fr: String, en: String): String =
        if (AppLanguage.current == "en") en else fr
}
