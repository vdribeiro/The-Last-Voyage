package com.hybris.tlv.config

import com.hybris.tlv.serializer.PREFERENCES_JSON
import com.hybris.tlv.serializer.json
import com.hybris.tlv.storage.loadFile
import com.hybris.tlv.storage.saveFile
import kotlinx.serialization.Serializable

@Serializable
internal data class Preferences(
    val showTutorial: Boolean = true,
) {
    companion object {
        fun get(): Preferences = loadFile(fileName = PREFERENCES_JSON)?.let {
            runCatching { json.decodeFromString<Preferences>(string = it) }.getOrNull()
        } ?: Preferences().also { set(preferences = it) }

        fun set(preferences: Preferences): Boolean = runCatching { json.encodeToString(value = preferences) }.getOrNull()?.let {
            saveFile(fileName = PREFERENCES_JSON, content = it)
        } ?: false
    }
}
