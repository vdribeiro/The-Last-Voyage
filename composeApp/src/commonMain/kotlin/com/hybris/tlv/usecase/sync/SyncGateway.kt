package com.hybris.tlv.usecase.sync

import com.hybris.tlv.locale.Locale
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.storage.Config
import com.hybris.tlv.storage.LocalConfig
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.storage.RemoteConfigSettings
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.achievement.AchievementInternalUseCases
import com.hybris.tlv.usecase.combine
import com.hybris.tlv.usecase.credit.CreditInternalUseCases
import com.hybris.tlv.usecase.earth.EarthInternalUseCases
import com.hybris.tlv.usecase.event.EventInternalUseCases
import com.hybris.tlv.usecase.ship.ShipInternalUseCases
import com.hybris.tlv.usecase.space.SpaceInternalUseCases
import com.hybris.tlv.usecase.translation.TranslationInternalUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

internal class SyncGateway(
    private val locale: Locale,
    private val localConfig: LocalConfig,
    private val remoteConfig: RemoteConfig,
    private val internalTranslation: TranslationInternalUseCases,
    private val internalEarth: EarthInternalUseCases,
    private val internalShip: ShipInternalUseCases,
    private val internalSpace: SpaceInternalUseCases,
    private val internalEvent: EventInternalUseCases,
    private val internalAchievement: AchievementInternalUseCases,
    private val internalCredit: CreditInternalUseCases,
): SyncUseCases {

    override suspend fun setup(): Flow<SyncResult> = flow {
        emit(value = SyncResult.Loading(progress = 0f, total = 1f))
        Logger.setup()

        val configs = listOf(
            Config.TranslationsVersion,
            Config.CatastrophesVersion,
            Config.EnginesVersion,
            Config.StellarHostsVersion,
            Config.PlanetsVersion,
            Config.EventsVersion,
            Config.AchievementsVersion,
            Config.CreditsVersion,
            Config.DeveloperCorner,
            Config.Tip
        )
        val configSetting = RemoteConfigSettings(
            minimumFetchIntervalInSeconds = 3600,
            fetchTimeoutInSeconds = 60
        )
        remoteConfig
            .settings(settings = configSetting)
            .setDefaults(defaults = configs)
            .fetchAndActivate()

        emit(value = SyncResult.Success)
    }

    override suspend fun getArchive(): Flow<SyncResult> = internalSpace.getArchive()

    override suspend fun rewrite(): Flow<SyncResult> =
        combine(
            flows = listOf(
                internalTranslation.rewriteTranslations(),
                internalEarth.rewriteCatastrophes(),
                internalShip.rewriteEngines(),
                internalSpace.rewriteStellarHosts(),
                internalSpace.rewritePlanets(),
                internalEvent.rewriteEvents(),
                internalAchievement.rewriteAchievements(),
                internalCredit.rewriteCredits()
            )
        ) { it.combine() }

    override suspend fun sync(): Flow<SyncResult> =
        combine(
            flows = listOf(
                update(key = Config.TranslationsVersion) { internalTranslation.syncTranslations() },
                update(key = Config.CatastrophesVersion) { internalEarth.syncCatastrophes() },
                update(key = Config.EnginesVersion) { internalShip.syncEngines() },
                update(key = Config.StellarHostsVersion) { internalSpace.syncStellarHosts() },
                update(key = Config.PlanetsVersion) { internalSpace.syncPlanets() },
                update(key = Config.EventsVersion) { internalEvent.syncEvents() },
                update(key = Config.AchievementsVersion) { internalAchievement.syncAchievements() },
                update(key = Config.CreditsVersion) { internalCredit.syncCredits() }
            )
        ) { result ->
            result.getOrNull(index = 0)?.let { update(key = Config.TranslationsVersion, syncResult = it) }
            result.getOrNull(index = 1)?.let { update(key = Config.CatastrophesVersion, syncResult = it) }
            result.getOrNull(index = 2)?.let { update(key = Config.EnginesVersion, syncResult = it) }
            result.getOrNull(index = 3)?.let { update(key = Config.StellarHostsVersion, syncResult = it) }
            result.getOrNull(index = 4)?.let { update(key = Config.PlanetsVersion, syncResult = it) }
            result.getOrNull(index = 5)?.let { update(key = Config.EventsVersion, syncResult = it) }
            result.getOrNull(index = 6)?.let { update(key = Config.AchievementsVersion, syncResult = it) }
            result.getOrNull(index = 7)?.let { update(key = Config.CreditsVersion, syncResult = it) }
            result.combine()
        }

    private suspend fun update(key: Config, sync: suspend () -> Flow<SyncResult>): Flow<SyncResult> {
        val remoteValue = remoteConfig.getLong(key = key)
        val localValue = localConfig.getLong(key = key)
        Logger.info(tag = TAG, message = "${key.key}: remote: $remoteValue - local: $localValue")
        return if (remoteValue > localValue) sync() else flowOf(value = SyncResult.Success)
    }

    private fun update(key: Config, syncResult: SyncResult) {
        if (syncResult is SyncResult.Success) localConfig.put(key = key, value = remoteConfig.getLong(key = key))
    }

    override suspend fun prepopulate(): Flow<SyncResult> = flow {
        val totalOperations = 9f
        emit(value = SyncResult.Loading(progress = 0f, total = totalOperations))
        internalTranslation.prepopulateTranslations()
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
        emit(value = SyncResult.Loading(progress = 8f, total = totalOperations))
        internalTranslation.loadTranslationsToCache(languageIso = locale.getLanguage())
        emit(value = SyncResult.Success)
    }

    companion object Companion {
        private const val TAG = "AppCore"
    }
}