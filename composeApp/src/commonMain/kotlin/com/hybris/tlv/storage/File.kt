package com.hybris.tlv.storage

/**
 * Save file to device storage. Return true on success.
 */
internal expect fun saveFile(path: String, content: String): Boolean

/**
 * Load file from device storage. Return null if not found.
 */
internal expect fun loadFile(path: String): String?
