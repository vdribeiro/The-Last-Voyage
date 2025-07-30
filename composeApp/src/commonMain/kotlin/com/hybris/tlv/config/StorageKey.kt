package com.hybris.tlv.config

internal sealed class StorageKey(val key: String, val defaultValue: Any) {
    // Versioning
    data object TranslationsVersion: StorageKey(key = "translations_version", defaultValue = 1L)
    data object CatastrophesVersion: StorageKey(key = "catastrophes_version", defaultValue = 1L)
    data object EnginesVersion: StorageKey(key = "engines_version", defaultValue = 1L)
    data object StellarHostsVersion: StorageKey(key = "stellar_hosts_version", defaultValue = 1L)
    data object PlanetsVersion: StorageKey(key = "planets_version", defaultValue = 1L)
    data object EventsVersion: StorageKey(key = "events_version", defaultValue = 1L)
    data object AchievementsVersion: StorageKey(key = "achievements_version", defaultValue = 1L)
    data object CreditsVersion: StorageKey(key = "credits_version", defaultValue = 1L)

    // Music
    data object Music: StorageKey(key = "music", defaultValue = true)
}

internal fun Any.asBoolean(): Boolean = this as Boolean
internal fun Any.asString(): String = this as String
internal fun Any.asLong(): Long = this as Long
internal fun Any.asDouble(): Double = this as Double
