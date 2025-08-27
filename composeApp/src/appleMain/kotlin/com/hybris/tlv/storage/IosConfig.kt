package com.hybris.tlv.storage

import platform.Foundation.NSUserDefaults

internal class IosConfig(): Config {

    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults

    override fun getString(key: ConfigKey): String = runCatching {
        return userDefaults.stringForKey(defaultName = key.key) ?: key.defaultValue.asString()
    }.getOrDefault(defaultValue = key.defaultValue.asString())

    override fun getBoolean(key: ConfigKey): Boolean = runCatching {
        when (userDefaults.objectForKey(defaultName = key.key)) {
            null -> key.defaultValue.asBoolean()
            else -> userDefaults.boolForKey(defaultName = key.key)
        }
    }.getOrDefault(defaultValue = key.defaultValue.asBoolean())

    override fun getLong(key: ConfigKey): Long = runCatching {
        when (userDefaults.objectForKey(defaultName = key.key)) {
            null -> key.defaultValue.asLong()
            else -> userDefaults.integerForKey(defaultName = key.key)
        }
    }.getOrDefault(defaultValue = key.defaultValue.asLong())

    override fun getDouble(key: ConfigKey): Double = runCatching {
        when (userDefaults.objectForKey(defaultName = key.key)) {
            null -> key.defaultValue.asDouble()
            else -> userDefaults.doubleForKey(defaultName = key.key)
        }
    }.getOrDefault(defaultValue = key.defaultValue.asDouble())

    override fun put(key: ConfigKey, value: String?) =
        if (value != null) userDefaults.setObject(value = value, forKey = key.key) else userDefaults.removeObjectForKey(defaultName = key.key)

    override fun put(key: ConfigKey, value: Boolean?) =
        if (value != null) userDefaults.setBool(value = value, forKey = key.key) else userDefaults.removeObjectForKey(defaultName = key.key)

    override fun put(key: ConfigKey, value: Long?) =
        if (value != null) userDefaults.setInteger(value = value, forKey = key.key) else userDefaults.removeObjectForKey(defaultName = key.key)

    override fun put(key: ConfigKey, value: Double?) =
        if (value != null) userDefaults.setDouble(value = value, forKey = key.key) else userDefaults.removeObjectForKey(defaultName = key.key)
}

