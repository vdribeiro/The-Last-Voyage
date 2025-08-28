package com.hybris.tlv.storage

import com.hybris.tlv.http.CONFIGS_URL
import com.hybris.tlv.http.getStream
import io.ktor.client.HttpClient
import com.hybris.tlv.http.Result
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.json

internal class Storage(
    private val httpClient: HttpClient,
): StorageManager {

    private var cache: Config = Config()
    override val config: Config get() = cache

    override suspend fun getLocal(): Config? = runCatching {
        loadFile(fileName = "configs.json")?.let {
            json.decodeFromString<Config>(string = it)
        }
    }.getOrNull()

    override suspend fun getRemote(): Config? =
        when (val result = httpClient.getStream<Config>(path = CONFIGS_URL)) {
            is Result.Error -> null.also { Logger.error(tag = TAG, message = result.error) }
            is Result.Success -> result.list.firstOrNull()
        }

    override suspend fun setLocal(config: Config) {
        cache = config
        saveFile(fileName = "configs.json", content = json.encodeToString(value = config))
    }

    companion object Companion {
        private const val TAG = "Storage"
    }
}