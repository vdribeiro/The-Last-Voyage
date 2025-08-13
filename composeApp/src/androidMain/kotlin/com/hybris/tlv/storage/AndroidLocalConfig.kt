package com.hybris.tlv.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

internal class AndroidLocalConfig(context: Context): LocalConfig {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    override fun getString(key: Config): String = runCatching {
        sharedPreferences.getString(key.key, key.defaultValue.asString()) ?: key.defaultValue.asString()
    }.getOrDefault(defaultValue = key.defaultValue.asString())

    override fun getBoolean(key: Config): Boolean = runCatching {
        sharedPreferences.getBoolean(key.key, key.defaultValue.asBoolean())
    }.getOrDefault(defaultValue = key.defaultValue.asBoolean())

    override fun getLong(key: Config): Long = runCatching {
        sharedPreferences.getLong(key.key, key.defaultValue.asLong())
    }.getOrDefault(defaultValue = key.defaultValue.asLong())

    override fun getDouble(key: Config): Double = runCatching {
        Double.fromBits(bits = sharedPreferences.getLong(key.key, key.defaultValue.asDouble().toRawBits()))
    }.getOrDefault(defaultValue = key.defaultValue.asDouble())

    override fun put(key: Config, value: String?) =
        sharedPreferences.edit { putString(key.key, value) }

    override fun put(key: Config, value: Boolean?) =
        sharedPreferences.edit { if (value != null) putBoolean(key.key, value) else remove(key.key) }

    override fun put(key: Config, value: Long?) =
        sharedPreferences.edit { if (value != null) putLong(key.key, value) else remove(key.key) }

    override fun put(key: Config, value: Double?) =
        sharedPreferences.edit { if (value != null) putLong(key.key, value.toRawBits()) else remove(key.key) }
}
