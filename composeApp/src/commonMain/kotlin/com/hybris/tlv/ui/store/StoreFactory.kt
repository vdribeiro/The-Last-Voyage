package com.hybris.tlv.ui.store

import com.hybris.tlv.ui.screen.achievement.AchievementStateBuilder
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditStateBuilder
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.gameover.GameOverStateBuilder
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.score.ScoreStateBuilder
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStateBuilder
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.usecase.UseCases

/**
 * Store creator.
 */
internal class StoreFactory(
    private val useCases: UseCases
) {
    fun createGameOverStore(stateBuilder: Any? = null): GameOverStore = GameOverStore(
        stateBuilder = stateBuilder as? GameOverStateBuilder ?: GameOverStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession,
        achievementUseCases = useCases.achievement
    )

    fun createStellarExplorerStore(stateBuilder: Any? = null): StellarExplorerStore = StellarExplorerStore(
        stateBuilder = stateBuilder as? StellarExplorerStateBuilder ?: StellarExplorerStateBuilder.Default,
        spaceUseCases = useCases.space
    )

    fun createScoreStore(stateBuilder: Any? = null): ScoreStore = ScoreStore(
        stateBuilder = stateBuilder as? ScoreStateBuilder ?: ScoreStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession
    )

    fun createAchievementStore(stateBuilder: Any? = null): AchievementStore = AchievementStore(
        stateBuilder = stateBuilder as? AchievementStateBuilder ?: AchievementStateBuilder.Default,
        achievementUseCases = useCases.achievement
    )

    fun createCreditStore(stateBuilder: Any? = null): CreditStore = CreditStore(
        stateBuilder = stateBuilder as? CreditStateBuilder ?: CreditStateBuilder.Default,
        creditUseCases = useCases.credit
    )
}
