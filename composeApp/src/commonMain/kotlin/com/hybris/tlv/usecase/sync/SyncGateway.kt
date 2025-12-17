package com.hybris.tlv.usecase.sync

import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import com.hybris.tlv.TLV.ARCHIVE
import com.hybris.tlv.TLV.RESET
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.platform.Property
import com.hybris.tlv.serializer.CONFIGS_JSON
import com.hybris.tlv.serializer.PREFERENCES_JSON
import com.hybris.tlv.storage.deleteFile
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.credit.CreditUseCases
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.ArchiveUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.translation.TranslationUseCases
import database.AppDatabase

internal class SyncGateway(
    private val config: ConfigManager,
    private val database: AppDatabase,
    private val translationUseCases: TranslationUseCases,
    private val archiveUseCases: ArchiveUseCases,
    private val catastropheUseCases: CatastropheUseCases,
    private val shipUseCases: ShipUseCases,
    private val spaceUseCases: SpaceUseCases,
    private val eventUseCases: EventUseCases,
    private val achievementUseCases: AchievementUseCases,
    private val creditUseCases: CreditUseCases
): SyncUseCases {

    override suspend fun reset() = withContext(context = Dispatcher.IO) {
        deleteFile(path = CONFIGS_JSON)
        deleteFile(path = PREFERENCES_JSON)
        database.clearDatabase()
    }

    override suspend fun sync(progress: (Float) -> Unit) = withContext(context = Dispatcher.Default) {
        if (RESET) reset()
        config.setup()

        val remoteVersion = config.remoteConfigs.value.appVersion
        val localVersion = config.localConfigs.value.appVersion
        Telemetry.info(tag = TAG, message = "App version: remote version: $remoteVersion, local version: $localVersion")
        config.setConfigs { it.copy(appVersion = remoteVersion) }
        if (localVersion == 0L || Property.APP_VERSION_NUMBER == remoteVersion) syncAll(progress = progress)
        config.saveConfigs()
        translationUseCases.refreshCache()

        Telemetry.info(tag = TAG, message = "Preferences\n${config.preferences.value}")
        Telemetry.info(tag = TAG, message = "Local Configs\n${config.localConfigs.value}")
        Telemetry.info(tag = TAG, message = "Remote Configs\n${config.remoteConfigs.value}")
    }

    private suspend fun syncAll(progress: (Float) -> Unit) = supervisorScope {
        val tasks = listOf(
            suspend { translationUseCases.syncTranslations() },
            suspend { if (ARCHIVE) archiveUseCases.getArchive() },
            suspend { catastropheUseCases.syncCatastrophes() },
            suspend { shipUseCases.syncEngines() },
            suspend { spaceUseCases.syncStellarHosts() },
            suspend { spaceUseCases.syncPlanets() },
            suspend { eventUseCases.syncEvents() },
            suspend { achievementUseCases.syncAchievements() },
            suspend { creditUseCases.syncCredits() }
        )
        val total = tasks.size.toFloat()
        tasks.map { task -> async { task() } }.forEachIndexed { index, job ->
            runCatching {
                job.await()
            }.onFailure { Telemetry.error(tag = TAG, message = "Sync task failed.", throwable = it) }.getOrNull()
            progress((index + 1).toFloat() / total)
        }
    }

    companion object Companion {
        private const val TAG = "Sync"
    }
}
