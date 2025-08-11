package com.hybris.tlv

import com.hybris.tlv.config.LocalConfig
import com.hybris.tlv.config.RemoteConfig
import com.hybris.tlv.config.RemoteConfigSettings
import com.hybris.tlv.config.Config
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

internal class AppCore(
    override val dispatcher: Dispatcher,
    override val locale: Locale,
    override val localConfig: LocalConfig,
    override val remoteConfig: RemoteConfig,
    override val useCases: UseCases
): Core {

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

    override suspend fun rewrite(): Flow<SyncResult> =
        combine(
            flows = listOf(
                useCases.translation.setup(),
                useCases.earth.setup(),
                useCases.ship.setup(),
                useCases.space.setup(),
                useCases.event.setup(),
                useCases.achievement.setup(),
                useCases.credits.setup()
            )
        ) { it.combine() }

    override suspend fun prepopulate(): Flow<SyncResult> = flow {
        val totalOperations = 9f
        emit(value = SyncResult.Loading(progress = 0f, total = totalOperations))
        useCases.translation.prepopulateTranslations()
        emit(value = SyncResult.Loading(progress = 1f, total = totalOperations))
        useCases.earth.prepopulateCatastrophes()
        emit(value = SyncResult.Loading(progress = 2f, total = totalOperations))
        useCases.ship.prepopulateEngines()
        emit(value = SyncResult.Loading(progress = 3f, total = totalOperations))
        useCases.space.prepopulateStellarHosts()
        emit(value = SyncResult.Loading(progress = 4f, total = totalOperations))
        useCases.space.prepopulatePlanets()
        emit(value = SyncResult.Loading(progress = 5f, total = totalOperations))
        useCases.event.prepopulateEvents()
        emit(value = SyncResult.Loading(progress = 6f, total = totalOperations))
        useCases.achievement.prepopulateAchievements()
        emit(value = SyncResult.Loading(progress = 7f, total = totalOperations))
        useCases.credits.prepopulateCredits()
        emit(value = SyncResult.Loading(progress = 8f, total = totalOperations))
        useCases.translation.loadTranslationsToCache(languageIso = locale.getLanguage())
        emit(value = SyncResult.Success)
    }

    override suspend fun sync(): Flow<SyncResult> =
        combine(
            flows = listOf(
                update(key = Config.TranslationsVersion) { useCases.translation.syncTranslations() },
                update(key = Config.CatastrophesVersion) { useCases.earth.syncCatastrophes() },
                update(key = Config.EnginesVersion) { useCases.ship.syncEngines() },
                update(key = Config.StellarHostsVersion) { useCases.space.syncStellarHosts() },
                update(key = Config.PlanetsVersion) { useCases.space.syncPlanets() },
                update(key = Config.EventsVersion) { useCases.event.syncEvents() },
                update(key = Config.AchievementsVersion) { useCases.achievement.syncAchievements() },
                update(key = Config.CreditsVersion) { useCases.credits.syncCredits() }
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

    companion object {
        private const val TAG = "AppCore"
    }
}
