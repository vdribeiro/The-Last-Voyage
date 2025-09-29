package com.hybris.tlv.storage

import com.hybris.tlv.applicationContext
import java.io.File

private val appDataDir: File by lazy {
    applicationContext.filesDir
}

internal actual fun saveFile(fileName: String, content: String): Boolean = runCatching {
    val file = File(appDataDir, fileName)
    file.writeText(text = content)
    true
}.getOrDefault(defaultValue = false)

internal actual fun loadFile(fileName: String): String? = runCatching {
    val file = File(appDataDir, fileName)
    file.readText()
}.getOrNull()
