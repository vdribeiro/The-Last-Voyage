package com.hybris.tlv.config

import com.hybris.tlv.http.HttpClientFactory.Companion.CONFIGS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.json
import com.hybris.tlv.storage.loadFile
import com.hybris.tlv.storage.saveFile
import io.ktor.client.HttpClient

internal class Config(
    private val httpClient: HttpClient,
): ConfigManager {

    private var localCache: Configs = Configs()
    override val localConfigs: Configs get() = localCache
    private var remoteCache: Configs = Configs()
    override val remoteConfigs: Configs get() = remoteCache

    override suspend fun fetchLocal() {
        val localConfigs = loadFile(fileName = "configs.json")?.let {
            json.decodeFromString<Configs>(string = it)
        } ?: Configs().also { setLocal(configs = it) }
        localCache = localConfigs
    }

    override suspend fun fetchRemote() {
        val remoteConfigs = when (val result = httpClient.getStream<Configs>(path = CONFIGS_URL)) {
            is Result.Error -> null.also { Logger.error(tag = TAG, message = result.error) }
            is Result.Success -> result.list.firstOrNull()
        } ?: Configs()
        remoteCache = remoteConfigs
    }

    override suspend fun setLocal(configs: Configs) {
        localCache = configs
        saveFile(fileName = "configs.json", content = json.encodeToString(value = configs))
    }

    companion object Companion {
        private const val TAG = "Config"
    }
}
