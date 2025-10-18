package com.hybris.tlv.storage

import java.io.File
import com.hybris.tlv.telemetry.Telemetry

private val appDataDir: File by lazy {
    val home = System.getProperty("user.home")
    File(home, ".TheLastVoyage")
}

internal actual suspend fun saveFile(path: String, content: String): Boolean = runCatching {
    val file = File(appDataDir, path)
    val parentDir = file.parentFile
    if (parentDir != null && !parentDir.exists()) parentDir.mkdirs()
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
