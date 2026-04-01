package com.hybris.tlv.data.storage

import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import com.hybris.tlv.core.flow.Dispatcher
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
 * Saves a serializable object of type [T] to a file at the specified [path].
 * Returns true if the file was saved successfully, false otherwise.
 */
internal suspend inline fun <reified T> saveJsonFile(path: FilePath, content: T): Boolean = withContext(context = Dispatcher.IO) {
    encode<T>(jsonSerializer = prettyJson, value = content)?.let { saveFile(path = path.path, content = it) } ?: false
}

/**
 * Loads and decodes a JSON file from the specified [path] into an object of type [T].
 * Returns null if the file doesn't exist or decoding fails.
 */
internal suspend inline fun <reified T> loadJsonFile(path: FilePath): T? = withContext(context = Dispatcher.IO) {
    loadFile(path = path.path)?.let { decode<T>(value = it) }
}

/**
 * Deletes a JSON file in the specified [path].
 * Returns true if the file was deleted successfully, false otherwise.
 */
internal suspend fun deleteJsonFile(path: FilePath): Boolean = withContext(context = Dispatcher.IO) {
    deleteFile(path = path.path)
}
