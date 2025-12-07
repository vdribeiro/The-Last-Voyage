package com.hybris.tlv.ui.screen.achievement

import kotlinx.coroutines.Job
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases

internal class AchievementStore(
    private val achievementUseCases: AchievementUseCases
): Store<AchievementState, Unit>(
    initialState = AchievementState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")

        val achievements = achievementUseCases.getAchievements()
        updateState {
            it.copy(
                loading = false,
                achievements = achievements
            )
        }
    }

    companion object {
        private const val TAG = "AchievementStore"
    }
}
