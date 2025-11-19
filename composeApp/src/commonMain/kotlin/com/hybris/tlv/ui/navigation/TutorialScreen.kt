package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import com.hybris.tlv.ui.screen.tutorial.TutorialScreen
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder
import com.hybris.tlv.ui.screen.tutorial.TutorialStore

internal fun NavGraphBuilder.tutorialScreen() =
    graph<TutorialScreen, TutorialStore>(
        store = { TutorialStore(stateBuilder = it.stateBuilder) },
        screen = { TutorialScreen(store = it) }
    )

@Serializable
internal data class TutorialScreen(val stateBuilder: TutorialStateBuilder = TutorialStateBuilder.Default(newGame = false)): Screen
