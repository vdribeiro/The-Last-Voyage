package com.hybris.tlv

import io.ktor.client.engine.HttpClientEngine
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.data.config.Config
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.data.database.DatabaseFactory
import com.hybris.tlv.data.http.HttpClientFactory
import com.hybris.tlv.domain.usecase.Gateways
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.infrastructure.audio.AudioPlayer
import com.hybris.tlv.infrastructure.audio.createAudioPlayer

/**
 * Dependency index.
 */
internal class Dependency(
    val config: ConfigManager,
    val useCases: UseCases,
    val audioPlayer: AudioPlayer
) {
    companion object {
        fun create(
            sqlDriver: SqlDriver,
            httpEngine: HttpClientEngine?
        ): Dependency {
            val database = DatabaseFactory(driver = sqlDriver).database
            val client = HttpClientFactory(engine = httpEngine).httpClient
            val config = Config(httpClient = client)
            val useCases = Gateways(
                config = config,
                database = database,
                httpClient = client,
            )
            val audioPlayer = createAudioPlayer()
            return Dependency(
                config = config,
                useCases = useCases,
                audioPlayer = audioPlayer
            )
        }
    }
}
