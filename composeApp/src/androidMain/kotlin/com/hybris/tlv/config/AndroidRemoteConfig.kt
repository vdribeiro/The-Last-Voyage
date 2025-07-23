package com.hybris.tlv.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.hybris.tlv.firestore.tryAwait

internal class AndroidRemoteConfig: RemoteConfig {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    override suspend fun settings(settings: RemoteConfigSettings): RemoteConfig = apply {
        remoteConfig.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(settings.minimumFetchIntervalInSeconds)
                .setFetchTimeoutInSeconds(settings.fetchTimeoutInSeconds)
                .build()
        ).tryAwait()
    }

    override suspend fun setDefaults(defaults: List<StorageKey>): RemoteConfig = apply {
        remoteConfig.setDefaultsAsync(defaults.associate { it.key to it.defaultValue }).tryAwait()
    }

    override suspend fun fetchAndActivate(): Boolean = remoteConfig.fetchAndActivate().tryAwait()

    override fun getBoolean(key: StorageKey): Boolean = runCatching {
        remoteConfig.getValue(key.key).asBoolean()
    }.getOrDefault(defaultValue = key.defaultValue.asBoolean())

    override fun getString(key: StorageKey): String = runCatching {
        remoteConfig.getValue(key.key).asString()
    }.getOrDefault(defaultValue = key.defaultValue.asString())

    override fun getLong(key: StorageKey): Long = runCatching {
        remoteConfig.getValue(key.key).asLong()
    }.getOrDefault(defaultValue = key.defaultValue.asLong())

    override fun getDouble(key: StorageKey): Double = runCatching {
        remoteConfig.getValue(key.key).asDouble()
    }.getOrDefault(defaultValue = key.defaultValue.asDouble())
}