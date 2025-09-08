package com.hybris.tlv.usecase.sync

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.config.Configs
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.isDebug
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.sync.model.SyncResult
import database.AppDatabase
import io.ktor.client.HttpClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class SyncGateway(
    private val dispatcher: Dispatcher,
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    private val database: AppDatabase,
): SyncUseCases {

    private val translationSync = TranslationSync(
        dispatcher = dispatcher,
        httpClient = httpClient,
        database = database
    )
    private val learningSync = LearningSync(
        httpClient = httpClient,
        database = database
    )
    private val catastropheSync = CatastropheSync(
        httpClient = httpClient,
        database = database
    )
    private val shipSync = ShipSync(
        httpClient = httpClient,
        database = database
    )
    private val stellarHostSync = StellarHostSync(
        httpClient = httpClient,
        database = database
    )
    private val planetSync = PlanetSync(
        httpClient = httpClient,
        database = database
    )
    private val eventSync = EventSync(
        httpClient = httpClient,
        database = database
    )
    private val achievementSync = AchievementSync(
        httpClient = httpClient,
        database = database
    )
    private val creditSync = CreditSync(
        httpClient = httpClient,
        database = database
    )
    private val spaceSync = SpaceSync(
        httpClient = httpClient
    )

    override suspend fun sync(): Flow<SyncResult> = channelFlow {
        val localConfig = config.getLocal()
        val remoteConfig = if (isDebug) Configs(enableAllFeatures = true) else config.getRemote()

        val tasks = listOf(
            SyncTask(
                remoteVersion = remoteConfig.translationsVersion,
                localVersion = localConfig.translationsVersion,
                sync = translationSync::syncTranslations,
                prepopulate = translationSync::prepopulateTranslations,
                updateConfig = { configs, version -> configs.copy(translationsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.learningsVersion,
                localVersion = localConfig.learningsVersion,
                sync = learningSync::syncLearnings,
                prepopulate = learningSync::prepopulateLearnings,
                updateConfig = { configs, version -> configs.copy(learningsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.catastrophesVersion,
                localVersion = localConfig.catastrophesVersion,
                sync = catastropheSync::syncCatastrophes,
                prepopulate = catastropheSync::prepopulateCatastrophes,
                updateConfig = { configs, version -> configs.copy(catastrophesVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.enginesVersion,
                localVersion = localConfig.enginesVersion,
                sync = shipSync::syncEngines,
                prepopulate = shipSync::prepopulateEngines,
                updateConfig = { configs, version -> configs.copy(enginesVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.stellarHostsVersion,
                localVersion = localConfig.stellarHostsVersion,
                sync = stellarHostSync::syncStellarHosts,
                prepopulate = stellarHostSync::prepopulateStellarHosts,
                updateConfig = { configs, version -> configs.copy(stellarHostsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.planetsVersion,
                localVersion = localConfig.planetsVersion,
                sync = planetSync::syncPlanets,
                prepopulate = planetSync::prepopulatePlanets,
                updateConfig = { configs, version -> configs.copy(planetsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.eventsVersion,
                localVersion = localConfig.eventsVersion,
                sync = eventSync::syncEvents,
                prepopulate = eventSync::prepopulateEvents,
                updateConfig = { configs, version -> configs.copy(eventsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.achievementsVersion,
                localVersion = localConfig.achievementsVersion,
                sync = achievementSync::syncAchievements,
                prepopulate = achievementSync::prepopulateAchievements,
                updateConfig = { configs, version -> configs.copy(achievementsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.creditsVersion,
                localVersion = localConfig.creditsVersion,
                sync = creditSync::syncCredits,
                prepopulate = creditSync::prepopulateCredits,
                updateConfig = { configs, version -> configs.copy(creditsVersion = version) }
            ),
        )

        val progressMutex = Mutex()
        val total = tasks.size.toFloat()
        var progress = 0f
        send(element = SyncResult.Loading(progress = progress, total = total))

        val finalConfig = supervisorScope {
            val updaters = tasks.mapIndexed { index, task ->
                async {
                    var configUpdater: (Configs) -> Configs = { it }
                    if (task.remoteVersion > task.localVersion) {
                        when (val result = task.sync()) {
                            SyncResult.Success -> configUpdater = { config -> task.updateConfig(config, task.remoteVersion) }
                            is SyncResult.Error -> Logger.error(tag = TAG, message = result.error)
                            is SyncResult.Loading -> Logger.error(tag = TAG, message = "Impossible state")
                        }
                    }
                    task.prepopulate()
                    send(element = SyncResult.Loading(progress = progressMutex.withLock { ++progress }, total = total))
                    configUpdater
                }
            }.awaitAll()
            updaters.fold(initial = localConfig) { config, updater -> updater(config) }
        }.copyValues(config = remoteConfig).copyFeatures(config = remoteConfig)

        config.setLocal(configs = finalConfig)
        send(element = SyncResult.Success)
    }

    private data class SyncTask<T: Comparable<T>>(
        val remoteVersion: T,
        val localVersion: T,
        val sync: suspend () -> SyncResult,
        val prepopulate: suspend () -> Unit,
        val updateConfig: (Configs, T) -> Configs,
    )

    override suspend fun getArchive(): Flow<SyncResult> = spaceSync.getArchive()

    companion object Companion {
        private const val TAG = "Sync"
    }
}
