package com.hybris.tlv.usecase.sync

import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import com.hybris.tlv.TLV.flag
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
    private val archiveUseCases: ArchiveUseCases,
    private val translationUseCases: TranslationUseCases,
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
        if (flag.reset) reset()
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
            suspend { if (flag.archive) archiveUseCases.getArchive() },
            suspend { syncTranslations() },
            suspend { syncCatastrophes() },
            suspend { syncEngines() },
            suspend { syncStellarHosts() },
            suspend { syncPlanets() },
            suspend { syncEvents() },
            suspend { syncAchievements() },
            suspend { syncCredits() }
        )
        val total = tasks.size.toFloat()
        tasks.map { task -> async { task() } }.forEachIndexed { index, job ->
            runCatching {
                job.await()
            }.onFailure { Telemetry.error(tag = TAG, message = "Sync task failed.", throwable = it) }.getOrNull()
            progress((index + 1).toFloat() / total)
        }
    }

    private suspend fun syncTranslations() {
        val remoteVersion = config.remoteConfigs.value.translationsVersion
        val localVersion = config.localConfigs.value.translationsVersion
        Telemetry.info(tag = TAG, message = "Syncing translations: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (translationUseCases.syncTranslations()) {
                config.setConfigs { it.copy(translationsVersion = remoteVersion) }
                return
            }
        }
        translationUseCases.prepopulateTranslations()
    }

    private suspend fun syncCatastrophes() {
        val remoteVersion = config.remoteConfigs.value.catastrophesVersion
        val localVersion = config.localConfigs.value.catastrophesVersion
        Telemetry.info(tag = TAG, message = "Syncing catastrophes: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (catastropheUseCases.syncCatastrophes()) {
                config.setConfigs { it.copy(catastrophesVersion = remoteVersion) }
                return
            }
        }
        catastropheUseCases.prepopulateCatastrophes()
    }

    private suspend fun syncEngines() {
        val remoteVersion = config.remoteConfigs.value.enginesVersion
        val localVersion = config.localConfigs.value.enginesVersion
        Telemetry.info(tag = TAG, message = "Syncing engines: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (shipUseCases.syncEngines()) {
                config.setConfigs { it.copy(enginesVersion = remoteVersion) }
                return
            }
        }
        shipUseCases.prepopulateEngines()
    }

    private suspend fun syncStellarHosts() {
        val remoteVersion = config.remoteConfigs.value.stellarHostsVersion
        val localVersion = config.localConfigs.value.stellarHostsVersion
        Telemetry.info(tag = TAG, message = "Syncing stellar hosts: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (spaceUseCases.syncStellarHosts()) {
                config.setConfigs { it.copy(stellarHostsVersion = remoteVersion) }
                return
            }
        }
        spaceUseCases.prepopulateStellarHosts()
    }

    private suspend fun syncPlanets() {
        val remoteVersion = config.remoteConfigs.value.planetsVersion
        val localVersion = config.localConfigs.value.planetsVersion
        Telemetry.info(tag = TAG, message = "Syncing planets: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (spaceUseCases.syncPlanets()) {
                config.setConfigs { it.copy(planetsVersion = remoteVersion) }
                return
            }
        }
        spaceUseCases.prepopulatePlanets()
    }

    private suspend fun syncEvents() {
        val remoteVersion = config.remoteConfigs.value.eventsVersion
        val localVersion = config.localConfigs.value.eventsVersion
        Telemetry.info(tag = TAG, message = "Syncing events: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (eventUseCases.syncEvents()) {
                config.setConfigs { it.copy(eventsVersion = remoteVersion) }
                return
            }
        }
        eventUseCases.prepopulateEvents()
    }

    private suspend fun syncAchievements() {
        val remoteVersion = config.remoteConfigs.value.achievementsVersion
        val localVersion = config.localConfigs.value.achievementsVersion
        Telemetry.info(tag = TAG, message = "Syncing achievements: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (achievementUseCases.syncAchievements()) {
                config.setConfigs { it.copy(achievementsVersion = remoteVersion) }
                return
            }
        }
        achievementUseCases.prepopulateAchievements()
    }

    private suspend fun syncCredits() {
        val remoteVersion = config.remoteConfigs.value.creditsVersion
        val localVersion = config.localConfigs.value.creditsVersion
        Telemetry.info(tag = TAG, message = "Syncing credits: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (creditUseCases.syncCredits()) {
                config.setConfigs { it.copy(creditsVersion = remoteVersion) }
                return
            }
        }
        creditUseCases.prepopulateCredits()
    }

    companion object Companion {
        private const val TAG = "Sync"
    }
}
