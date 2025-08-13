package com.hybris.tlv.storage

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToURL

@OptIn(ExperimentalForeignApi::class)
actual fun saveFile(fileName: String, content: String): Boolean = runCatching {
    val fileManager = NSFileManager.defaultManager
    val documentsURL = fileManager.URLsForDirectory(
        directory = NSDocumentDirectory,
        inDomains = NSUserDomainMask
    ).firstOrNull() as? NSURL ?: return false
    val downloadsURL = documentsURL.URLByAppendingPathComponent(pathComponent = "Downloads") ?: return false
    fileManager.createDirectoryAtURL(url = downloadsURL, withIntermediateDirectories = true, attributes = null, error = null)
    val fileURL = downloadsURL.URLByAppendingPathComponent(pathComponent = fileName) ?: return false
    val bytes = content.encodeToByteArray()
    val data = bytes.usePinned { NSData.dataWithBytes(bytes = it.addressOf(index = 0), length = bytes.size.toULong()) }
    data.writeToURL(url = fileURL, atomically = true)
    true
}.getOrDefault(defaultValue = false)
