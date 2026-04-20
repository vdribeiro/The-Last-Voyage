package com.hybris.tlv.domain.usecase.sync

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import com.hybris.tlv.App
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.data.database.reset
import com.hybris.tlv.domain.usecase.achievement.AchievementUseCases
import com.hybris.tlv.domain.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.domain.usecase.credit.CreditUseCases
import com.hybris.tlv.domain.usecase.event.EventUseCases
import com.hybris.tlv.domain.usecase.ship.ShipUseCases
import com.hybris.tlv.domain.usecase.space.SpaceUseCases
import com.hybris.tlv.domain.usecase.sync.model.DataSource
import com.hybris.tlv.domain.usecase.sync.model.SyncResult
import com.hybris.tlv.data.translation.TranslationCache
import com.hybris.tlv.domain.usecase.translation.TranslationUseCases
import database.AppDatabase

internal class SyncGateway(
    private val config: ConfigManager,
    private val database: AppDatabase,
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
    }

    override suspend fun sync(
        reset: Boolean,
        progress: (Float) -> Unit
    ): SyncResult = withContext(context = Dispatcher.IO) {
        if (reset) reset()
        config.setup()

        val remoteVersion = config.remoteConfigs.appVersion
        val localVersion = config.localConfigs.appVersion
        Telemetry.info(tag = TAG, message = "App version: remote version: $remoteVersion, local version: $localVersion")
        config.setConfigs { it.copy(appVersion = remoteVersion) }
        val result = syncAll(
            latestVersion = App.VERSION_NUMBER == remoteVersion,
            progress = progress
        )
        config.saveConfigs()

        val translations = translationUseCases.getTranslations()
        TranslationCache.set(translations = translations)

        Telemetry.info(tag = TAG, message = "Preferences\n${config.preferences}")
        Telemetry.info(tag = TAG, message = "Local Configs\n${config.localConfigs}")
        Telemetry.info(tag = TAG, message = "Remote Configs\n${config.remoteConfigs}")

        progress(1f)
        result
    }

    private suspend fun syncAll(
        latestVersion: Boolean,
        progress: (Float) -> Unit
    ): SyncResult = withContext(context = Dispatcher.IO) {
        supervisorScope {
            val mutex = Mutex()
            var completedTasks = 0f
            val totalTasks = 8f
            fun asyncWithProgress(task: suspend () -> DataSource): Deferred<DataSource> = async {
                task().also {
                    mutex.withLock {
                        completedTasks++
                        progress(completedTasks / totalTasks)
                    }
                }
            }
            SyncResult(
                translations = asyncWithProgress { syncTranslations(latestVersion = latestVersion) }.tryAwait(task = "translations"),
                catastrophes = asyncWithProgress { syncCatastrophes(latestVersion = latestVersion) }.tryAwait(task = "catastrophes"),
                engines = asyncWithProgress { syncEngines(latestVersion = latestVersion) }.tryAwait(task = "engines"),
                stellarHosts = asyncWithProgress { syncStellarHosts(latestVersion = latestVersion) }.tryAwait(task = "stellarHosts"),
                planets = asyncWithProgress { syncPlanets(latestVersion = latestVersion) }.tryAwait(task = "planets"),
                events = asyncWithProgress { syncEvents(latestVersion = latestVersion) }.tryAwait(task = "events"),
                achievements = asyncWithProgress { syncAchievements(latestVersion = latestVersion) }.tryAwait(task = "achievements"),
                credits = asyncWithProgress { syncCredits(latestVersion = latestVersion) }.tryAwait(task = "credits")
            )
        }
    }

    private suspend fun Deferred<DataSource>.tryAwait(task: String): DataSource = withContext(context = Dispatcher.IO) {
        runCatching { await() }.onFailure {
            Telemetry.error(tag = TAG, message = "Sync task $task failed.", throwable = it)
        }.getOrDefault(defaultValue = DataSource.NONE)
    }

    private suspend fun syncTranslations(latestVersion: Boolean): DataSource = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.translationsVersion
        val localVersion = config.localConfigs.translationsVersion
        Telemetry.info(tag = TAG, message = "Syncing translations: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion && latestVersion) {
            if (translationUseCases.syncTranslations()) {
                config.setConfigs { it.copy(translationsVersion = remoteVersion) }
                return@withContext DataSource.REMOTE
            }
        }
        if (translationUseCases.prepopulateTranslations()) return@withContext DataSource.LOCAL
        return@withContext DataSource.NONE
    }

    private suspend fun syncCatastrophes(latestVersion: Boolean): DataSource = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.catastrophesVersion
        val localVersion = config.localConfigs.catastrophesVersion
        Telemetry.info(tag = TAG, message = "Syncing catastrophes: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion && latestVersion) {
            if (catastropheUseCases.syncCatastrophes()) {
                config.setConfigs { it.copy(catastrophesVersion = remoteVersion) }
                return@withContext DataSource.REMOTE
            }
        }
        if (catastropheUseCases.prepopulateCatastrophes()) return@withContext DataSource.LOCAL
        return@withContext DataSource.NONE
    }

    private suspend fun syncEngines(latestVersion: Boolean): DataSource = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.enginesVersion
        val localVersion = config.localConfigs.enginesVersion
        Telemetry.info(tag = TAG, message = "Syncing engines: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion && latestVersion) {
            if (shipUseCases.syncEngines()) {
                config.setConfigs { it.copy(enginesVersion = remoteVersion) }
                return@withContext DataSource.REMOTE
            }
        }
        if (shipUseCases.prepopulateEngines()) return@withContext DataSource.LOCAL
        return@withContext DataSource.NONE
    }

    private suspend fun syncStellarHosts(latestVersion: Boolean): DataSource = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.stellarHostsVersion
        val localVersion = config.localConfigs.stellarHostsVersion
        Telemetry.info(tag = TAG, message = "Syncing stellar hosts: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion && latestVersion) {
            if (spaceUseCases.syncStellarHosts()) {
                config.setConfigs { it.copy(stellarHostsVersion = remoteVersion) }
                return@withContext DataSource.REMOTE
            }
        }
        if (spaceUseCases.prepopulateStellarHosts()) return@withContext DataSource.LOCAL
        return@withContext DataSource.NONE
    }

    private suspend fun syncPlanets(latestVersion: Boolean): DataSource = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.planetsVersion
        val localVersion = config.localConfigs.planetsVersion
        Telemetry.info(tag = TAG, message = "Syncing planets: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion && latestVersion) {
            if (spaceUseCases.syncPlanets()) {
                config.setConfigs { it.copy(planetsVersion = remoteVersion) }
                return@withContext DataSource.REMOTE
            }
        }
        if (spaceUseCases.prepopulatePlanets()) return@withContext DataSource.LOCAL
        return@withContext DataSource.NONE
    }

    private suspend fun syncEvents(latestVersion: Boolean): DataSource = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.eventsVersion
        val localVersion = config.localConfigs.eventsVersion
        Telemetry.info(tag = TAG, message = "Syncing events: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion && latestVersion) {
            if (eventUseCases.syncEvents()) {
                config.setConfigs { it.copy(eventsVersion = remoteVersion) }
                return@withContext DataSource.REMOTE
            }
        }
        if (eventUseCases.prepopulateEvents()) return@withContext DataSource.LOCAL
        return@withContext DataSource.NONE
    }

    private suspend fun syncAchievements(latestVersion: Boolean): DataSource = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.achievementsVersion
        val localVersion = config.localConfigs.achievementsVersion
        Telemetry.info(tag = TAG, message = "Syncing achievements: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion && latestVersion) {
            if (achievementUseCases.syncAchievements()) {
                config.setConfigs { it.copy(achievementsVersion = remoteVersion) }
                return@withContext DataSource.REMOTE
            }
        }
        if (achievementUseCases.prepopulateAchievements()) return@withContext DataSource.LOCAL
        return@withContext DataSource.NONE
    }

    private suspend fun syncCredits(latestVersion: Boolean): DataSource = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.creditsVersion
        val localVersion = config.localConfigs.creditsVersion
        Telemetry.info(tag = TAG, message = "Syncing credits: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion && latestVersion) {
            if (creditUseCases.syncCredits()) {
                config.setConfigs { it.copy(creditsVersion = remoteVersion) }
                return@withContext DataSource.REMOTE
            }
        }
        if (creditUseCases.prepopulateCredits()) return@withContext DataSource.LOCAL
        return@withContext DataSource.NONE
    }

    companion object Companion {
        private const val TAG = "Sync"
    }
}
