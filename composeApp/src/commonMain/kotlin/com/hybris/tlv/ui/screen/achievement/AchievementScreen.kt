package com.hybris.tlv.ui.screen.achievement

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.store.getStore
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.list.AchievementList
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.model.Precondition

@Composable
internal fun AchievementScreen(store: Store<AchievementState, Unit>) {
    val storeState by store.stateFlow.collectAsState()

    Screen(
        loading = storeState.loading,
        onBackClick = { store.back() },
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
    ) {
        AchievementList(
            achievements = storeState.achievements,
            id = { it.id },
            description = { it.description },
            done = { it.done }
        )
    }
}

@Preview
@Composable
private fun AchievementLoadingPreview() = AppTheme {
    AchievementScreen(
        store = getStore(
            initialState = AchievementState(
                loading = true,
                achievements = emptyList()
            )
        )
    )
}

@Preview
@Composable
private fun AchievementListPreview() = AppTheme {
    AchievementScreen(
        store = getStore(
            initialState = AchievementState(
                loading = false,
                achievements = listOf(
                    Achievement(
                        id = "earth",
                        description = "Settle on Earth",
                        preconditions = Precondition(
                            settledPlanetId = "earth"
                        ),
                    )
                )
            )
        )
    )
}
