package com.hybris.tlv

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.data.config.Config
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.data.database.DatabaseFactory
import com.hybris.tlv.data.database.NoOpSqlDriver
import com.hybris.tlv.data.http.HttpClientFactory
import com.hybris.tlv.data.http.NoOpHttpEngine
import com.hybris.tlv.domain.usecase.Gateways
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.ui.screen.StoreFactory
import database.AppDatabase

/**
 * A comprehensive container that manages the dependency index.
 * This class orchestrates the instantiation of the entire application stack. It follows a hierarchical initialization pattern:
 * 1. **Engines:** Platform drivers like [SqlDriver] and [HttpClientEngine] are received.
 * 2. **Infrastructure:** Wrappers like [AppDatabase] and [HttpClient] are built from engines.
 * 3. **Management:** Managers like [ConfigManager] are initialized.
 * 4. **Logic:** [UseCases] provide the bridge between data and domain logic.
 * 5. **UI State:** The [StoreFactory] is created to bridge the domain and UI layers.
 *
 * ### Architectural Pattern:
 * This class implements **Manual Dependency Injection** and allows the swap of platform-specific drivers while maintaining a consistent, type-safe graph for the rest of the application.
 *
 * @property sqlDriver The platform-specific SQL driver.
 * @property database The generated SQL database instance.
 * @property httpEngine The platform-specific Network engine.
 * @property httpClient The configured HTTP client used for all network requests.
 * @property audioPlayer The coordinator for music and sound effects.
 * @property config The manager for local and remote configuration settings.
 * @property useCases The entry point for all domain-level business logic.
 * @property storeFactory The factory used to create Stores for UI state management.
 */
internal class Dependency(
    val sqlDriver: SqlDriver = NoOpSqlDriver,
    val database: AppDatabase = DatabaseFactory(driver = sqlDriver).database,
    val httpEngine: HttpClientEngine = NoOpHttpEngine,
    val httpClient: HttpClient = HttpClientFactory(engine = httpEngine).httpClient,
    val audioPlayer: AudioPlayer = AudioPlayer(),
    val config: ConfigManager = Config(httpClient = httpClient),
    val useCases: UseCases = Gateways(
        config = config,
        database = database,
        httpClient = httpClient,
    ),
    val storeFactory: StoreFactory = StoreFactory(
        config = config,
        useCases = useCases
    )
)