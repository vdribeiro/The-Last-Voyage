package com.hybris.tlv.ui.screen.achievement

import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.usecase.achievement.model.Achievement
import com.hybris.tlv.domain.usecase.achievement.model.Precondition
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.list.AchievementList

@Composable
internal fun AchievementScreen(store: Store<AchievementState, Unit>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        loading = storeState.loading,
    ) {
        AchievementList(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            achievements = storeState.achievements,
            id = Achievement::id,
            description = Achievement::description,
            done = Achievement::done
        )
    }
}

@Preview
@Composable
private fun AchievementScreenLoadingPreview() = Preview {
    AchievementScreen(
        store = Store(
            initialState = AchievementState(
                loading = true,
                achievements = persistentListOf()
            )
        )
    )
}

@Preview
@Composable
private fun AchievementScreenPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "achievements_screen__title",
                value = "Achievements"
            ),
        )
    )
    AchievementScreen(
        store = Store(
            initialState = AchievementState(
                loading = false,
                achievements = persistentListOf(
                    Achievement(
                        id = "Homecoming",
                        description = "Settle on Earth",
                        preconditions = Precondition(
                            settledPlanetId = "earth"
                        ),
                    ),
                    Achievement(
                        id = "The Martian",
                        description = "Settle on Mars",
                        preconditions = Precondition(
                            settledPlanetId = "mars"
                        ),
                    )
                )
            )
        )
    )
}
