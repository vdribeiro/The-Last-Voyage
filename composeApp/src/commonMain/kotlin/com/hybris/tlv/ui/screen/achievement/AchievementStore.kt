package com.hybris.tlv.ui.screen.achievement

import kotlinx.coroutines.Job
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases

internal class AchievementStore(
    config: ConfigManager,
    private val achievementUseCases: AchievementUseCases
): Store<AchievementState, Unit>(
    config = config,
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
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    companion object {
        private const val TAG = "AchievementStore"
    }
}
