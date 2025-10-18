package com.hybris.tlv.storage

import java.io.File
import com.hybris.tlv.platform.Property
import com.hybris.tlv.telemetry.Telemetry

internal val appDataDir: File by lazy {
    val os = System.getProperty("os.name").lowercase()
    val baseDir = when {
        os.contains(other = "win") -> System.getenv("APPDATA")
        os.contains(other = "mac") -> "${System.getProperty("user.home")}/Library/Application Support"
        else -> "${System.getProperty("user.home")}/.local/share"
    }
    val appDir = Property.APP_NAME
        .lowercase()
        .replace(regex = "\\s+".toRegex(), replacement = "")
    File(baseDir, appDir).also {
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
