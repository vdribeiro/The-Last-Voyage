package com.hybris.tlv.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

internal class AndroidLocalConfig(context: Context): LocalConfig {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    override fun getString(key: StorageKey): String = runCatching {
        sharedPreferences.getString(key.key, key.defaultValue.asString()) ?: key.defaultValue.asString()
    }.getOrDefault(defaultValue = key.defaultValue.asString())

    override fun getBoolean(key: StorageKey): Boolean = runCatching {
        sharedPreferences.getBoolean(key.key, key.defaultValue.asBoolean())
    }.getOrDefault(defaultValue = key.defaultValue.asBoolean())

    override fun getLong(key: StorageKey): Long = runCatching {
        sharedPreferences.getLong(key.key, key.defaultValue.asLong())
    }.getOrDefault(defaultValue = key.defaultValue.asLong())

    override fun getDouble(key: StorageKey): Double = runCatching {
        Double.fromBits(bits = sharedPreferences.getLong(key.key, key.defaultValue.asDouble().toRawBits()))
    }.getOrDefault(defaultValue = key.defaultValue.asDouble())

    override fun put(key: StorageKey, value: String?) =
        sharedPreferences.edit { putString(key.key, value) }

    override fun put(key: StorageKey, value: Boolean?) =
        sharedPreferences.edit { if (value != null) putBoolean(key.key, value) else remove(key.key) }

    override fun put(key: StorageKey, value: Long?) =
        sharedPreferences.edit { if (value != null) putLong(key.key, value) else remove(key.key) }

    override fun put(key: StorageKey, value: Double?) =
        sharedPreferences.edit { if (value != null) putLong(key.key, value.toRawBits()) else remove(key.key) }
}
