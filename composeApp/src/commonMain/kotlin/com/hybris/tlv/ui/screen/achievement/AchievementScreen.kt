package com.hybris.tlv.ui.screen.achievement

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.model.Precondition
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun AchievementScreen(store: com.hybris.tlv.ui.screen.Store<AchievementState, Unit>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    _root_ide_package_.com.hybris.tlv.ui.screen.Screen(
        store = store,
        loading = storeState.loading,
    ) {
        _root_ide_package_.com.hybris.tlv.ui.theme.component.list.AchievementList(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            achievements = storeState.achievements,
            id = { it.id },
            description = { it.description },
            done = { it.done }
        )
    }
}

@Preview
@Composable
private fun AchievementScreenLoadingPreview() = _root_ide_package_.com.hybris.tlv.ui.theme.AppTheme {
    AchievementScreen(
        store = _root_ide_package_.com.hybris.tlv.ui.screen.Store(
            initialState = AchievementState(
                loading = true,
                achievements = emptyList()
            )
        )
    )
}

@Preview
@Composable
private fun AchievementScreenPreview() = _root_ide_package_.com.hybris.tlv.ui.theme.AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "achievements_screen__title",
                value = "Achievements"
            ),
        )
    )
    AchievementScreen(
        store = _root_ide_package_.com.hybris.tlv.ui.screen.Store(
            initialState = AchievementState(
                loading = false,
                achievements = listOf(
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
