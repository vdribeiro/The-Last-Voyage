package com.hybris.tlv.storage

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
internal actual fun saveFile(fileName: String, content: String): Boolean = runCatching {
    val path = appDataDir.stringByAppendingPathComponent(str = fileName)
    (content as NSString).writeToFile(path, atomically = true, encoding = NSUTF8StringEncoding, error = null)
    true
}.getOrDefault(defaultValue = false)

@OptIn(ExperimentalForeignApi::class)
internal actual fun loadFile(fileName: String): String? = runCatching {
    val path = appDataDir.stringByAppendingPathComponent(str = fileName)
    NSString.stringWithContentsOfFile(path, encoding = NSUTF8StringEncoding, error = null).orEmpty()
}.getOrNull()