package com.hybris.tlv.ui.screen.tutorial

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.bottombar.GameNavigationBar
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.text.TitleDescription
import com.hybris.tlv.ui.theme.component.topbar.StatusBar
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun TutorialScreen(store: Store<TutorialState, TutorialAction>) {
    val storeState by store.stateFlow.collectAsState()
    val ship = storeState.ship

    Screen(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { store.send(action = TutorialAction.Next) }
            .semantics(mergeDescendants = false) {},
        onBackClick = { store.back() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp
                ),
                hull = ship.integrity.toString(),
                fuel = ship.fuel.toString(),
                materials = ship.materials.toString(),
                cryopods = ship.cryopods.toString()
            )
        },
        bottomBar = {
            GameNavigationBar(
                shipEnabled = false,
                shipSelected = storeState.tutorialStep == Tutorial.SHIP,
                systemEnabled = false,
                systemSelected = storeState.tutorialStep == Tutorial.SYSTEM,
                travelEnabled = false,
                travelSelected = storeState.tutorialStep == Tutorial.TRAVEL,
            )
        }
    ) {
        val title: String
        val description: String
        when (storeState.tutorialStep) {
            Tutorial.GOAL -> {
                title = getTranslation(key = "tutorial_screen__mechanics_goal_title")
                description = getTranslation(key = "tutorial_screen__mechanics_goal_description")
            }

            Tutorial.SHIP -> {
                title = getTranslation(key = "tutorial_screen__mechanics_attributes_title")
                description = getTranslation(key = "tutorial_screen__mechanics_attributes_description")
            }

            Tutorial.SYSTEM -> {
                title = getTranslation(key = "tutorial_screen__mechanics_system_title")
                description = getTranslation(key = "tutorial_screen__mechanics_system_description")
            }

            Tutorial.TRAVEL -> {
                title = getTranslation(key = "tutorial_screen__mechanics_travel_title")
                description = getTranslation(key = "tutorial_screen__mechanics_travel_description")
            }

            Tutorial.GAME_OVER -> {
                title = getTranslation(key = "tutorial_screen__mechanics_game_over_title")
                description = getTranslation(key = "tutorial_screen__mechanics_game_over_description")
            }
        }
        TitleDescription(
            title = title,
            description = description,
        )
    }
}

@Preview
@Composable
private fun TutorialScreenGoalPreview() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "tutorial_screen__mechanics_goal_title",
                value = "Goal"
            ),
            Translation(
                key = "tutorial_screen__mechanics_goal_description",
                value = "Win the game!"
            ),
        )
    )
    AppTheme {
        TutorialScreen(
            store = Store(
                initialState = TutorialState(
                    tutorialStep = Tutorial.GOAL,
                )
            )
        )
    }
}

@Preview
@Composable
private fun TutorialScreenShipPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "tutorial_screen__mechanics_attributes_title",
                value = "Ship"
            ),
            Translation(
                key = "tutorial_screen__mechanics_attributes_description",
                value = "Your ship is awesome!"
            ),
        )
    )
    TutorialScreen(
        store = Store(
            initialState = TutorialState(
                tutorialStep = Tutorial.SHIP,
            )
        )
    )
}

@Preview
@Composable
private fun TutorialScreenSystemPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "tutorial_screen__mechanics_system_title",
                value = "System"
            ),
            Translation(
                key = "tutorial_screen__mechanics_system_description",
                value = "There are so many!"
            ),
        )
    )
    TutorialScreen(
        store = Store(
            initialState = TutorialState(
                tutorialStep = Tutorial.SYSTEM,
            )
        )
    )
}

@Preview
@Composable
private fun TutorialScreenTravelPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "tutorial_screen__mechanics_travel_title",
                value = "Travel"
            ),
            Translation(
                key = "tutorial_screen__mechanics_travel_description",
                value = "Warp speed, Captain."
            ),
        )
    )
    TutorialScreen(
        store = Store(
            initialState = TutorialState(
                tutorialStep = Tutorial.TRAVEL,
            )
        )
    )
}

@Preview
@Composable
private fun TutorialScreenGameOverPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "tutorial_screen__mechanics_game_over_title",
                value = "Game Over"
            ),
            Translation(
                key = "tutorial_screen__mechanics_game_over_description",
                value = "Game over man! Game over!"
            ),
        )
    )
    TutorialScreen(
        store = Store(
            initialState = TutorialState(
                tutorialStep = Tutorial.GAME_OVER,
            )
        )
    )
}
