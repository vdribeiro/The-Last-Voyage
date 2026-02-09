@file:ShadowedInTesting

package com.hybris.tlv.core.resource

import kotlinx.coroutines.withContext
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting
import thelastvoyage.composeapp.generated.resources.Res

/**
 * Reads the content of the resource file at the specified path and returns it as a string.
 * Returns null on failure.
 */
internal suspend fun loadResource(path: String): String? = withContext(context = Dispatcher.IO) {
    runCatching {
        Res.readBytes(path = path).decodeToString()
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to read resource", throwable = it) }.getOrNull()
}

private const val TAG = "ResourceLoader"
