package com.hybris.tlv.config

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.http.HttpClientFactory.Companion.CONFIGS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.json
import com.hybris.tlv.storage.loadFile
import com.hybris.tlv.storage.saveFile
import io.ktor.client.HttpClient

internal class Config(
    private val dispatcher: Dispatcher,
    private val httpClient: HttpClient,
): ConfigManager {

    override var localConfigs: Configs = Configs()
    private var remoteCache: Configs = Configs()
    override val remoteConfigs: Configs get() = remoteCache

    init {
        fetchLocal()
        fetchRemote()
    }

    private fun fetchLocal() = dispatcher.io.launch {
        this@Config.localConfigs = loadFile(fileName = "configs.json")?.let {
            runCatching { json.decodeFromString<Configs>(string = it) }.getOrNull()
        } ?: Configs().also { flush(configs = it) }
    }

    private fun fetchRemote() = dispatcher.io.launch {
        val remoteConfigs = when (val result = httpClient.getStream<Configs>(path = CONFIGS_URL)) {
            is Result.Error -> null.also { Logger.error(tag = TAG, message = result.error) }
            is Result.Success -> result.list.firstOrNull()
        } ?: Configs()
        remoteCache = remoteConfigs
    }

    override suspend fun flush(configs: Configs) {
        runCatching { json.encodeToString(value = configs) }.getOrNull()?.let {
            saveFile(fileName = "configs.json", content = it)
        }
    }

    companion object Companion {
        private const val TAG = "Config"
    }
}
