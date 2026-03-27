package com.hybris.tlv.data.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import io.ktor.http.decodeURLQueryComponent
import io.ktor.http.encodeURLQueryComponent
import com.hybris.tlv.core.telemetry.Telemetry

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
internal inline fun <reified T> encode(jsonSerializer: Json = json, value: T?): String? = runCatching {
    value?.let { jsonSerializer.encodeToString(value = value) }
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to encode value", throwable = it)
}.getOrNull()

/**
 * Safely decodes a JSON string [value] into an object of type [T].
 * Returns null if decoding fails or the value is null.
 * If [value] is blank, it provides a default empty JSON object or array to prevent deserialization errors for empty or collection types.
 */
internal inline fun <reified T> decode(jsonSerializer: Json = json, value: String?): T? = runCatching {
    value?.let {
        jsonSerializer.decodeFromString<T>(string = value.ifBlank {
            when (T::class) {
                Collection::class -> "[{}]"
                else -> "{}"
            }
        })
    }
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to decode value", throwable = it)
}.getOrNull()

/**
 * Safely encodes a given value of type [T] into a URL-safe JSON string.
 * Returns "null" if encoding fails or the value is null.
 */
internal inline fun <reified T> encodeURL(value: T?): String = runCatching {
    encode(value = value)?.encodeURLQueryComponent()
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to encode URL value", throwable = it)
}.getOrNull() ?: "null"

/**
 * Safely decodes a URL-safe JSON string into an object of type [T].
 * Returns null if decoding fails or the value is null or "null".
 */
internal inline fun <reified T> decodeURL(value: String?): T? = runCatching {
    if (value == "null") return null
    decode<T>(value = value?.decodeURLQueryComponent())
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to decode URL value", throwable = it)
}.getOrNull()

private const val TAG = "JSON"
