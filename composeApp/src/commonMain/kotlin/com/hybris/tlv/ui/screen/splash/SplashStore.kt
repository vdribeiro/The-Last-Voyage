package com.hybris.tlv.ui.screen.splash

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import com.hybris.tlv.TLV
import com.hybris.tlv.TLV.ARCHIVE
import com.hybris.tlv.TLV.RESET
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.platform.Property
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.credit.CreditUseCases
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.ArchiveUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.translation.TranslationUseCases

internal class SplashStore(
    private val config: ConfigManager,
    private val archiveUseCases: ArchiveUseCases,
    private val translationUseCases: TranslationUseCases,
    private val catastropheUseCases: CatastropheUseCases,
    private val shipUseCases: ShipUseCases,
    private val spaceUseCases: SpaceUseCases,
    private val eventUseCases: EventUseCases,
    private val achievementUseCases: AchievementUseCases,
    private val creditUseCases: CreditUseCases
): Store<SplashState, SplashAction>(
    initialState = SplashState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        if (RESET) TLV.reset()
        config.setup()

        val remoteVersion = config.remoteConfigs.value.appVersion
        val localVersion = config.localConfigs.value.appVersion
        Telemetry.info(tag = TAG, message = "App version: remote version: $remoteVersion, local version: $localVersion")
        config.setConfigs { it.copy(appVersion = remoteVersion) }
        if (localVersion == 0L || Property.APP_VERSION_NUMBER == remoteVersion) sync()
        config.saveConfigs()
        translationUseCases.refreshCache()

        Telemetry.info(tag = TAG, message = "Preferences\n${config.preferences.value}")
        Telemetry.info(tag = TAG, message = "Local Configs\n${config.localConfigs.value}")
        Telemetry.info(tag = TAG, message = "Remote Configs\n${config.remoteConfigs.value}")
        delay(timeMillis = 1000L)

        Telemetry.info(tag = TAG, message = "Setup complete")

        if (config.preferences.value.showIntro) {
            config.setPreferences { it.copy(showIntro = false) }
            updateState { it.copy(loading = false, currentContent = Content.INTRO) }
        } else navigate(screen = Screen.MainMenu)
    }

    private suspend fun sync() = supervisorScope {
        val tasks = listOf(
            suspend { if (ARCHIVE) archiveUseCases.getArchive() },
            suspend { translationUseCases.syncTranslations() },
            suspend { catastropheUseCases.syncCatastrophes() },
            suspend { shipUseCases.syncEngines() },
            suspend { spaceUseCases.syncStellarHosts() },
            suspend { spaceUseCases.syncPlanets() },
            suspend { eventUseCases.syncEvents() },
            suspend { achievementUseCases.syncAchievements() },
            suspend { creditUseCases.syncCredits() }
        )
        val total = tasks.size.toFloat()
        tasks.map { task -> async { task() } }.forEachIndexed { index, job ->
            runCatching {
                job.await()
            }.onFailure { Telemetry.error(tag = TAG, message = "Sync task failed.", throwable = it) }.getOrNull()
            updateState { it.copy(progress = (index + 1).toFloat() / total) }
        }
    }

    override fun back(state: SplashState) {}

    override fun reducer(state: SplashState, action: SplashAction) {
        when (action) {
            SplashAction.Next -> navigate(screen = Screen.MainMenu)
        }
    }

    companion object {
        private const val TAG = "SplashStore"
    }
}
