package com.hybris.tlv.storage

import java.io.File

actual fun saveFile(fileName: String, content: String): Boolean = runCatching {
    val userHome = System.getProperty("user.home")
    val file = File(userHome, fileName)
    file.writeText(content)
    true
}.getOrDefault(defaultValue = false)
