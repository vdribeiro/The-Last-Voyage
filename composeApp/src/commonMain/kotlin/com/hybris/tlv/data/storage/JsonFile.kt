package com.hybris.tlv.data.storage

import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.data.serializer.decode
import com.hybris.tlv.data.serializer.encode

/**
 * A JSON configuration optimized for human readability within local storage.
 * This serializer is configured with a lenient policy and `prettyPrint` enabled. This ensures that local cache files are easily inspectable on the device file system.
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
 * Serializes an object of type [T] and persists it as a JSON file to the specified [path].
 *
 * @param T The type of the serializable object.
 * @param path The [FilePath] representing the target destination on disk.
 * @param content The data object to be encoded and saved.
 * @return `true` if the encoding and the platform's [saveFile] call succeeded, `false` otherwise.
 */
internal suspend inline fun <reified T> saveJsonFile(path: FilePath, content: T): Boolean = withContext(context = Dispatcher.IO) {
    encode<T>(jsonSerializer = prettyJson, value = content)?.let { saveFile(path = path.path, content = it) } ?: false
}

/**
 * Reads a JSON file from disk and attempts to deserialize its content into an object of type [T].
 *
 * @param T The expected target type for the decoded data.
 * @param path The [FilePath] where the JSON data is stored.
 * @return An instance of [T] if the file exists and is valid JSON, `null` if the file is missing or decoding fails.
 */
internal suspend inline fun <reified T> loadJsonFile(path: FilePath): T? = withContext(context = Dispatcher.IO) {
    loadFile(path = path.path)?.let { decode<T>(value = it) }
}

/**
 * Permanently removes a JSON file from the device storage.
 *
 * @param path The [FilePath] of the file to be deleted.
 * @return `true` if the file was deleted successfully or did not exist, `false` on system failure.
 */
internal suspend fun deleteJsonFile(path: FilePath): Boolean = withContext(context = Dispatcher.IO) {
    deleteFile(path = path.path)
}
