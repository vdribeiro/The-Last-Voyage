package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.usecase.achievement.model.Achievement

internal data class AchievementState(
    val achievements: List<Achievement> = emptyList()
)

internal sealed interface AchievementAction
