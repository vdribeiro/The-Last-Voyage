package com.hybris.tlv.ui.screen.tutorial

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.bottombar.GameNavigation
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.component.topbar.StatusBar
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun TutorialScreen(store: Store<TutorialState, TutorialAction>) {
    val storeState by store.stateFlow.collectAsState()
    val ship = remember {
        Ship(
            id = "",
            engine = Engine(
                id = "",
                description = "",
                velocity = 0.1,
                fuelConsumption = 0.0,
                cost = 0
            ),
            assignedPoints = 0,
            yearsTraveled = 0.0,
            sensorRange = (1..5).random(),
            integrity = (50..100).random(),
            fuel = (50..1000).random(),
            materials = (50..1000).random(),
            cryopods = (50..1000).random(),
        )
    }

    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val typography = LocalTypography.current

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
                modifier = Modifier
                    .statusBarsPadding(),
                hull = ship.integrity.toString(),
                fuel = ship.fuel.toString(),
                materials = ship.materials.toString(),
                cryopods = ship.cryopods.toString()
            )
        },
        bottomBar = {
            GameNavigation(
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
                title = remember(key1 = translationVersion) { getTranslation(key = "tutorial_screen__mechanics_goal_title") }
                description = remember(key1 = translationVersion) { getTranslation(key = "tutorial_screen__mechanics_goal_description") }
            }

            Tutorial.SHIP -> {
                title = remember(key1 = translationVersion) { getTranslation(key = "tutorial_screen__mechanics_attributes_title") }
                description = remember(key1 = translationVersion) { getTranslation(key = "tutorial_screen__mechanics_attributes_description") }
            }

            Tutorial.SYSTEM -> {
                title = remember(key1 = translationVersion) { getTranslation(key = "tutorial_screen__mechanics_system_title") }
                description = remember(key1 = translationVersion) { getTranslation(key = "tutorial_screen__mechanics_system_description") }
            }

            Tutorial.TRAVEL -> {
                title = remember(key1 = translationVersion) { getTranslation(key = "tutorial_screen__mechanics_travel_title") }
                description = remember(key1 = translationVersion) { getTranslation(key = "tutorial_screen__mechanics_travel_description") }
            }

            Tutorial.GAME_OVER -> {
                title = remember(key1 = translationVersion) { getTranslation(key = "tutorial_screen__mechanics_game_over_title") }
                description = remember(key1 = translationVersion) { getTranslation(key = "tutorial_screen__mechanics_game_over_description") }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 32.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                style = typography.titleLarge,
                text = title,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(state = rememberScrollState())
                    .padding(all = 8.dp),
                style = typography.bodyLarge,
                text = description,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Preview
@Composable
private fun TutorialGoalPreview() {
    AppTheme {
        TutorialScreen(
            store = getStore(
                initialState = TutorialState(
                    tutorialStep = Tutorial.GOAL,
                )
            )
        )
    }
}

@Preview
@Composable
private fun TutorialShipPreview() = AppTheme {
    TutorialScreen(
        store = getStore(
            initialState = TutorialState(
                tutorialStep = Tutorial.SHIP,
            )
        )
    )
}

@Preview
@Composable
private fun TutorialSystemPreview() = AppTheme {
    TutorialScreen(
        store = getStore(
            initialState = TutorialState(
                tutorialStep = Tutorial.SYSTEM,
            )
        )
    )
}

@Preview
@Composable
private fun TutorialTravelPreview() = AppTheme {
    TutorialScreen(
        store = getStore(
            initialState = TutorialState(
                tutorialStep = Tutorial.TRAVEL,
            )
        )
    )
}

@Preview
@Composable
private fun TutorialGameOverPreview() = AppTheme {
    TutorialScreen(
        store = getStore(
            initialState = TutorialState(
                tutorialStep = Tutorial.GAME_OVER,
            )
        )
    )
}
