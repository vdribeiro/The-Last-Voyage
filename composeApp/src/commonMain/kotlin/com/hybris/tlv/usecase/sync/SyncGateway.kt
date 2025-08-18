package com.hybris.tlv.usecase.sync

import com.hybris.tlv.locale.Locale
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.storage.Config
import com.hybris.tlv.storage.LocalConfig
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.storage.RemoteConfigSettings
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.combine
import com.hybris.tlv.usecase.credits.CreditsUseCases
import com.hybris.tlv.usecase.earth.EarthUseCases
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.translation.TranslationUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

internal class SyncGateway(
    private val locale: Locale,
    private val localConfig: LocalConfig,
    private val remoteConfig: RemoteConfig,
    private val translation: TranslationUseCases,
    private val earth: EarthUseCases,
    private val ship: ShipUseCases,
    private val space: SpaceUseCases,
    private val event: EventUseCases,
    private val achievement: AchievementUseCases,
    private val credits: CreditsUseCases,
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

    override suspend fun getArchive(): Flow<SyncResult> = space.getArchive()

    override suspend fun rewrite(): Flow<SyncResult> =
        kotlinx.coroutines.flow.combine(
            flows = listOf(
                translation.rewrite(),
                earth.rewrite(),
                ship.rewrite(),
                space.rewrite(),
                event.rewrite(),
                achievement.rewrite(),
                credits.rewrite()
            )
        ) { it.combine() }

    override suspend fun sync(): Flow<SyncResult> =
        kotlinx.coroutines.flow.combine(
            flows = listOf(
                update(key = Config.TranslationsVersion) { translation.syncTranslations() },
                update(key = Config.CatastrophesVersion) { earth.syncCatastrophes() },
                update(key = Config.EnginesVersion) { ship.syncEngines() },
                update(key = Config.StellarHostsVersion) { space.syncStellarHosts() },
                update(key = Config.PlanetsVersion) { space.syncPlanets() },
                update(key = Config.EventsVersion) { event.syncEvents() },
                update(key = Config.AchievementsVersion) { achievement.syncAchievements() },
                update(key = Config.CreditsVersion) { credits.syncCredits() }
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
        translation.prepopulateTranslations()
        emit(value = SyncResult.Loading(progress = 1f, total = totalOperations))
        earth.prepopulateCatastrophes()
        emit(value = SyncResult.Loading(progress = 2f, total = totalOperations))
        ship.prepopulateEngines()
        emit(value = SyncResult.Loading(progress = 3f, total = totalOperations))
        space.prepopulateStellarHosts()
        emit(value = SyncResult.Loading(progress = 4f, total = totalOperations))
        space.prepopulatePlanets()
        emit(value = SyncResult.Loading(progress = 5f, total = totalOperations))
        event.prepopulateEvents()
        emit(value = SyncResult.Loading(progress = 6f, total = totalOperations))
        achievement.prepopulateAchievements()
        emit(value = SyncResult.Loading(progress = 7f, total = totalOperations))
        credits.prepopulateCredits()
        emit(value = SyncResult.Loading(progress = 8f, total = totalOperations))
        translation.loadTranslationsToCache(languageIso = locale.getLanguage())
        emit(value = SyncResult.Success)
    }

    companion object Companion {
        private const val TAG = "AppCore"
    }
}