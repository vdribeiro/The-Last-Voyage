package com.hybris.tlv.ui.screen

import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.domain.usecase.ship.model.Ship
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.catastrophe.CatastropheStore
import com.hybris.tlv.ui.screen.cheat.CheatStore
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.help.HelpStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.ui.screen.tutorial.TutorialStore

internal class StoreFactory(
    private val config: ConfigManager,
    private val useCases: UseCases
) {

    fun getSplashStore(reset: Boolean): SplashStore =
        SplashStore(
            reset = reset,
            config = config,
            syncUseCases = useCases.sync
        )

    fun getCheatStore(): CheatStore =
        CheatStore(config = config)

    fun getMainMenuStore(): MainMenuStore =
        MainMenuStore(
            config = config,
            gameSessionUseCases = useCases.gameSession,
        )

    fun getHelpStore(): HelpStore =
        HelpStore(config = config)

    fun getFeedbackStore(tag: String?, message: String?): FeedbackStore =
        FeedbackStore(
            tag = tag,
            message = message,
        )

    fun getNewGameStore(): NewGameStore =
        NewGameStore(
            shipUseCases = useCases.ship,
            gameSessionUseCases = useCases.gameSession
        )

    fun getCatastropheStore(): CatastropheStore =
        CatastropheStore(
            catastropheUseCases = useCases.catastrophe,
        )

    fun getTutorialStore(newGame: Boolean): TutorialStore =
        TutorialStore(
            newGame = newGame,
            config = config
        )

    fun getGameStore(ship: Ship?): GameStore =
        GameStore(
            ship = ship,
            config = config,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            gameSessionUseCases = useCases.gameSession
        )

    fun getEventStore(ship: Ship?): EventStore =
        EventStore(
            ship = ship,
            eventUseCases = useCases.event,
            gameSessionUseCases = useCases.gameSession,
        )

    fun getGameOverStore(): GameOverStore =
        GameOverStore(
            gameSessionUseCases = useCases.gameSession,
            achievementUseCases = useCases.achievement
        )

    fun getStellarExplorerStore(): StellarExplorerStore =
        StellarExplorerStore(spaceUseCases = useCases.space)

    fun getScoreStore(): ScoreStore =
        ScoreStore(gameSessionUseCases = useCases.gameSession)

    fun getAchievementStore(): AchievementStore =
        AchievementStore(achievementUseCases = useCases.achievement)

    fun getCreditStore(): CreditStore =
        CreditStore(creditUseCases = useCases.credit)
}