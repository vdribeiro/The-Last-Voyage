package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.achievements
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.usecase.translation.model.domain.Translation

@Preview
@Composable
private fun AchievementNull() {
    val navigation = navigation(
        screen = Screen.ACHIEVEMENT,
        stateBuilder = AchievementState()
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun AchievementList() {
    val navigation = navigation(
        screen = Screen.ACHIEVEMENT,
        stateBuilder = AchievementState(
            achievements = achievements
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

private val translations = listOf(
    Translation(
        key = "key",
        value = "value"
    ),
)
