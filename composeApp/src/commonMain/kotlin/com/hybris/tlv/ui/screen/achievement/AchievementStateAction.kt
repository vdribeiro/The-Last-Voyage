package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.usecase.achievement.model.Achievement

internal sealed interface AchievementStateBuilder {
    data object Default: AchievementStateBuilder
    data class FromSavableState(val state: AchievementState): AchievementStateBuilder
}

internal data class AchievementState(
    val loading: Boolean = true,
    val achievements: List<Achievement> = emptyList()
)
