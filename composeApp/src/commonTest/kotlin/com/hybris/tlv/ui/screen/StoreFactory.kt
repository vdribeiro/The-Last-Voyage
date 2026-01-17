package com.hybris.tlv.ui.screen

import com.hybris.tlv.Dependency
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
import com.hybris.tlv.usecase.ship.model.Ship

internal class StoreFactory(private val dependency: Dependency) {

    fun getSplashStore(reset: Boolean): SplashStore =
        SplashStore(
            reset = reset,
            config = dependency.config,
            syncUseCases = dependency.useCases.sync
        )

    fun getCheatStore(): CheatStore =
        CheatStore(config = dependency.config)

    fun getMainMenuStore(): MainMenuStore =
        MainMenuStore(
            config = dependency.config,
            gameSessionUseCases = dependency.useCases.gameSession,
        )

    fun getHelpStore(): HelpStore =
        HelpStore(config = dependency.config)

    fun getFeedbackStore(tag: String?, message: String?): FeedbackStore =
        FeedbackStore(
            tag = tag,
            message = message,
        )

    fun getNewGameStore(): NewGameStore =
        NewGameStore(
            shipUseCases = dependency.useCases.ship,
            gameSessionUseCases = dependency.useCases.gameSession
        )

    fun getCatastropheStore(): CatastropheStore =
        CatastropheStore(
            catastropheUseCases = dependency.useCases.catastrophe,
        )

    fun getTutorialStore(newGame: Boolean): TutorialStore =
        TutorialStore(
            newGame = newGame,
            config = dependency.config
        )

    fun getGameStore(ship: Ship?): GameStore =
        GameStore(
            ship = ship,
            config = dependency.config,
            shipUseCases = dependency.useCases.ship,
            spaceUseCases = dependency.useCases.space,
            gameSessionUseCases = dependency.useCases.gameSession
        )

    fun getEventStore(ship: Ship?): EventStore =
        EventStore(
            ship = ship,
            eventUseCases = dependency.useCases.event,
            gameSessionUseCases = dependency.useCases.gameSession,
        )

    fun getGameOverStore(): GameOverStore =
        GameOverStore(
            gameSessionUseCases = dependency.useCases.gameSession,
            achievementUseCases = dependency.useCases.achievement
        )

    fun getStellarExplorerStore(): StellarExplorerStore =
        StellarExplorerStore(spaceUseCases = dependency.useCases.space)

    fun getScoreStore(): ScoreStore =
        ScoreStore(gameSessionUseCases = dependency.useCases.gameSession)

    fun getAchievementStore(): AchievementStore =
        AchievementStore(achievementUseCases = dependency.useCases.achievement)

    fun getCreditStore(): CreditStore =
        CreditStore(creditUseCases = dependency.useCases.credit)
}