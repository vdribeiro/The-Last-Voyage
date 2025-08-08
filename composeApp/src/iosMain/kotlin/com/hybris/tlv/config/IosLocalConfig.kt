package com.hybris.tlv.config

import platform.Foundation.NSUserDefaults

internal class IosLocalConfig(): LocalConfig {

    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults

    override fun getString(key: StorageKey): String = runCatching {
        return userDefaults.stringForKey(defaultName = key.key) ?: key.defaultValue.asString()
    }.getOrDefault(defaultValue = key.defaultValue.asString())

    override fun getBoolean(key: StorageKey): Boolean = runCatching {
        when (userDefaults.objectForKey(defaultName = key.key)) {
            null -> key.defaultValue.asBoolean()
            else -> userDefaults.boolForKey(defaultName = key.key)
        }
    }.getOrDefault(defaultValue = key.defaultValue.asBoolean())

    override fun getLong(key: StorageKey): Long = runCatching {
        when (userDefaults.objectForKey(defaultName = key.key)) {
            null -> key.defaultValue.asLong()
            else -> userDefaults.integerForKey(defaultName = key.key)
        }
    }.getOrDefault(defaultValue = key.defaultValue.asLong())

    override fun getDouble(key: StorageKey): Double = runCatching {
        when (userDefaults.objectForKey(defaultName = key.key)) {
            null -> key.defaultValue.asDouble()
            else -> userDefaults.doubleForKey(defaultName = key.key)
        }
    }.getOrDefault(defaultValue = key.defaultValue.asDouble())

    override fun put(key: StorageKey, value: String?) =
        if (value != null) userDefaults.setObject(value = value, forKey = key.key) else userDefaults.removeObjectForKey(defaultName = key.key)

    override fun put(key: StorageKey, value: Boolean?) =
        if (value != null) userDefaults.setBool(value = value, forKey = key.key) else userDefaults.removeObjectForKey(defaultName = key.key)


    override fun put(key: StorageKey, value: Long?) =
        if (value != null) userDefaults.setInteger(value = value, forKey = key.key) else userDefaults.removeObjectForKey(defaultName = key.key)

    override fun put(key: StorageKey, value: Double?) =
        if (value != null) userDefaults.setDouble(value = value, forKey = key.key) else userDefaults.removeObjectForKey(defaultName = key.key)
}

