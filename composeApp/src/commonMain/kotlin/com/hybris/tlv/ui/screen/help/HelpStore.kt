package com.hybris.tlv.ui.screen.help

import kotlin.concurrent.Volatile
import kotlinx.coroutines.Job
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.test.VisibleForTesting
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Store

internal class HelpStore(
    private val config: ConfigManager,
): Store<HelpState, HelpAction>(
    initialState = HelpState()
) {
    @VisibleForTesting
    @Volatile
    internal var versionClick: Int = 0

    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val configs = config.localConfigs
        updateState {
            it.copy(
                loading = false,
                formula = configs.formula,
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun versionClick(action: HelpAction.VersionClick) {
        if (action.reset) versionClick = 0 else versionClick++
        updateState { it.copy(showSnackbar = versionClick >= CLICKS_FOR_HINTS) }
    }

    private fun reset(): Job = launch(id = "reset") {
        navigate(screen = Screen.Splash(reset = true))
    }

    override fun back(state: HelpState) {
        when (state.currentContent) {
            Content.LEARN_MENU -> super.back(state = state)
            Content.NAVIGATION,
            Content.CONTROL_PANEL,
            Content.HOST_DEFINITION,
            Content.HOST_TYPE,
            Content.PLANET_DEFINITION,
            Content.PLANET_TYPE,
            Content.HABITABILITY,
            Content.SCORE -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
        }
    }

    override fun reducer(state: HelpState, action: HelpAction) {
        when (action) {
            HelpAction.Navigation -> updateState { it.copy(currentContent = Content.NAVIGATION) }
            HelpAction.ControlPanel -> updateState { it.copy(currentContent = Content.CONTROL_PANEL) }
            HelpAction.Mechanics -> navigate(screen = Screen.Tutorial())
            HelpAction.HostDefinition -> updateState { it.copy(currentContent = Content.HOST_DEFINITION) }
            HelpAction.HostType -> updateState { it.copy(currentContent = Content.HOST_TYPE) }
            HelpAction.PlanetDefinition -> updateState { it.copy(currentContent = Content.PLANET_DEFINITION) }
            HelpAction.PlanetType -> updateState { it.copy(currentContent = Content.PLANET_TYPE) }
            HelpAction.Habitability -> updateState { it.copy(currentContent = Content.HABITABILITY) }
            HelpAction.Score -> updateState { it.copy(currentContent = Content.SCORE) }
            HelpAction.Catastrophes -> navigate(screen = Screen.CatastropheExplorer)
            HelpAction.Events -> navigate(screen = Screen.EventExplorer)
            is HelpAction.VersionClick -> versionClick(action = action)
            HelpAction.Reset -> reset()
        }
    }

    companion object Companion {
        private const val TAG = "Help"
        private const val CLICKS_FOR_HINTS = 5
    }
}
