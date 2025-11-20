package com.hybris.tlv.ui.screen.help

import kotlinx.coroutines.Job
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Tutorial
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.learning.LearningUseCases

internal class HelpStore(
    private val config: ConfigManager,
    private val learningUseCases: LearningUseCases
): Store<HelpState, HelpAction>(
    config = config,
    initialState = HelpState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val configs = config.localConfigs.value
        val learningsMap = learningUseCases.getLearnings().groupBy { it.type }

        updateState {
            it.copy(
                loading = false,
                formula = configs.formula,
                learningsMap = learningsMap,
            )
        }
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    override fun back(state: HelpState) {
        when (state.currentContent) {
            Content.LEARN_MENU -> super.back(state = state)
            Content.NAVIGATION,
            Content.HOST_DEFINITION,
            Content.PLANET_DEFINITION,
            Content.HABITABILITY -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
        }
    }

    override fun reducer(state: HelpState, action: HelpAction) {
        when (action) {
            HelpAction.Navigation -> updateState { it.copy(currentContent = Content.NAVIGATION) }
            HelpAction.HostDefinition -> updateState { it.copy(currentContent = Content.HOST_DEFINITION) }
            HelpAction.PlanetDefinition -> updateState { it.copy(currentContent = Content.PLANET_DEFINITION) }
            HelpAction.Mechanics -> navigate(screen = Tutorial())
            HelpAction.Habitability -> updateState { it.copy(currentContent = Content.HABITABILITY) }
        }
    }

    companion object Companion {
        private const val TAG = "Help"
    }
}
