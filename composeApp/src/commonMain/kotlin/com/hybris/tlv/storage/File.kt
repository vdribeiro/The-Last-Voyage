package com.hybris.tlv.storage

/**
 * Save file to device storage. Return true if success.
 */
expect fun saveFile(fileName: String, content: String): Boolean

/**
 * Load file from device storage. Return null if not found.
 */
expect fun loadFile(fileName: String): String?
