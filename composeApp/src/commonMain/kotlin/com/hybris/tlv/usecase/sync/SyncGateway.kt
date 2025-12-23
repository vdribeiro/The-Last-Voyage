package com.hybris.tlv.usecase.sync

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import com.hybris.tlv.TLV.flag
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.reset
import com.hybris.tlv.database.isEmpty
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.platform.Property
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.credit.CreditUseCases
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.ArchiveUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.sync.model.DataSource
import com.hybris.tlv.usecase.sync.model.SyncResult
import com.hybris.tlv.usecase.translation.TranslationCache
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
        config.reset()
        database.reset()
        TranslationCache.reset()
    }

    override suspend fun isEmpty(): Boolean =
        database.isEmpty()

    override suspend fun sync(progress: (Float) -> Unit): SyncResult = withContext(context = Dispatcher.Default) {
        if (flag.reset) reset()
        config.setup()

        val remoteVersion = config.remoteConfigs.value.appVersion
        val localVersion = config.localConfigs.value.appVersion
        Telemetry.info(tag = TAG, message = "App version: remote version: $remoteVersion, local version: $localVersion")
        config.setConfigs { it.copy(appVersion = remoteVersion) }
        val result = if (localVersion == 0L || Property.APP_VERSION_NUMBER == remoteVersion) syncAll(progress = progress) else SyncResult(
            archive = DataSource.NONE,
            translations = DataSource.NONE,
            catastrophes = DataSource.NONE,
            engines = DataSource.NONE,
            stellarHosts = DataSource.NONE,
            planets = DataSource.NONE,
            events = DataSource.NONE,
            achievements = DataSource.NONE,
            credits = DataSource.NONE
        )
        config.saveConfigs()
        translationUseCases.refreshCache()

        Telemetry.info(tag = TAG, message = "Preferences\n${config.preferences.value}")
        Telemetry.info(tag = TAG, message = "Local Configs\n${config.localConfigs.value}")
        Telemetry.info(tag = TAG, message = "Remote Configs\n${config.remoteConfigs.value}")

        return@withContext result
    }

    private suspend fun syncAll(progress: (Float) -> Unit): SyncResult = supervisorScope {
        val mutex = Mutex()
        var completedTasks = 0f
        val totalTasks = 9f
        fun asyncWithProgress(task: suspend () -> DataSource): Deferred<DataSource> = async {
            task().also {
                mutex.withLock {
                    completedTasks++
                    progress(completedTasks / totalTasks)
                }
            }
        }

        val archiveDeferred = asyncWithProgress { getArchive() }
        val translationsDeferred = asyncWithProgress { syncTranslations() }
        val catastrophesDeferred = asyncWithProgress { syncCatastrophes() }
        val enginesDeferred = asyncWithProgress { syncEngines() }
        val stellarHostsDeferred = asyncWithProgress { syncStellarHosts() }
        val planetsDeferred = asyncWithProgress { syncPlanets() }
        val eventsDeferred = asyncWithProgress { syncEvents() }
        val achievementsDeferred = asyncWithProgress { syncAchievements() }
        val creditsDeferred = asyncWithProgress { syncCredits() }

        SyncResult(
            archive = archiveDeferred.tryAwait(task = "archive"),
            translations = translationsDeferred.tryAwait(task = "translation"),
            catastrophes = catastrophesDeferred.tryAwait(task = "catastrophe"),
            engines = enginesDeferred.tryAwait(task = "engine"),
            stellarHosts = stellarHostsDeferred.tryAwait(task = "stellarHost"),
            planets = planetsDeferred.tryAwait(task = "planet"),
            events = eventsDeferred.tryAwait(task = "event"),
            achievements = achievementsDeferred.tryAwait(task = "achievement"),
            credits = creditsDeferred.tryAwait(task = "credit")
        )
    }

    private suspend fun Deferred<DataSource>.tryAwait(task: String): DataSource =
        runCatching { await() }.onFailure {
            Telemetry.error(tag = TAG, message = "Sync task $task failed.", throwable = it)
        }.getOrDefault(defaultValue = DataSource.NONE)

    private suspend fun getArchive(): DataSource =
        if (flag.archive && archiveUseCases.getArchive()) DataSource.REMOTE else DataSource.NONE

    private suspend fun syncTranslations(): DataSource {
        val remoteVersion = config.remoteConfigs.value.translationsVersion
        val localVersion = config.localConfigs.value.translationsVersion
        Telemetry.info(tag = TAG, message = "Syncing translations: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (translationUseCases.syncTranslations()) {
                config.setConfigs { it.copy(translationsVersion = remoteVersion) }
                return DataSource.REMOTE
            }
        }
        if (translationUseCases.prepopulateTranslations()) return DataSource.LOCAL
        return DataSource.NONE
    }

    private suspend fun syncCatastrophes(): DataSource {
        val remoteVersion = config.remoteConfigs.value.catastrophesVersion
        val localVersion = config.localConfigs.value.catastrophesVersion
        Telemetry.info(tag = TAG, message = "Syncing catastrophes: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (catastropheUseCases.syncCatastrophes()) {
                config.setConfigs { it.copy(catastrophesVersion = remoteVersion) }
                return DataSource.REMOTE
            }
        }
        if (catastropheUseCases.prepopulateCatastrophes()) return DataSource.LOCAL
        return DataSource.NONE
    }

    private suspend fun syncEngines(): DataSource {
        val remoteVersion = config.remoteConfigs.value.enginesVersion
        val localVersion = config.localConfigs.value.enginesVersion
        Telemetry.info(tag = TAG, message = "Syncing engines: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (shipUseCases.syncEngines()) {
                config.setConfigs { it.copy(enginesVersion = remoteVersion) }
                return DataSource.REMOTE
            }
        }
        if (shipUseCases.prepopulateEngines()) return DataSource.LOCAL
        return DataSource.NONE
    }

    private suspend fun syncStellarHosts(): DataSource {
        val remoteVersion = config.remoteConfigs.value.stellarHostsVersion
        val localVersion = config.localConfigs.value.stellarHostsVersion
        Telemetry.info(tag = TAG, message = "Syncing stellar hosts: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (spaceUseCases.syncStellarHosts()) {
                config.setConfigs { it.copy(stellarHostsVersion = remoteVersion) }
                return DataSource.REMOTE
            }
        }
        if (spaceUseCases.prepopulateStellarHosts()) return DataSource.LOCAL
        return DataSource.NONE
    }

    private suspend fun syncPlanets(): DataSource {
        val remoteVersion = config.remoteConfigs.value.planetsVersion
        val localVersion = config.localConfigs.value.planetsVersion
        Telemetry.info(tag = TAG, message = "Syncing planets: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (spaceUseCases.syncPlanets()) {
                config.setConfigs { it.copy(planetsVersion = remoteVersion) }
                return DataSource.REMOTE
            }
        }
        if (spaceUseCases.prepopulatePlanets()) return DataSource.LOCAL
        return DataSource.NONE
    }

    private suspend fun syncEvents(): DataSource {
        val remoteVersion = config.remoteConfigs.value.eventsVersion
        val localVersion = config.localConfigs.value.eventsVersion
        Telemetry.info(tag = TAG, message = "Syncing events: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (eventUseCases.syncEvents()) {
                config.setConfigs { it.copy(eventsVersion = remoteVersion) }
                return DataSource.REMOTE
            }
        }
        if (eventUseCases.prepopulateEvents()) return DataSource.LOCAL
        return DataSource.NONE
    }

    private suspend fun syncAchievements(): DataSource {
        val remoteVersion = config.remoteConfigs.value.achievementsVersion
        val localVersion = config.localConfigs.value.achievementsVersion
        Telemetry.info(tag = TAG, message = "Syncing achievements: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (achievementUseCases.syncAchievements()) {
                config.setConfigs { it.copy(achievementsVersion = remoteVersion) }
                return DataSource.REMOTE
            }
        }
        if (achievementUseCases.prepopulateAchievements()) return DataSource.LOCAL
        return DataSource.NONE
    }

    private suspend fun syncCredits(): DataSource {
        val remoteVersion = config.remoteConfigs.value.creditsVersion
        val localVersion = config.localConfigs.value.creditsVersion
        Telemetry.info(tag = TAG, message = "Syncing credits: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            if (creditUseCases.syncCredits()) {
                config.setConfigs { it.copy(creditsVersion = remoteVersion) }
                return DataSource.REMOTE
            }
        }
        if (creditUseCases.prepopulateCredits()) return DataSource.LOCAL
        return DataSource.NONE
    }

    companion object Companion {
        private const val TAG = "Sync"
    }
}
