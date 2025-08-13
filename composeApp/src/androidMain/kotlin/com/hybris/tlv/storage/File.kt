package com.hybris.tlv.storage

import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.hybris.tlv.applicationContext
import java.io.File

actual fun saveFile(fileName: String, content: String): Boolean = runCatching {
    val fileBytes = content.toByteArray(Charsets.UTF_8)

    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val contentResolver = applicationContext.contentResolver
            val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues) ?: return false
            runCatching {
                contentResolver.openOutputStream(uri)?.use { it.write(fileBytes) }
            }.getOrElse {
                contentResolver.delete(uri, null, null)
            }
        }

        else -> {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
            runCatching {
                file.writeBytes(array = fileBytes)
            }.getOrElse {
                file.delete()
            }
        }
    }
    true
}.getOrDefault(defaultValue = false)
