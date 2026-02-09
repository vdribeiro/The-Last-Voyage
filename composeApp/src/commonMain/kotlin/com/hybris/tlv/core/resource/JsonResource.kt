package com.hybris.tlv.core.resource

internal sealed class JsonResource(val path: String) {
    data object Translations: JsonResource(path = "files/translations.json")
    data object Catastrophes: JsonResource(path = "files/catastrophes.json")
    data object Engines: JsonResource(path = "files/engines.json")
    data object StellarHosts: JsonResource(path = "files/hosts.json")
    data object Planets: JsonResource(path = "files/planets.json")
    data object Events: JsonResource(path = "files/events.json")
    data object Achievements: JsonResource(path = "files/achievements.json")
    data object Credits: JsonResource(path = "files/credits.json")
    data object SolarHosts: JsonResource(path = "files/solarsystem.json")
    data object SolarPlanets: JsonResource(path = "files/solarplanets.json")
}
