package com.hybris.tlv.data.http

import com.hybris.tlv.core.platform.Platform
import com.hybris.tlv.core.platform.platform

/**
 * A type-safe representation of all remote endpoints used within the application, serving as a centralized registry for API paths.
 *
 * @property path The full string URL for the network request.
 */
internal sealed class URL(val path: String) {
    data object ExoplanetArchive: URL(path = "$EXOPLANETS_BASE_URL/TAP/sync")
    data object Configs: URL(path = "$BASE_URL/configs.json")
    data object Translations: URL(path = "$BASE_URL/translations.json")
    data object Catastrophes: URL(path = "$BASE_URL/catastrophes.json")
    data object CatastrophesTranslations: URL(path = "$BASE_URL/catastrophes_translations.json")
    data object Engines: URL(path = "$BASE_URL/engines.json")
    data object EnginesTranslations: URL(path = "$BASE_URL/engines_translations.json")
    data object StellarHosts: URL(path = "$BASE_URL/hosts.json")
    data object Planets: URL(path = "$BASE_URL/planets.json")
    data object Events: URL(path = "$BASE_URL/events.json")
    data object EventsTranslations: URL(path = "$BASE_URL/events_translations.json")
    data object Achievements: URL(path = "$BASE_URL/achievements.json")
    data object AchievementsTranslations: URL(path = "$BASE_URL/achievements_translations.json")
    data object Credits: URL(path = "$BASE_URL/credits.json")

    companion object {
        const val EXOPLANETS_BASE_URL = "https://exoplanetarchive.ipac.caltech.edu"
        const val BASE_URL = "https://the-last-voyage.web.app/data"
        val devBaseUrl: String = when (platform) {
            Platform.Android -> "http://10.0.2.2:8080/data"
            else -> "http://localhost:8080/data"
        }
    }
}