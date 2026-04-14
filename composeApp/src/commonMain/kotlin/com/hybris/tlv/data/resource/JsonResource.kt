package com.hybris.tlv.data.resource

/**
 * A type-safe index of bundled JSON assets.
 * This registry provides a centralized way to reference local configuration, game data and fallback translations.
 *
 * @property path The relative file path to the JSON asset in `commonMain/composeResources/files`.
 */
internal sealed class JsonResource(val path: String) {
    data object Translations: JsonResource(path = "files/translations.json")
    data object Catastrophes: JsonResource(path = "files/catastrophes.json")
    data object CatastrophesTranslations: JsonResource(path = "files/catastrophes_translations.json")
    data object Engines: JsonResource(path = "files/engines.json")
    data object EnginesTranslations: JsonResource(path = "files/engines_translations.json")
    data object StellarHosts: JsonResource(path = "files/hosts.json")
    data object Planets: JsonResource(path = "files/planets.json")
    data object Events: JsonResource(path = "files/events.json")
    data object EventsTranslations: JsonResource(path = "files/events_translations.json")
    data object Achievements: JsonResource(path = "files/achievements.json")
    data object AchievementsTranslations: JsonResource(path = "files/achievements_translations.json")
    data object Credits: JsonResource(path = "files/credits.json")
    data object SolarHosts: JsonResource(path = "files/solarsystem.json")
    data object SolarPlanets: JsonResource(path = "files/solarplanets.json")
}
