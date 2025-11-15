@file:Suppress("CAST_NEVER_SUCCEEDS")
@file:OptIn(ExperimentalForeignApi::class)

package com.hybris.tlv.storage

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile
import com.hybris.tlv.telemetry.Telemetry

private val appDataDir: NSString by lazy {
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
    }
}

internal actual suspend fun saveFile(path: String, content: String): Boolean = runCatching {
    val file = appDataDir.stringByAppendingPathComponent(str = path)
    (content as NSString).writeToFile(
        path = file,
        atomically = true,
        encoding = NSUTF8StringEncoding,
        error = null
    )
    true
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to save file", throwable = it) }.getOrDefault(defaultValue = false)

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun loadFile(path: String): String? = runCatching {
    val fullPath = appDataDir.stringByAppendingPathComponent(str = path)
    if (NSFileManager.defaultManager.fileExistsAtPath(path = fullPath)) {
        NSString.stringWithContentsOfFile(
            path = fullPath,
            encoding = NSUTF8StringEncoding,
            error = null
        )
    } else null
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to load file", throwable = it) }.getOrNull()

private const val TAG = "File"
