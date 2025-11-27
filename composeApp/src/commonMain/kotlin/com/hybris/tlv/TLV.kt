package com.hybris.tlv

import kotlinx.coroutines.withContext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.dependency.Dependency
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.serializer.CONFIGS_JSON
import com.hybris.tlv.serializer.PREFERENCES_JSON
import com.hybris.tlv.storage.deleteFile

internal object TLV {

    private val dependency: Dependency = Dependency()

    suspend fun reset() = withContext(context = Dispatcher.IO) {
        deleteFile(path = CONFIGS_JSON)
        deleteFile(path = PREFERENCES_JSON)
        dependency.sqlDriver.clearDatabase()
    }

    /**
     * App entry point.
     */
    @Composable
    fun App(modifier: Modifier = Modifier) {
        App(
            modifier = modifier,
            config = dependency.config,
            useCases = dependency.useCases,
            audioPlayer = dependency.audioPlayer,
        )
    }

    /**
     * Reset local data.
     */
    const val RESET = true
    /**
     * Enable or disable HTTP client.
     */
    const val HTTP = false
    /**
     * Enable or disable getting exoplanet data from the NASA archive (only works if HTTP is enabled).
     */
    const val ARCHIVE = false
    /**
     * Enable or disable ambient music.
     */
    const val MUSIC = false
}
