package com.hybris.tlv.storage

/**
 * Save file to device storage. Return true on success.
 */
internal expect fun saveFile(fileName: String, content: String): Boolean

/**
 * Load file from device storage. Return null if not found.
 */
internal expect fun loadFile(fileName: String): String?
