package com.hybris.tlv.storage

import kotlinx.coroutines.withContext
import com.hybris.tlv.database.deleteDatabase
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.serializer.CONFIGS_JSON
import com.hybris.tlv.serializer.PREFERENCES_JSON
import com.hybris.tlv.telemetry.Telemetry

internal suspend fun clearStorage(): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        deleteFile(path = CONFIGS_JSON)
        deleteFile(path = PREFERENCES_JSON)
        deleteDatabase()
        true
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to clear storage", throwable = it) }.getOrDefault(defaultValue = false)
}

private const val TAG = "Storage"