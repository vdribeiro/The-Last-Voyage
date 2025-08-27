package com.hybris.tlv.storage

/**
 * Save file to device storage.
 */
expect fun saveFile(fileName: String, content: String): Boolean

/**
 * Load file from device storage.
 */
expect fun loadFile(fileName: String): String
