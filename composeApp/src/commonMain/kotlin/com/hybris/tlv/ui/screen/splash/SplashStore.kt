package com.hybris.tlv.ui.screen.splash

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.platform.Property
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.credit.CreditUseCases
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.learning.LearningUseCases
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.ArchiveUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.translation.TranslationUseCases

internal class SplashStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    private val config: ConfigManager,
    private val archiveUseCases: ArchiveUseCases,
    private val translateUseCases: TranslationUseCases,
    private val learningUseCases: LearningUseCases,
    private val catastropheUseCases: CatastropheUseCases,
    private val shipUseCases: ShipUseCases,
    private val spaceUseCases: SpaceUseCases,
    private val eventUseCases: EventUseCases,
    private val achievementUseCases: AchievementUseCases,
    private val creditUseCases: CreditUseCases
): Store<SplashState, SplashAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = SplashState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        config
            .refresh()
            .savePreferences()
            .saveConfigs()

        val remoteVersion = config.remoteConfigs.appVersion
        val localVersion = config.localConfigs.appVersion
        Telemetry.info(tag = TAG, message = "App version: remote version: $remoteVersion, local version: $localVersion")
        if (localVersion == 0L || Property.APP_VERSION_NUMBER == localVersion) sync()
        config.setConfigs { it.copy(appVersion = remoteVersion) }

        translateUseCases.refreshCache()
        Telemetry.info(tag = TAG, message = "Preferences\n${config.preferences}")
        Telemetry.info(tag = TAG, message = "Local Configs\n${config.localConfigs}")
        Telemetry.info(tag = TAG, message = "Remote Configs\n${config.remoteConfigs}")
        delay(timeMillis = 1000L)
        Telemetry.info(tag = TAG, message = "Setup complete")

        if (!config.preferences.showIntro) navigate(screen = Screen.MainMenu) else {
            config.setPreferences { it.copy(showIntro = false) }
            updateState { it.copy(loading = false, currentContent = Content.INTRO) }
        }

        config
            .savePreferences()
            .saveConfigs()
    }

    private suspend fun sync() = supervisorScope {
        val tasks = listOf(
            suspend { archiveUseCases.getArchive() },
            suspend { translateUseCases.syncTranslations() },
            suspend { learningUseCases.syncLearnings() },
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
            }.getOrElse {
                Telemetry.error(tag = TAG, message = "Sync task failed.", throwable = it)
            }
            updateState { it.copy(progress = (index + 1).toFloat() / total) }
        }
        config.saveConfigs()
    }

    override fun goBack(state: SplashState) {}

    override fun reducer(state: SplashState, action: SplashAction) {
        when (action) {
            SplashAction.Next -> navigate(screen = Screen.MainMenu)
        }
    }

    companion object {
        private const val TAG = "SplashStore"
    }
}
