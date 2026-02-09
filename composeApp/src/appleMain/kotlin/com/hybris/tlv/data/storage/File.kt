package com.hybris.tlv.data.storage

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.withContext
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry

@OptIn(ExperimentalForeignApi::class)
internal actual val appDataPath: String by lazy {
    runCatching {
        val fileManager = NSFileManager.defaultManager
        val paths = NSSearchPathForDirectoriesInDomains(
            directory = NSDocumentDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
        )
        paths.firstOrNull()?.let { it as String }
            ?.also {
                if (!fileManager.fileExistsAtPath(path = it)) {
                    val success = fileManager.createDirectoryAtPath(
                        path = it,
                        withIntermediateDirectories = true,
                        attributes = null,
                        error = null
                    )
                    if (!success) throw IllegalStateException("Unable to create directory")
                }
            }
            ?.takeIf { fileManager.fileExistsAtPath(path = it) }
            ?: throw IllegalStateException("App data directory does not exist")
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to get app data path", throwable = it) }.getOrDefault(defaultValue = "")
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal actual suspend fun saveFile(path: String, content: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        val file = NSString.create(string = appDataPath).stringByAppendingPathComponent(str = path)
        NSString.create(string = content).writeToFile(
            path = file,
            atomically = true,
            encoding = NSUTF8StringEncoding,
            error = null
        )
        true
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to save file $path", throwable = it) }.getOrDefault(defaultValue = false)
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal actual suspend fun loadFile(path: String): String? = withContext(context = Dispatcher.IO) {
    runCatching {
        val fullPath = NSString.create(string = appDataPath).stringByAppendingPathComponent(str = path)
        val fileManager = NSFileManager.defaultManager
        if (fileManager.fileExistsAtPath(path = fullPath)) {
            NSString.stringWithContentsOfFile(
                path = fullPath,
                encoding = NSUTF8StringEncoding,
                error = null
            )
        } else null
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to load file $path", throwable = it) }.getOrNull()
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal actual suspend fun deleteFile(path: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        val fullPath = NSString.create(string = appDataPath).stringByAppendingPathComponent(str = path)
        val fileManager = NSFileManager.defaultManager
        if (fileManager.fileExistsAtPath(path = fullPath)) {
            fileManager.removeItemAtPath(
                path = fullPath,
                error = null
            )
        } else true
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to delete file $path", throwable = it) }.getOrDefault(defaultValue = false)
}

private const val TAG = "File"
