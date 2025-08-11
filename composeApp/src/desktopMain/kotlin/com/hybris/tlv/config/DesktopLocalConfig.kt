package com.hybris.tlv.config

import java.util.prefs.Preferences

internal class DesktopLocalConfig(): LocalConfig {

    private val preferences: Preferences = Preferences.userRoot().node("preferences")

    override fun getBoolean(key: Config): Boolean = runCatching {
        preferences.getBoolean(key.key, key.defaultValue.asBoolean())
    }.getOrDefault(defaultValue = key.defaultValue.asBoolean())

    override fun getString(key: Config): String = runCatching {
        preferences.get(key.key, key.defaultValue.asString())
    }.getOrDefault(defaultValue = key.defaultValue.asString())

    override fun getLong(key: Config): Long = runCatching {
        preferences.getLong(key.key, key.defaultValue.asLong())
    }.getOrDefault(defaultValue = key.defaultValue.asLong())

    override fun getDouble(key: Config): Double = runCatching {
        preferences.getDouble(key.key, key.defaultValue.asDouble())
    }.getOrDefault(defaultValue = key.defaultValue.asDouble())

    override fun put(key: Config, value: Boolean?) =
        if (value != null) preferences.putBoolean(key.key, value) else preferences.remove(key.key)

    override fun put(key: Config, value: String?) =
        if (value != null) preferences.put(key.key, value) else preferences.remove(key.key)

    override fun put(key: Config, value: Long?) =
        if (value != null) preferences.putLong(key.key, value) else preferences.remove(key.key)

    override fun put(key: Config, value: Double?) =
        if (value != null) preferences.putDouble(key.key, value) else preferences.remove(key.key)
}
