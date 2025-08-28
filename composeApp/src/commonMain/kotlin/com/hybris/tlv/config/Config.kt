package com.hybris.tlv.config

import com.hybris.tlv.http.CONFIGS_URL
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

    private var cache: Configs = Configs()
    override val configs: Configs get() = cache

    override suspend fun getLocal(): Configs =
        loadFile(fileName = "configs.json")?.let {
            json.decodeFromString<Configs>(string = it)
        } ?: Configs().also { setLocal(configs = it) }

    override suspend fun getRemote(): Configs =
        when (val result = httpClient.getStream<Configs>(path = CONFIGS_URL)) {
            is Result.Error -> null.also { Logger.error(tag = TAG, message = result.error) }
            is Result.Success -> result.list.firstOrNull()
        } ?: Configs()

    override suspend fun setLocal(configs: Configs) {
        cache = configs
        saveFile(fileName = "configs.json", content = json.encodeToString(value = configs))
    }

    companion object Companion {
        private const val TAG = "Storage"
    }
}