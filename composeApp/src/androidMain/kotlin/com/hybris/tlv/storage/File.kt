package com.hybris.tlv.storage

import java.io.File
import com.hybris.tlv.applicationContext
import com.hybris.tlv.telemetry.Telemetry

private val appDataDir: File by lazy {
    applicationContext.filesDir.also {
        if (!it.exists()) it.mkdirs()
    }
}

internal actual suspend fun saveFile(path: String, content: String): Boolean = runCatching {
    val file = File(appDataDir, path)
    file.writeText(text = content)
    true
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to save file", throwable = it)
    false
}

internal actual suspend fun loadFile(path: String): String? = runCatching {
    val file = File(appDataDir, path)
    if (file.exists() && file.isFile) file.readText() else null
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to load file", throwable = it)
    null
}

private const val TAG = "File"