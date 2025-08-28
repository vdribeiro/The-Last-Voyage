package com.hybris.tlv.storage

import com.hybris.tlv.applicationContext
import java.io.File

private val appDataDir: File by lazy {
    applicationContext.filesDir
}

actual fun saveFile(fileName: String, content: String): Boolean = runCatching {
    val file = File(appDataDir, fileName)
    file.writeText(text = content)
    true
}.getOrDefault(defaultValue = false)

actual fun loadFile(fileName: String): String? = runCatching {
    val file = File(appDataDir, fileName)
    file.readText()
}.getOrDefault(defaultValue = null)
