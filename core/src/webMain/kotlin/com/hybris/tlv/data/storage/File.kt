package com.hybris.tlv.data.storage

import kotlinx.browser.localStorage
import kotlinx.coroutines.withContext
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.platform.Property

actual val appDataPath: String by lazy {
    Property.APP_NAME.lowercase().replace(regex = "\\s+".toRegex(), replacement = "") + "_storage"
}

actual suspend fun saveFile(path: String, content: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        localStorage.setItem(key = "${appDataPath}_$path", value = content)
        true
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to save file $path", throwable = it)
    }.getOrDefault(defaultValue = false)
}

actual suspend fun loadFile(path: String): String? = withContext(context = Dispatcher.IO) {
    runCatching {
        localStorage.getItem(key = "${appDataPath}_$path")
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to load file $path", throwable = it)
    }.getOrNull()
}

actual suspend fun deleteFile(path: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        localStorage.removeItem(key = "${appDataPath}_$path")
        true
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to delete file $path", throwable = it)
    }.getOrDefault(defaultValue = false)
}

private const val TAG = "File"
