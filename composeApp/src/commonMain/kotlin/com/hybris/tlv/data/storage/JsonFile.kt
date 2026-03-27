package com.hybris.tlv.data.storage

import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.resource.JsonResource
import com.hybris.tlv.data.resource.loadResource
import com.hybris.tlv.data.serializer.decode
import com.hybris.tlv.data.serializer.encode

/**
 * A pretty lenient JSON serializer.
 */
@OptIn(ExperimentalSerializationApi::class)
private val prettyJson = Json {
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    allowTrailingComma = true
    prettyPrint = true
}

/**
 * Saves a serializable object of type [T] to a file at the specified [json].
 * The object is first encoded to a JSON string before being saved.
 * Returns true if the file was saved successfully, false otherwise.
 */
internal suspend inline fun <reified T> saveJsonFile(json: FilePath, content: T): Boolean = withContext(context = Dispatcher.IO) {
    encode<T>(jsonSerializer = prettyJson, value = content)?.let { saveFile(path = json.path, content = it) } ?: false
}

/**
 * Loads and decodes a JSON file from the specified path into an object of type [T].
 * Returns null if the file doesn't exist or decoding fails.
 */
internal suspend inline fun <reified T> loadJsonFile(json: FilePath): T? = withContext(context = Dispatcher.IO) {
    loadFile(path = json.path)?.let { decode<T>(value = it) }
}

internal suspend fun deleteJsonFile(json: FilePath): Boolean = withContext(context = Dispatcher.IO) {
    deleteFile(path = json.path)
}

/**
 * Loads and decodes a JSON resource from the application's resources into a list of objects of type [T].
 * Returns an empty list if loading or decoding fails.
 */
internal suspend inline fun <reified T> loadFromJsonResource(json: JsonResource): List<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        decode<List<T>>(value = loadResource(path = json.path))
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to load resource", throwable = it)
    }.getOrNull().orEmpty()
}

private const val TAG = "JSON"
