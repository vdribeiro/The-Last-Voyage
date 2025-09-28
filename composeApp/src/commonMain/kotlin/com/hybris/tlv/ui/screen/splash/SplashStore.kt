package com.hybris.tlv.ui.screen.splash

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope

internal class SplashStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    private val config: ConfigManager,
    private val translateUseCases: TranslationUseCases,
    private val archiveUseCases: ArchiveUseCases,
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
        config.fetch()

        val tasks = listOf(
            // Uncomment to get exoplanet archive
            //suspend { archiveUseCases.getArchive() },
            suspend { translateUseCases.syncTranslations(); translateUseCases.prepopulateTranslations() },
            suspend { learningUseCases.syncLearnings(); learningUseCases.prepopulateLearnings() },
            suspend { catastropheUseCases.syncCatastrophes(); catastropheUseCases.prepopulateCatastrophes() },
            suspend { shipUseCases.syncEngines(); shipUseCases.prepopulateEngines() },
            suspend { spaceUseCases.syncStellarHosts(); spaceUseCases.prepopulateStellarHosts() },
            suspend { spaceUseCases.syncPlanets(); spaceUseCases.prepopulatePlanets() },
            suspend { eventUseCases.syncEvents(); eventUseCases.prepopulateEvents() },
            suspend { achievementUseCases.syncAchievements(); achievementUseCases.prepopulateAchievements() },
            suspend { creditUseCases.syncCredits(); creditUseCases.prepopulateCredits() }
        )
        val deferredJobs = supervisorScope {
            tasks.map { task -> async { task() } }
        }
        deferredJobs.forEachIndexed { index, job ->
            job.await()
            val progress = (index + 1).toFloat() / deferredJobs.size.toFloat()
            updateState { it.copy(progress = progress) }
        }

        config.flush()
        delay(timeMillis = 1000L)
        navigate(screen = Screen.MainMenu)
    }

    override fun goBack(state: SplashState) {}
}
