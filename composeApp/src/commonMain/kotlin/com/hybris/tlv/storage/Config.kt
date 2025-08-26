package com.hybris.tlv.storage

internal sealed class Config(val key: String, val defaultValue: Any) {
    // Music
    data object Music: Config(key = "music", defaultValue = true)

    // Versioning
    data object AppVersion: Config(key = "app_version", defaultValue = 1L)
    data object TranslationsVersion: Config(key = "translations_version", defaultValue = 1L)
    data object CatastrophesVersion: Config(key = "catastrophes_version", defaultValue = 1L)
    data object EnginesVersion: Config(key = "engines_version", defaultValue = 1L)
    data object StellarHostsVersion: Config(key = "stellar_hosts_version", defaultValue = 1L)
    data object PlanetsVersion: Config(key = "planets_version", defaultValue = 1L)
    data object EventsVersion: Config(key = "events_version", defaultValue = 1L)
    data object AchievementsVersion: Config(key = "achievements_version", defaultValue = 1L)
    data object CreditsVersion: Config(key = "credits_version", defaultValue = 1L)

    // Weblinks
    data object DeveloperCorner: Config(
        key = "developer_corner",
        defaultValue = "https://mammoth-gallium-e97.notion.site/The-Last-Voyage-2420fa355a5080da91ffd9262f430feb"
    )

    data object Tip: Config(key = "tip", defaultValue = "https://ko-fi.com/engsoneca")
}

internal fun Any.asBoolean(): Boolean = this as Boolean
internal fun Any.asString(): String = this as String
internal fun Any.asLong(): Long = this as Long
internal fun Any.asDouble(): Double = this as Double
