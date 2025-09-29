package com.hybris.tlv.storage

/**
 * Save file to device storage. Return true on success, false otherwise.
 */
internal expect suspend fun saveFile(path: String, content: String): Boolean

/**
 * Load file from device storage. Return null if not found or an error occurred.
 */
internal expect suspend fun loadFile(path: String): String?
