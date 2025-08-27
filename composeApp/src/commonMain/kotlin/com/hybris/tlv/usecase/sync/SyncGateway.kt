package com.hybris.tlv.usecase.sync

import com.hybris.tlv.locale.Locale
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.storage.Config
import com.hybris.tlv.storage.ConfigKey
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
    private val locale: Locale,
    private val config: Config,
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
        emit(value = SyncResult.Loading(progress = 0f, total = totalOperations))
        internalTranslation.prepopulateTranslations()
        internalTranslation.loadTranslationsToCache(languageIso = locale.getLanguage())
        emit(value = SyncResult.Loading(progress = 1f, total = totalOperations))
        internalEarth.prepopulateCatastrophes()
        emit(value = SyncResult.Loading(progress = 2f, total = totalOperations))
        internalShip.prepopulateEngines()
        emit(value = SyncResult.Loading(progress = 3f, total = totalOperations))
        internalSpace.prepopulateStellarHosts()
        emit(value = SyncResult.Loading(progress = 4f, total = totalOperations))
        internalSpace.prepopulatePlanets()
        emit(value = SyncResult.Loading(progress = 5f, total = totalOperations))
        internalEvent.prepopulateEvents()
        emit(value = SyncResult.Loading(progress = 6f, total = totalOperations))
        internalAchievement.prepopulateAchievements()
        emit(value = SyncResult.Loading(progress = 7f, total = totalOperations))
        internalCredit.prepopulateCredits()
        emit(value = SyncResult.Success)
    }

    override suspend fun sync(): Flow<SyncResult> = flow {
        val totalOperations = 8f
        emit(value = SyncResult.Loading(progress = 0f, total = totalOperations))
        update(key = ConfigKey.TranslationsVersion) { internalTranslation.syncTranslations() }
        emit(value = SyncResult.Loading(progress = 1f, total = totalOperations))
        update(key = ConfigKey.CatastrophesVersion) { internalEarth.syncCatastrophes() }
        emit(value = SyncResult.Loading(progress = 2f, total = totalOperations))
        update(key = ConfigKey.EnginesVersion) { internalShip.syncEngines() }
        emit(value = SyncResult.Loading(progress = 3f, total = totalOperations))
        update(key = ConfigKey.StellarHostsVersion) { internalSpace.syncStellarHosts() }
        emit(value = SyncResult.Loading(progress = 4f, total = totalOperations))
        update(key = ConfigKey.PlanetsVersion) { internalSpace.syncPlanets() }
        emit(value = SyncResult.Loading(progress = 5f, total = totalOperations))
        update(key = ConfigKey.EventsVersion) { internalEvent.syncEvents() }
        emit(value = SyncResult.Loading(progress = 6f, total = totalOperations))
        update(key = ConfigKey.AchievementsVersion) { internalAchievement.syncAchievements() }
        emit(value = SyncResult.Loading(progress = 7f, total = totalOperations))
        update(key = ConfigKey.CreditsVersion) { internalCredit.syncCredits() }
        emit(value = SyncResult.Success)
    }

    private suspend fun update(key: ConfigKey, sync: suspend () -> SyncResult) {
        val remoteValue = remoteConfig.getLong(key = key)
        val localValue = config.getLong(key = key)
        Logger.info(tag = TAG, message = "${key.key}: remote: $remoteValue - local: $localValue")
        when (val result = if (remoteValue > localValue) sync() else SyncResult.Success) {
            SyncResult.Success -> config.put(key = key, value = remoteConfig.getLong(key = key))
            is SyncResult.Error -> Logger.error(tag = TAG, message = result.error)
            is SyncResult.Loading -> Logger.error(tag = TAG, message = "Impossible state")
        }
    }

    override suspend fun getArchive(): Flow<SyncResult> = internalSpace.getArchive()

    companion object Companion {
        private const val TAG = "AppCore"
    }
}