package com.hybris.tlv.storage

import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.tasks.await

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

    override suspend fun setDefaults(defaults: List<Config>): RemoteConfig = apply {
        remoteConfig.setDefaultsAsync(defaults.associate { it.key to it.defaultValue }).tryAwait()
    }

    override suspend fun fetchAndActivate(): Boolean = remoteConfig.fetchAndActivate().tryAwait()

    override fun getBoolean(key: Config): Boolean = runCatching {
        remoteConfig.getValue(key.key).asBoolean()
    }.getOrDefault(defaultValue = key.defaultValue.asBoolean())

    override fun getString(key: Config): String = runCatching {
        remoteConfig.getValue(key.key).asString()
    }.getOrDefault(defaultValue = key.defaultValue.asString())

    override fun getLong(key: Config): Long = runCatching {
        remoteConfig.getValue(key.key).asLong()
    }.getOrDefault(defaultValue = key.defaultValue.asLong())

    override fun getDouble(key: Config): Double = runCatching {
        remoteConfig.getValue(key.key).asDouble()
    }.getOrDefault(defaultValue = key.defaultValue.asDouble())

    /**
     * Awaits the completion of the task without blocking a thread.
     * If the Job of the current coroutine is cancelled it returns false.
     */
    private suspend fun <T> Task<T>.tryAwait(): Boolean = runCatching {
        await()
        true
    }.getOrDefault(defaultValue = false)
}