package com.hybris.tlv.data.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import io.ktor.http.decodeURLQueryComponent
import io.ktor.http.encodeURLQueryComponent
import com.hybris.tlv.core.telemetry.Telemetry

/**
 * A [Json] instance configured with a lenient policy:
 * - **Lenient Parsing:** Accepts quoted boolean values and other relaxed JSON standards.
 * - **Forward Compatibility:** Ignores unknown keys in the JSON input to prevent crashes when API schemas evolve.
 * - **Persistence:** Explicitly encodes default values to ensure data integrity during storage.
 * - **Formatting:** Permits trailing commas, facilitating easier manual editing of configuration files.
 */
@OptIn(ExperimentalSerializationApi::class)
val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    allowTrailingComma = true
}

/**
 * Attempts to transform an object of type [T] into a JSON [String].
 *
 * @param T The type of the object to encode.
 * @param jsonSerializer The [Json] configuration to use. Defaults to [json].
 * @param value The object instance to be serialized.
 * @return A JSON formatted string, or `null` if the value is null or serialization fails.
 */
inline fun <reified T> encode(jsonSerializer: Json = json, value: T?): String? = runCatching {
    value?.let { jsonSerializer.encodeToString(value = value) }
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to encode value", throwable = it)
}.getOrNull()

/**
 * Attempts to transform a JSON [String] back into an object of type [T].
 * This decoder includes a "safety net" for blank strings:
 * - If the input is blank and [T] is a [Collection], it attempts to decode from `[{}]`.
 * - For other types, it attempts to decode from `{}`.
 *
 * @param T The expected target type.
 * @param jsonSerializer The [Json] configuration to use. Defaults to [json].
 * @param value The JSON string to be deserialized.
 * @return An instance of [T], or `null` if decoding fails or the input is null.
 */
inline fun <reified T> decode(jsonSerializer: Json = json, value: String?): T? = runCatching {
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
 * Encodes a value into a JSON string and applies URL query component encoding.
 * This is useful for passing complex objects or configurations via URL parameters.
 *
 * @param value The object instance to encode.
 * @return A URL-safe string representation, or the literal string "null" on failure.
 */
inline fun <reified T> encodeURL(value: T?): String = runCatching {
    encode(value = value)?.encodeURLQueryComponent()
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to encode URL value", throwable = it)
}.getOrNull() ?: "null"

/**
 * Decodes a URL-encoded JSON string back into an object of type [T].
 *
 * @param value The URL-safe string retrieved from a query component.
 * @return An instance of [T], or `null` if the string is literal "null" or decoding fails.
 */
inline fun <reified T> decodeURL(value: String?): T? = runCatching {
    if (value == "null") return null
    decode<T>(value = value?.decodeURLQueryComponent())
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to decode URL value", throwable = it)
}.getOrNull()

const val TAG = "JSON"
