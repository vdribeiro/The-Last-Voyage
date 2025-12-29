@file:ShadowedInTesting

package com.hybris.tlv.storage

import java.io.File
import kotlinx.coroutines.withContext
import com.hybris.tlv.applicationContext
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual val appDataPath: String by lazy {
    applicationContext.filesDir.also { if (!it.exists()) it.mkdirs() }.absolutePath
}

internal actual suspend fun saveFile(path: String, content: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        val file = File(appDataPath, path)
        file.writeText(text = content)
        true
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to save file $path", throwable = it) }.getOrDefault(defaultValue = false)
}

internal actual suspend fun loadFile(path: String): String? = withContext(context = Dispatcher.IO) {
    runCatching {
        val file = File(appDataPath, path)
        if (file.exists() && file.isFile) file.readText() else null
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to load file $path", throwable = it) }.getOrNull()
}

internal actual suspend fun deleteFile(path: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        val file = File(appDataPath, path)
        if (file.exists()) file.delete() else true
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to delete file $path", throwable = it) }.getOrDefault(defaultValue = false)
}

private const val TAG = "File"