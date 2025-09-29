package com.hybris.tlv.storage

import com.hybris.tlv.telemetry.Logger
import java.io.File

private val appDataDir: File by lazy {
    val home = System.getProperty("user.home")
    File(home, ".TheLastVoyage").apply { if (!exists()) mkdirs() }
}

internal actual fun saveFile(path: String, content: String): Boolean = runCatching {
    val file = File(appDataDir, path)
    file.writeText(text = content)
    true
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to save file\n${it.stackTraceToString()}")
    false
}

internal actual fun loadFile(path: String): String? = runCatching {
    val file = File(appDataDir, path)
    file.readText()
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to load file\n${it.stackTraceToString()}")
    null
}

private const val TAG = "File"
