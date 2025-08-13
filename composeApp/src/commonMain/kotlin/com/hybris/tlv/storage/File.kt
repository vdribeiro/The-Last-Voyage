package com.hybris.tlv.storage

/**
 * Save file to device storage.
 */
expect fun saveFile(fileName: String, content: String): Boolean
