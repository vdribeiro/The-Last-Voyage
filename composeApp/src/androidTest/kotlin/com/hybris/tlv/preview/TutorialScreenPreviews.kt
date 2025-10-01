package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.getStore
import com.hybris.tlv.ui.screen.tutorial.Tutorial
import com.hybris.tlv.ui.screen.tutorial.TutorialScreen
import com.hybris.tlv.ui.screen.tutorial.TutorialState
import com.hybris.tlv.ui.theme.AppTheme

@Preview
@Composable
private fun TutorialGoal() {
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
private fun TutorialShip() {
    AppTheme {
        TutorialScreen(
            store = getStore(
                initialState = TutorialState(
                    tutorialStep = Tutorial.SHIP,
                )
            )
        )
    }
}

@Preview
@Composable
private fun TutorialSystem() {
    AppTheme {
        TutorialScreen(
            store = getStore(
                initialState = TutorialState(
                    tutorialStep = Tutorial.SYSTEM,
                )
            )
        )
    }
}

@Preview
@Composable
private fun TutorialTravel() {
    AppTheme {
        TutorialScreen(
            store = getStore(
                initialState = TutorialState(
                    tutorialStep = Tutorial.TRAVEL,
                )
            )
        )
    }
}

@Preview
@Composable
private fun TutorialGameOver() {
    AppTheme {
        TutorialScreen(
            store = getStore(
                initialState = TutorialState(
                    tutorialStep = Tutorial.GAME_OVER,
                )
            )
        )
    }
}