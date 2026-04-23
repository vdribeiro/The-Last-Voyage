package com.hybris.tlv.data.http

import com.hybris.tlv.core.platform.Platform
import com.hybris.tlv.core.platform.isDebug
import com.hybris.tlv.core.platform.platform

/**
 * A type-safe representation of all remote endpoints used within the application, serving as a centralized registry for API paths.
 *
 * @property path The full string URL for the network request.
 */
internal sealed class URL(val path: String) {
    data object ExoplanetArchive: URL(path = "$EXOPLANETS_BASE_URL/TAP/sync")
    data object Configs: URL(path = "$baseUrl/data/configs.json")
    data object Translations: URL(path = "$baseUrl/data/translations.json")
    data object Catastrophes: URL(path = "$baseUrl/data/catastrophes.json")
    data object CatastrophesTranslations: URL(path = "$baseUrl/data/catastrophes_translations.json")
    data object Engines: URL(path = "$baseUrl/data/engines.json")
    data object EnginesTranslations: URL(path = "$baseUrl/data/engines_translations.json")
    data object StellarHosts: URL(path = "$baseUrl/data/hosts.json")
    data object Planets: URL(path = "$baseUrl/data/planets.json")
    data object Events: URL(path = "$baseUrl/data/events.json")
    data object EventsTranslations: URL(path = "$baseUrl/data/events_translations.json")
    data object Achievements: URL(path = "$baseUrl/data/achievements.json")
    data object AchievementsTranslations: URL(path = "$baseUrl/data/achievements_translations.json")
    data object Credits: URL(path = "$baseUrl/data/credits.json")

    companion object {
        const val EXOPLANETS_BASE_URL = "https://exoplanetarchive.ipac.caltech.edu"
        const val BASE_URL = "https://the-last-voyage.web.app"
        val devBaseUrl: String = when (platform) {
            Platform.Android -> "http://10.0.2.2:8080"
            else -> "http://localhost:8080"
        }
        val baseUrl: String get() = if (isDebug) devBaseUrl else BASE_URL
    }
}