package com.hybris.tlv.storage

import com.hybris.tlv.applicationContext
import com.hybris.tlv.telemetry.Logger
import java.io.File

private val appDataDir: File by lazy {
    applicationContext.filesDir
}

private fun assertFile(path: String): File {
    val file = File(appDataDir, path)
    val parentDir = file.parentFile
    if (parentDir != null && !parentDir.exists()) parentDir.mkdirs()
    return file
}

internal actual suspend fun saveFile(path: String, content: String): Boolean = runCatching {
    assertFile(path = path).writeText(text = content)
    true
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to save file", throwable = it)
    false
}

internal actual suspend fun loadFile(path: String): String? = runCatching {
    assertFile(path = path).readText()
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to load file", throwable = it)
    null
}

private const val TAG = "File"