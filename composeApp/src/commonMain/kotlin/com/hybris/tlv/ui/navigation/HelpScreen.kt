package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.screen.help.HelpScreen
import com.hybris.tlv.ui.screen.help.HelpStateBuilder
import com.hybris.tlv.ui.screen.help.HelpStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.helpScreen(
    config: ConfigManager,
    useCases: UseCases
) = graph<HelpScreen, HelpStore>(
    store = {
        HelpStore(
            stateBuilder = HelpStateBuilder.Default,
            config = config,
            learningUseCases = useCases.learning
        )
    },
    screen = { HelpScreen(store = it) }
)

@Serializable
internal data object HelpScreen: Screen
