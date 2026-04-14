package com.hybris.tlv.data.resource

import kotlinx.coroutines.withContext
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.serializer.decode
import thelastvoyage.composeapp.generated.resources.Res

/**
 * Loads and deserializes a JSON asset into a list of type [T].
 * This utility bridges the gap between the raw file system and the data layer.
 * It leverages the [Res] API from Compose Multiplatform to read files across all platforms.
 *
 * ### Execution Details:
 * - **Context:** Runs on [Dispatcher.IO] to ensure file I/O doesn't block the UI thread.
 * - **Error Handling:** If the file is missing, the JSON is malformed, or the schema does not match [T],
 * the error is logged via [Telemetry] and an empty list is returned to prevent application crashes.
 *
 * @param T The type of the object to decode from the JSON array.
 * @param json The [JsonResource] definition containing the relative file path.
 * @return A [List] of [T] containing the decoded data, or an empty list on failure.
 */
internal suspend inline fun <reified T> loadResource(json: JsonResource): List<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        decode<List<T>>(value = Res.readBytes(path = json.path).decodeToString())
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to load resource", throwable = it)
    }.getOrNull().orEmpty()
}

private const val TAG = "ResourceLoader"
