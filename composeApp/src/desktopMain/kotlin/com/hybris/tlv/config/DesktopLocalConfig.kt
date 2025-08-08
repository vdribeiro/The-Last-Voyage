package com.hybris.tlv.config

import java.util.prefs.Preferences

internal class DesktopLocalConfig(): LocalConfig {

    private val preferences: Preferences = Preferences.userRoot().node("preferences")

    override fun getBoolean(key: StorageKey): Boolean = runCatching {
        preferences.getBoolean(key.key, key.defaultValue.asBoolean())
    }.getOrDefault(defaultValue = key.defaultValue.asBoolean())

    override fun getString(key: StorageKey): String = runCatching {
        preferences.get(key.key, key.defaultValue.asString())
    }.getOrDefault(defaultValue = key.defaultValue.asString())

    override fun getLong(key: StorageKey): Long = runCatching {
        preferences.getLong(key.key, key.defaultValue.asLong())
    }.getOrDefault(defaultValue = key.defaultValue.asLong())

    override fun getDouble(key: StorageKey): Double = runCatching {
        preferences.getDouble(key.key, key.defaultValue.asDouble())
    }.getOrDefault(defaultValue = key.defaultValue.asDouble())

    override fun put(key: StorageKey, value: Boolean?) =
        if (value != null) preferences.putBoolean(key.key, value) else preferences.remove(key.key)

    override fun put(key: StorageKey, value: String?) =
        if (value != null) preferences.put(key.key, value) else preferences.remove(key.key)

    override fun put(key: StorageKey, value: Long?) =
        if (value != null) preferences.putLong(key.key, value) else preferences.remove(key.key)

    override fun put(key: StorageKey, value: Double?) =
        if (value != null) preferences.putDouble(key.key, value) else preferences.remove(key.key)
}
