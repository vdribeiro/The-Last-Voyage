package com.hybris.tlv.ui.screen.tutorial

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
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
                shipSelected = storeState.currentContent == Content.SHIP,
                systemEnabled = false,
                systemSelected = storeState.currentContent == Content.SYSTEM,
                travelEnabled = false,
                travelSelected = storeState.currentContent == Content.TRAVEL,
            )
        }
    ) {
        val title: String
        val description: String
        when (storeState.currentContent) {
            Content.GOAL -> {
                title = getTranslation(key = "tutorial_screen__mechanics_goal_title")
                description = getTranslation(key = "tutorial_screen__mechanics_goal_description")
            }

            Content.SHIP -> {
                title = getTranslation(key = "tutorial_screen__mechanics_attributes_title")
                description = getTranslation(key = "tutorial_screen__mechanics_attributes_description")
            }

            Content.SYSTEM -> {
                title = getTranslation(key = "tutorial_screen__mechanics_system_title")
                description = getTranslation(key = "tutorial_screen__mechanics_system_description")
            }

            Content.TRAVEL -> {
                title = getTranslation(key = "tutorial_screen__mechanics_travel_title")
                description = getTranslation(key = "tutorial_screen__mechanics_travel_description")
            }

            Content.GAME_OVER -> {
                title = getTranslation(key = "tutorial_screen__mechanics_game_over_title")
                description = getTranslation(key = "tutorial_screen__mechanics_game_over_description")
            }
        }
        TitleDescription(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 32.dp),
            title = title,
            description = description,
        )
    }
}

@Preview
@Composable
private fun TutorialScreenGoalPreview() = AppTheme {
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
    TutorialScreen(
        store = Store(
            initialState = TutorialState(
                currentContent = Content.GOAL,
            )
        )
    )
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
                currentContent = Content.SHIP,
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
                currentContent = Content.SYSTEM,
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
                currentContent = Content.TRAVEL,
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
                currentContent = Content.GAME_OVER,
            )
        )
    )
}
