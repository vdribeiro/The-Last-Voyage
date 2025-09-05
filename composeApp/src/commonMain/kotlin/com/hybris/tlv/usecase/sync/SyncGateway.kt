package com.hybris.tlv.usecase.sync

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.config.Configs
import com.hybris.tlv.isDebug
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.achievement.AchievementInternalUseCases
import com.hybris.tlv.usecase.credit.CreditInternalUseCases
import com.hybris.tlv.usecase.earth.EarthInternalUseCases
import com.hybris.tlv.usecase.event.EventInternalUseCases
import com.hybris.tlv.usecase.learning.LearningInternalUseCases
import com.hybris.tlv.usecase.ship.ShipInternalUseCases
import com.hybris.tlv.usecase.space.SpaceInternalUseCases
import com.hybris.tlv.usecase.sync.model.SyncResult
import com.hybris.tlv.usecase.translation.TranslationInternalUseCases
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class SyncGateway(
    private val config: ConfigManager,
    private val internalTranslation: TranslationInternalUseCases,
    private val internalLearning: LearningInternalUseCases,
    private val internalEarth: EarthInternalUseCases,
    private val internalShip: ShipInternalUseCases,
    private val internalSpace: SpaceInternalUseCases,
    private val internalEvent: EventInternalUseCases,
    private val internalAchievement: AchievementInternalUseCases,
    private val internalCredit: CreditInternalUseCases,
): SyncUseCases {

    override suspend fun sync(): Flow<SyncResult> = channelFlow {
        val localConfig = config.getLocal()
        val remoteConfig = if (isDebug) Configs(enableFeature = true) else config.getRemote()

        val tasks = listOf(
            SyncTask(
                remoteVersion = remoteConfig.translationsVersion,
                localVersion = localConfig.translationsVersion,
                sync = internalTranslation::syncTranslations,
                prepopulate = internalTranslation::prepopulateTranslations,
                updateConfig = { configs, version -> configs.copy(translationsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.learningsVersion,
                localVersion = localConfig.learningsVersion,
                sync = internalLearning::syncLearnings,
                prepopulate = internalLearning::prepopulateLearnings,
                updateConfig = { configs, version -> configs.copy(learningsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.catastrophesVersion,
                localVersion = localConfig.catastrophesVersion,
                sync = internalEarth::syncCatastrophes,
                prepopulate = internalEarth::prepopulateCatastrophes,
                updateConfig = { configs, version -> configs.copy(catastrophesVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.enginesVersion,
                localVersion = localConfig.enginesVersion,
                sync = internalShip::syncEngines,
                prepopulate = internalShip::prepopulateEngines,
                updateConfig = { configs, version -> configs.copy(enginesVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.stellarHostsVersion,
                localVersion = localConfig.stellarHostsVersion,
                sync = internalSpace::syncStellarHosts,
                prepopulate = internalSpace::prepopulateStellarHosts,
                updateConfig = { configs, version -> configs.copy(stellarHostsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.planetsVersion,
                localVersion = localConfig.planetsVersion,
                sync = internalSpace::syncPlanets,
                prepopulate = internalSpace::prepopulatePlanets,
                updateConfig = { configs, version -> configs.copy(planetsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.eventsVersion,
                localVersion = localConfig.eventsVersion,
                sync = internalEvent::syncEvents,
                prepopulate = internalEvent::prepopulateEvents,
                updateConfig = { configs, version -> configs.copy(eventsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.achievementsVersion,
                localVersion = localConfig.achievementsVersion,
                sync = internalAchievement::syncAchievements,
                prepopulate = internalAchievement::prepopulateAchievements,
                updateConfig = { configs, version -> configs.copy(achievementsVersion = version) }
            ),
            SyncTask(
                remoteVersion = remoteConfig.creditsVersion,
                localVersion = localConfig.creditsVersion,
                sync = internalCredit::syncCredits,
                prepopulate = internalCredit::prepopulateCredits,
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
        }.copy(
            developerCorner = remoteConfig.developerCorner,
            support = remoteConfig.support,
            formula = remoteConfig.formula,
            featureFeedback = remoteConfig.featureFeedback,
            featureLearn = remoteConfig.featureLearn
        )

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

    override suspend fun getArchive(): Flow<SyncResult> = internalSpace.getArchive()

    companion object Companion {
        private const val TAG = "Sync"
    }
}
