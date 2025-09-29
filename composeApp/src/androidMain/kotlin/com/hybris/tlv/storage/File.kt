package com.hybris.tlv.storage

import com.hybris.tlv.applicationContext
import java.io.File

private val appDataDir: File by lazy {
    applicationContext.filesDir
}

internal actual fun saveFile(path: String, content: String): Boolean = runCatching {
    val file = File(appDataDir, path)
    file.writeText(text = content)
    true
}.getOrDefault(defaultValue = false)

internal actual fun loadFile(path: String): String? = runCatching {
    val file = File(appDataDir, path)
    file.readText()
}.getOrNull()
