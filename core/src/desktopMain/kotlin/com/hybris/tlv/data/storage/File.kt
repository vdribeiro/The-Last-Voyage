package com.hybris.tlv.data.storage

import java.io.File
import kotlinx.coroutines.withContext
import com.hybris.tlv.App
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.platform.Platform
import com.hybris.tlv.core.platform.platform
import com.hybris.tlv.core.telemetry.Telemetry

actual val appDataPath: String by lazy {
    runCatching {
        val baseDir = when (platform) {
            Platform.Windows -> System.getenv("APPDATA") ?: "${System.getProperty("user.home")}/AppData/Roaming"
            Platform.Mac -> "${System.getProperty("user.home")}/Library/Application Support"
            Platform.Linux -> System.getenv("XDG_DATA_HOME").takeIf { !it.isNullOrBlank() } ?: "${System.getProperty("user.home")}/.local/share"
            else -> "${System.getProperty("user.home")}/.local/share"
        }
        val appDir = App.NAME
            .lowercase()
            .replace(regex = "\\s+".toRegex(), replacement = "")
        File(baseDir, appDir)
            .also { if (!it.exists()) it.mkdirs() }
            .takeIf { it.exists() }
            ?.absolutePath
            ?: throw IllegalStateException("App data directory does not exist")
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to get app data path", throwable = it)
    }.getOrDefault(defaultValue = "")
}

actual suspend fun saveFile(path: String, content: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        val file = File(appDataPath, path)
        file.writeText(text = content)
        true
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to save file $path", throwable = it)
    }.getOrDefault(defaultValue = false)
}

actual suspend fun loadFile(path: String): String? = withContext(context = Dispatcher.IO) {
    runCatching {
        val file = File(appDataPath, path)
        if (file.exists() && file.isFile) file.readText() else null
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to load file $path", throwable = it)
    }.getOrNull()
}

actual suspend fun deleteFile(path: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        val file = File(appDataPath, path)
        if (file.exists()) file.delete() else true
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to delete file $path", throwable = it)
    }.getOrDefault(defaultValue = false)
}

private const val TAG = "File"
