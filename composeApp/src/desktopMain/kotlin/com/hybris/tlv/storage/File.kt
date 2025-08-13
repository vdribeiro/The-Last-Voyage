package com.hybris.tlv.storage

import java.io.File

actual fun saveFile(fileName: String, content: String): Boolean = runCatching {
    val userHome = System.getProperty("user.home")
    val downloadsDir = File(userHome, "Downloads")
    if (!downloadsDir.exists()) downloadsDir.mkdir()
    val file = File(downloadsDir, fileName)
    file.writeText(text = content, charset = Charsets.UTF_8)
    true
}.getOrDefault(defaultValue = false)
