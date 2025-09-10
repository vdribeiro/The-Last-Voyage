package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.usecase.achievement.model.Achievement

internal sealed interface AchievementAction

internal data class AchievementState(
    val achievements: List<Achievement>? = null
)
