package com.hybris.tlv.storage

import com.hybris.tlv.telemetry.Telemetry
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

private val appDataDir: NSString by lazy {
    val paths = NSSearchPathForDirectoriesInDomains(directory = NSDocumentDirectory, domainMask = NSUserDomainMask, expandTilde = true)
    paths.first() as NSString
}

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun saveFile(path: String, content: String): Boolean = runCatching {
    val path = appDataDir.stringByAppendingPathComponent(str = path)
    (content as NSString).writeToFile(path, atomically = true, encoding = NSUTF8StringEncoding, error = null)
    true
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to save file", throwable = it)
    false
}

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun loadFile(path: String): String? = runCatching {
    val path = appDataDir.stringByAppendingPathComponent(str = path)
    NSString.stringWithContentsOfFile(path, encoding = NSUTF8StringEncoding, error = null).orEmpty()
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to load file", throwable = it)
    null
}

private const val TAG = "File"
