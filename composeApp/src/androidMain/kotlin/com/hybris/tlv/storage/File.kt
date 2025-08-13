package com.hybris.tlv.storage

import com.hybris.tlv.applicationContext
import java.io.File
import java.io.FileOutputStream

actual fun saveFile(fileName: String, content: String): Boolean = runCatching {
    val file = File(applicationContext.filesDir, fileName)
    FileOutputStream(file).use { it.write(content.toByteArray()) }
    true
}.getOrDefault(defaultValue = false)
