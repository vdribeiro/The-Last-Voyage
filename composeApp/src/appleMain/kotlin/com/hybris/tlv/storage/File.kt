@file:ShadowedInTesting

package com.hybris.tlv.storage

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.withContext
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

@OptIn(ExperimentalForeignApi::class)
internal actual val appDataPath: String by lazy {
    (NSSearchPathForDirectoriesInDomains(
        directory = NSDocumentDirectory,
        domainMask = NSUserDomainMask,
        expandTilde = true
    ).first() as NSString).also {
        val fileManager = NSFileManager.defaultManager
        if (!fileManager.fileExistsAtPath(path = it.toString())) {
            fileManager.createDirectoryAtPath(
                path = it.toString(),
                withIntermediateDirectories = true,
                attributes = null,
                error = null
            )
        }
    }.toString()
}

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun saveFile(path: String, content: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        val file = appDataPath.toNSString().stringByAppendingPathComponent(str = path)
        content.toNSString().writeToFile(
            path = file,
            atomically = true,
            encoding = NSUTF8StringEncoding,
            error = null
        )
        true
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to save file $path", throwable = it) }.getOrDefault(defaultValue = false)
}

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun loadFile(path: String): String? = withContext(context = Dispatcher.IO) {
    runCatching {
        val fullPath = appDataPath.toNSString().stringByAppendingPathComponent(str = path)
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

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun deleteFile(path: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        val fullPath = appDataPath.toNSString().stringByAppendingPathComponent(str = path)
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
