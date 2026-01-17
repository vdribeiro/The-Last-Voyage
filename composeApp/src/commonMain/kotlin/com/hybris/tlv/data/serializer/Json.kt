@file:ExcludeFromTesting

package com.hybris.tlv.data.serializer

import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import io.ktor.http.decodeURLQueryComponent
import io.ktor.http.encodeURLQueryComponent
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.data.storage.deleteFile
import com.hybris.tlv.data.storage.loadFile
import com.hybris.tlv.data.storage.saveFile
import com.hybris.tlv.resource.JsonResource
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ExcludeFromTesting
import thelastvoyage.composeapp.generated.resources.Res

/**
 * A lenient JSON serializer.
 */
@OptIn(ExperimentalSerializationApi::class)
internal val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    allowTrailingComma = true
}

/**
 * Safely encodes a given [value] of type [T] into a JSON string.
 * Returns null if encoding fails or the value is null.
 */
internal inline fun <reified T> encode(value: T?): String? = runCatching {
    value?.let { json.encodeToString(value = value) }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to encode value", throwable = it) }.getOrNull()

/**
 * Safely decodes a JSON string [value] into an object of type [T].
 * Returns null if decoding fails or the value is null.
 * If [value] is blank, it provides a default empty JSON object or array to prevent deserialization errors for empty or collection types.
 */
internal inline fun <reified T> decode(value: String?): T? = runCatching {
    value?.let {
        json.decodeFromString<T>(string = value.ifBlank {
            when (T::class) {
                Collection::class -> "[{}]"
                else -> "{}"
            }
        })
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to decode value", throwable = it) }.getOrNull()

/**
 * Safely encodes a given value of type [T] into a URL-safe JSON string.
 * Returns "null" if encoding fails or the value is null.
 */
internal inline fun <reified T> encodeURL(value: T?): String = runCatching {
    encode(value = value)?.encodeURLQueryComponent()
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to encode URL value", throwable = it) }.getOrNull() ?: "null"

/**
 * Safely decodes a URL-safe JSON string into an object of type [T].
 * Returns null if decoding fails or the value is null or "null".
 */
internal inline fun <reified T> decodeURL(value: String?): T? = runCatching {
    if (value == "null") return null
    decode<T>(value = value?.decodeURLQueryComponent())
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to decode URL value", throwable = it) }.getOrNull()

/**
 * Saves a serializable object of type [T] to a file at the specified [json].
 * The object is first encoded to a JSON string before being saved.
 * Returns true if the file was saved successfully, false otherwise.
 */
internal suspend inline fun <reified T> saveJsonFile(json: JsonFile, content: T): Boolean = withContext(context = Dispatcher.IO) {
    encode<T>(value = content)?.let { saveFile(path = json.path, content = it) } ?: false
}

/**
 * Loads and decodes a JSON file from the specified path into an object of type [T].
 * Returns null if the file doesn't exist or decoding fails.
 */
internal suspend inline fun <reified T> loadJsonFile(json: JsonFile): T? = withContext(context = Dispatcher.IO) {
    loadFile(path = json.path)?.let { decode<T>(value = it) }
}

internal suspend fun deleteJsonFile(json: JsonFile): Boolean = withContext(context = Dispatcher.IO) {
    deleteFile(path = json.path)
}

/**
 * Loads and decodes a JSON resource from the application's resources into a list of objects of type [T].
 * Returns an empty list if loading or decoding fails.
 */
internal suspend inline fun <reified T> loadFromJsonResource(json: JsonResource): List<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        decode<List<T>>(value = Res.readBytes(path = json.path).decodeToString())
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to load resource", throwable = it) }.getOrNull().orEmpty()
}

private const val TAG = "JSON"
