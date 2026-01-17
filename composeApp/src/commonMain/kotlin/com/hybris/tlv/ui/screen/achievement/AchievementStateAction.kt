package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.domain.usecase.achievement.model.Achievement

internal data class AchievementState(
    val loading: Boolean = true,
    val achievements: List<Achievement> = emptyList()
)
