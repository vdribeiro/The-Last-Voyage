package com.hybris.tlv.data.resource

import kotlinx.coroutines.withContext
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.serializer.decode
import thelastvoyage.composeapp.generated.resources.Res

/**
 * Loads and decodes a JSON resource from the application's resources into a list of objects of type [T].
 * Returns an empty list if loading or decoding fails.
 */
internal suspend inline fun <reified T> loadResource(json: JsonResource): List<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        decode<List<T>>(value = Res.readBytes(path = json.path).decodeToString())
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to load resource", throwable = it)
    }.getOrNull().orEmpty()
}

private const val TAG = "ResourceLoader"
