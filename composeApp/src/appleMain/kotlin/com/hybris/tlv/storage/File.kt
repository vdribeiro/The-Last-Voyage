@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.hybris.tlv.storage

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.stringByDeletingLastPathComponent
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile
import com.hybris.tlv.telemetry.Telemetry

private val appDataDir: NSString by lazy {
    val paths = NSSearchPathForDirectoriesInDomains(
        directory = NSDocumentDirectory,
        domainMask = NSUserDomainMask,
        expandTilde = true
    )
    paths.first() as NSString
}

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun saveFile(path: String, content: String): Boolean = runCatching {
    val fileManager = NSFileManager.defaultManager
    val fullPath = appDataDir.stringByAppendingPathComponent(str = path)
    val parentDir = (fullPath as NSString).stringByDeletingLastPathComponent()
    if (!fileManager.fileExistsAtPath(path = parentDir)) {
        fileManager.createDirectoryAtPath(
            path = parentDir,
            withIntermediateDirectories = true,
            attributes = null,
            error = null
        )
    }
    (content as NSString).writeToFile(
        path = fullPath,
        atomically = true,
        encoding = NSUTF8StringEncoding,
        error = null
    )
    true
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to save file", throwable = it)
    false
}

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun loadFile(path: String): String? = runCatching {
    val fileManager = NSFileManager.defaultManager
    val fullPath = appDataDir.stringByAppendingPathComponent(str = path)
    if (fileManager.fileExistsAtPath(path = fullPath)) {
        NSString.stringWithContentsOfFile(
            path = fullPath,
            encoding = NSUTF8StringEncoding,
            error = null
        )
    } else null
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to load file", throwable = it)
    null
}

private const val TAG = "File"
