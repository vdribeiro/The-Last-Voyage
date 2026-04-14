package com.hybris.tlv.data.storage

/**
 * The platform-specific absolute path to the application's internal data directory.
 */
internal expect val appDataPath: String

/**
 * Persists a [String] to the device's local file system at the specified [path].
 *
 * This function handles the raw byte-writing logic for each platform. It is marked as `suspend` to allow for non-blocking I/O operations,
 * which is essential when dealing with larger files or encrypted partitions.
 *
 * @param path The absolute file path where the data should be written.
 * @param content The string data to be persisted.
 * @return `true` if the write operation completed successfully `false` if an I/O error occurred or permissions were denied.
 */
internal expect suspend fun saveFile(path: String, content: String): Boolean

/**
 * Retrieves the content of a file from device storage as a [String].
 *
 * @param path The path to the file to be loaded.
 * @return The file content as a string, or `null` if the file does not exist or cannot be read.
 */
internal expect suspend fun loadFile(path: String): String?

/**
 * Permanently removes a file from the device storage.
 *
 * @param path The path to the file to be deleted.
 * @return `true` if the file was successfully deleted or was not found, `false` if the deletion failed due to a system error.
 */
internal expect suspend fun deleteFile(path: String): Boolean
