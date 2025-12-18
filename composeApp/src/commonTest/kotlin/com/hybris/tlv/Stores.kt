package com.hybris.tlv

import com.hybris.tlv.screen.Store
import com.hybris.tlv.screen.achievement.AchievementStore
import com.hybris.tlv.screen.cheat.CheatStore
import com.hybris.tlv.screen.credit.CreditStore
import com.hybris.tlv.screen.event.EventStore
import com.hybris.tlv.screen.feedback.FeedbackStore
import com.hybris.tlv.screen.game.GameStore
import com.hybris.tlv.screen.gameover.GameOverStore
import com.hybris.tlv.screen.help.HelpStore
import com.hybris.tlv.screen.mainmenu.MainMenuStore
import com.hybris.tlv.screen.newgame.NewGameStore
import com.hybris.tlv.screen.score.ScoreStore
import com.hybris.tlv.screen.splash.SplashStore
import com.hybris.tlv.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.screen.tutorial.TutorialStore
import com.hybris.tlv.usecase.ship.model.Ship

internal fun <State, Action> Store<State, Action>.state(): State = stateFlow.value

internal fun getSplashStore(): SplashStore =
    SplashStore(
        config = dependency.config,
        syncUseCases = dependency.useCases.sync
    )

internal fun getCheatStore(): CheatStore =
    CheatStore(config = dependency.config)

internal fun getMainMenuStore(): MainMenuStore =
    MainMenuStore(
        config = dependency.config,
        gameSessionUseCases = useCases.gameSession,
    )

internal fun getHelpStore(): HelpStore =
    HelpStore(
        config = dependency.config,
        syncUseCases = dependency.useCases.sync
    )

internal fun getFeedbackStore(tag: String? = null, message: String? = null) =
    FeedbackStore(
        tag = tag,
        message = message,
    )

internal fun getNewGameStore(): NewGameStore =
    NewGameStore(
        shipUseCases = useCases.ship,
        catastropheUseCases = useCases.catastrophe,
        gameSessionUseCases = useCases.gameSession
    )

internal fun getTutorialStore(newGame: Boolean = false): TutorialStore =
    TutorialStore(
        newGame = newGame,
        config = dependency.config
    )

internal fun getGameStore(ship: Ship? = com.hybris.tlv.ship): GameStore =
    GameStore(
        ship = ship,
        config = dependency.config,
        shipUseCases = useCases.ship,
        spaceUseCases = useCases.space,
        gameSessionUseCases = useCases.gameSession
    )

internal fun getEventStore(ship: Ship? = com.hybris.tlv.ship): EventStore =
    EventStore(
        ship = ship,
        eventUseCases = useCases.event,
        gameSessionUseCases = useCases.gameSession,
    )

internal fun getGameOverStore(): GameOverStore =
    GameOverStore(
        gameSessionUseCases = useCases.gameSession,
        achievementUseCases = useCases.achievement
    )

internal fun getStellarExplorerStore(): StellarExplorerStore =
    StellarExplorerStore(spaceUseCases = useCases.space)

internal fun getScoreStore(): ScoreStore =
    ScoreStore(gameSessionUseCases = useCases.gameSession)

internal fun getAchievementStore(): AchievementStore =
    AchievementStore(achievementUseCases = useCases.achievement)

internal fun getCreditStore(): CreditStore =
    CreditStore(creditUseCases = useCases.credit)
