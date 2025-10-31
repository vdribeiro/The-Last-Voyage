package com.hybris.tlv.ui.screen.help

import kotlinx.coroutines.Job
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.learning.LearningUseCases
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.model.Score
import com.hybris.tlv.usecase.space.model.StellarHost

internal class HelpStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: HelpStateBuilder,
    private val config: ConfigManager,
    private val learningUseCases: LearningUseCases
): Store<HelpState, HelpAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        HelpStateBuilder.Default -> HelpState()
        is HelpStateBuilder.FromSavableState -> stateBuilder.state
    }
) {
    init {
        when (stateBuilder) {
            HelpStateBuilder.Default -> setup()
            is HelpStateBuilder.FromSavableState -> {}
        }
    }

    override fun getSavableState(state: HelpState): Any =
        HelpStateBuilder.FromSavableState(state = state)

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val learningsMap = learningUseCases.getLearnings().groupBy { it.type }

        updateState {
            it.copy(
                loading = false,
                featureTutorial = config.localConfigs.featureTutorial,
                formula = config.localConfigs.formula,
                learningsMap = learningsMap,
            )
        }
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    override fun goBack(state: HelpState) {
        when (state.currentContent) {
            Content.LEARN_MENU -> super.goBack(state = state)
            Content.HOST_DEFINITION,
            Content.PLANET_DEFINITION,
            Content.HABITABILITY -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
        }
    }

    override fun reducer(state: HelpState, action: HelpAction) {
        when (action) {
            HelpAction.HostDefinition -> updateState { it.copy(currentContent = Content.HOST_DEFINITION) }
            HelpAction.PlanetDefinition -> updateState { it.copy(currentContent = Content.PLANET_DEFINITION) }
            HelpAction.Mechanics -> navigate(screen = Screen.Tutorial)
            HelpAction.Habitability -> updateState { it.copy(currentContent = Content.HABITABILITY) }
        }
    }

    companion object Companion {
        private const val TAG = "Help"
    }
}
