package com.hybris.tlv.storage

import com.hybris.tlv.applicationContext
import com.hybris.tlv.telemetry.Logger
import java.io.File

private val appDataDir: File by lazy {
    applicationContext.filesDir
}

internal actual suspend fun saveFile(path: String, content: String): Boolean = runCatching {
    val file = File(appDataDir, path)
    file.writeText(text = content)
    true
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to save file\n${it.stackTraceToString()}")
    false
}

internal actual suspend fun loadFile(path: String): String? = runCatching {
    val file = File(appDataDir, path)
    file.readText()
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to load file\n${it.stackTraceToString()}")
    null
}

private const val TAG = "File"