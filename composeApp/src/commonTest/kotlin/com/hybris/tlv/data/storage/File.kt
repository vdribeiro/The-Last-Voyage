@file:Suppress("RedundantSuspendModifier", "unused", "MayBeConstant")

package com.hybris.tlv.data.storage

private val map = mutableMapOf<String, String>()

internal val appDataPath: String = ""

internal suspend fun saveFile(path: String, content: String): Boolean {
    map[path] = content
    return true
}

internal suspend fun loadFile(path: String): String? = map[path]

internal suspend fun deleteFile(path: String): Boolean = map.remove(key = path) != null
