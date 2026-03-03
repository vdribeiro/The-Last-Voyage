package com.hybris.tlv.ui.screen.achievement

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hybris.tlv.domain.usecase.achievement.model.Achievement

internal data class AchievementState(
    val loading: Boolean = true,
    val achievements: ImmutableList<Achievement> = persistentListOf()
)
