package com.hybris.tlv.storage

import java.io.File
import com.hybris.tlv.platform.Platform
import com.hybris.tlv.platform.Property
import com.hybris.tlv.platform.getPlatform
import com.hybris.tlv.telemetry.Telemetry

internal val appDataDir: File by lazy {
    val baseDir = when (getPlatform()) {
        Platform.Windows -> System.getenv("APPDATA")
        Platform.Mac -> "${System.getProperty("user.home")}/Library/Application Support"
        Platform.Linux -> with(receiver = System.getenv("XDG_DATA_HOME")) {
            if (!isNullOrBlank()) this else "${System.getProperty("user.home")}/.local/share"
        }

        Platform.Android, Platform.Ios, Platform.Unknown -> "${System.getProperty("user.home")}/.local/share"
    }
    val appDir = Property.APP_NAME
        .lowercase()
        .replace(regex = "\\s+".toRegex(), replacement = "")
    File(baseDir, appDir).also { if (!it.exists()) it.mkdirs() }
}

internal actual suspend fun saveFile(path: String, content: String): Boolean = runCatching {
    val file = File(appDataDir, path)
    file.writeText(text = content)
    true
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to save file", throwable = it) }.getOrDefault(defaultValue = false)

internal actual suspend fun loadFile(path: String): String? = runCatching {
    val file = File(appDataDir, path)
    if (file.exists() && file.isFile) file.readText() else null
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to load file", throwable = it) }.getOrNull()

private const val TAG = "File"
