package com.hybris.tlv.usecase.sync

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.locale.getLanguage
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.achievement.AchievementInternalUseCases
import com.hybris.tlv.usecase.credit.CreditInternalUseCases
import com.hybris.tlv.usecase.earth.EarthInternalUseCases
import com.hybris.tlv.usecase.event.EventInternalUseCases
import com.hybris.tlv.usecase.ship.ShipInternalUseCases
import com.hybris.tlv.usecase.space.SpaceInternalUseCases
import com.hybris.tlv.usecase.sync.model.SyncResult
import com.hybris.tlv.usecase.translation.TranslationInternalUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class SyncGateway(
    private val storage: ConfigManager,
    private val internalTranslation: TranslationInternalUseCases,
    private val internalEarth: EarthInternalUseCases,
    private val internalShip: ShipInternalUseCases,
    private val internalSpace: SpaceInternalUseCases,
    private val internalEvent: EventInternalUseCases,
    private val internalAchievement: AchievementInternalUseCases,
    private val internalCredit: CreditInternalUseCases,
): SyncUseCases {

    override suspend fun prepopulate(): Flow<SyncResult> = flow {
        val totalOperations = 8f
        listOf(
            suspend {
                internalTranslation.prepopulateTranslations()
                internalTranslation.loadTranslationsToCache(languageIso = getLanguage())
            },
            suspend { internalEarth.prepopulateCatastrophes() },
            suspend { internalShip.prepopulateEngines() },
            suspend { internalSpace.prepopulateStellarHosts() },
            suspend { internalSpace.prepopulatePlanets() },
            suspend { internalEvent.prepopulateEvents() },
            suspend { internalAchievement.prepopulateAchievements() },
            suspend { internalCredit.prepopulateCredits() },
        ).forEachIndexed { index, prepopulate ->
            emit(value = SyncResult.Loading(progress = index.toFloat(), total = totalOperations))
            prepopulate()
        }
        emit(value = SyncResult.Success)
    }

    override suspend fun sync(): Flow<SyncResult> = flow {
        val totalOperations = 9f
        emit(value = SyncResult.Loading(progress = 0f, total = totalOperations))
        val localConfig = storage.getLocal()
        val remoteConfig = storage.getRemote()
        listOf(
            suspend {
                if (remoteConfig.translationsVersion > localConfig.translationsVersion) {
                    when (val result = internalTranslation.syncTranslations()) {
                        SyncResult.Success -> localConfig.copy(translationsVersion = remoteConfig.translationsVersion)
                        is SyncResult.Error -> Logger.error(tag = TAG, message = result.error)
                        is SyncResult.Loading -> Logger.error(tag = TAG, message = "Impossible state")
                    }
                }
            },
            suspend {
                if (remoteConfig.catastrophesVersion > localConfig.catastrophesVersion) {
                    when (val result = internalEarth.syncCatastrophes()) {
                        SyncResult.Success -> localConfig.copy(catastrophesVersion = remoteConfig.catastrophesVersion)
                        is SyncResult.Error -> Logger.error(tag = TAG, message = result.error)
                        is SyncResult.Loading -> Logger.error(tag = TAG, message = "Impossible state")
                    }
                }
            },
            suspend {
                if (remoteConfig.enginesVersion > localConfig.enginesVersion) {
                    when (val result = internalShip.syncEngines()) {
                        SyncResult.Success -> localConfig.copy(enginesVersion = remoteConfig.enginesVersion)
                        is SyncResult.Error -> Logger.error(tag = TAG, message = result.error)
                        is SyncResult.Loading -> Logger.error(tag = TAG, message = "Impossible state")
                    }
                }
            },
            suspend {
                if (remoteConfig.stellarHostsVersion > localConfig.stellarHostsVersion) {
                    when (val result = internalSpace.syncStellarHosts()) {
                        SyncResult.Success -> localConfig.copy(stellarHostsVersion = remoteConfig.stellarHostsVersion)
                        is SyncResult.Error -> Logger.error(tag = TAG, message = result.error)
                        is SyncResult.Loading -> Logger.error(tag = TAG, message = "Impossible state")
                    }
                }
            },
            suspend {
                if (remoteConfig.planetsVersion > localConfig.planetsVersion) {
                    when (val result = internalSpace.syncPlanets()) {
                        SyncResult.Success -> localConfig.copy(planetsVersion = remoteConfig.planetsVersion)
                        is SyncResult.Error -> Logger.error(tag = TAG, message = result.error)
                        is SyncResult.Loading -> Logger.error(tag = TAG, message = "Impossible state")
                    }
                }
            },
            suspend {
                if (remoteConfig.eventsVersion > localConfig.eventsVersion) {
                    when (val result = internalEvent.syncEvents()) {
                        SyncResult.Success -> localConfig.copy(eventsVersion = remoteConfig.eventsVersion)
                        is SyncResult.Error -> Logger.error(tag = TAG, message = result.error)
                        is SyncResult.Loading -> Logger.error(tag = TAG, message = "Impossible state")
                    }
                }
            },
            suspend {
                if (remoteConfig.achievementsVersion > localConfig.achievementsVersion) {
                    when (val result = internalAchievement.syncAchievements()) {
                        SyncResult.Success -> localConfig.copy(achievementsVersion = remoteConfig.achievementsVersion)
                        is SyncResult.Error -> Logger.error(tag = TAG, message = result.error)
                        is SyncResult.Loading -> Logger.error(tag = TAG, message = "Impossible state")
                    }
                }
            },
            suspend {
                if (remoteConfig.creditsVersion > localConfig.creditsVersion) {
                    when (val result = internalCredit.syncCredits()) {
                        SyncResult.Success -> localConfig.copy(creditsVersion = remoteConfig.creditsVersion)
                        is SyncResult.Error -> Logger.error(tag = TAG, message = result.error)
                        is SyncResult.Loading -> Logger.error(tag = TAG, message = "Impossible state")
                    }
                }
            },
        ).forEachIndexed { index, sync ->
            emit(value = SyncResult.Loading(progress = index.toFloat() + 1f, total = totalOperations))
            sync()
        }
        emit(value = SyncResult.Success)
    }

    override suspend fun getArchive(): Flow<SyncResult> = internalSpace.getArchive()

    companion object Companion {
        private const val TAG = "Sync"
    }
}