package com.hybris.tlv.storage

import java.io.File

private val appDataDir: File by lazy {
    val home = System.getProperty("user.home")
    File(home, ".TheLastVoyage").apply { if (!exists()) mkdirs() }
}

actual fun saveFile(fileName: String, content: String): Boolean = runCatching {
    val file = File(appDataDir, fileName)
    file.writeText(text = content)
    true
}.getOrDefault(defaultValue = false)

actual fun loadFile(fileName: String): String? = runCatching {
    val file = File(appDataDir, fileName)
    file.readText()
}.getOrNull()
