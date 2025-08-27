package com.hybris.tlv.storage

internal sealed class ConfigKey(val key: String, val defaultValue: Any) {
    // Versioning
    data object TranslationsVersion: ConfigKey(key = "translations_version", defaultValue = 0L)
    data object CatastrophesVersion: ConfigKey(key = "catastrophes_version", defaultValue = 0L)
    data object EnginesVersion: ConfigKey(key = "engines_version", defaultValue = 0L)
    data object StellarHostsVersion: ConfigKey(key = "stellar_hosts_version", defaultValue = 0L)
    data object PlanetsVersion: ConfigKey(key = "planets_version", defaultValue = 0L)
    data object EventsVersion: ConfigKey(key = "events_version", defaultValue = 0L)
    data object AchievementsVersion: ConfigKey(key = "achievements_version", defaultValue = 0L)
    data object CreditsVersion: ConfigKey(key = "credits_version", defaultValue = 0L)

    // Weblinks
    data object DeveloperCorner: ConfigKey(
        key = "developer_corner",
        defaultValue = "https://mammoth-gallium-e97.notion.site/The-Last-Voyage-2420fa355a5080da91ffd9262f430feb"
    )

    data object Tip: ConfigKey(
        key = "tip",
        defaultValue = "https://ko-fi.com/engsoneca"
    )
}

internal fun Any.asBoolean(): Boolean = this as Boolean
internal fun Any.asString(): String = this as String
internal fun Any.asLong(): Long = this as Long
internal fun Any.asDouble(): Double = this as Double
