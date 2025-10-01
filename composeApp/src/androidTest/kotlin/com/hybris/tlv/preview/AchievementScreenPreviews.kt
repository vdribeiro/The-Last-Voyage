package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.screen.achievement.AchievementScreen
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.model.Precondition

@Preview
@Composable
private fun AchievementLoading() {
    AppTheme {
        AchievementScreen(
            store = getStore(
                initialState = AchievementState(
                    loading = true,
                    achievements = emptyList()
                )
            )
        )
    }
}

@Preview
@Composable
private fun AchievementList() {
    AppTheme {
        AchievementScreen(
            store = getStore(
                initialState = AchievementState(
                    loading = false,
                    achievements = listOf(
                        Achievement(
                            id = "earth",
                            name = "Earth",
                            description = "Settle on Earth",
                            preconditions = Precondition(
                                settledPlanetId = "earth"
                            ),
                            status = false
                        )
                    )
                )
            )
        )
    }
}
