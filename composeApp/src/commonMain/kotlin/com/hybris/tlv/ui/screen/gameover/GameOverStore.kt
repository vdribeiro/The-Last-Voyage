package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.error.ErrorState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface GameOverAction {
    data object Continue: GameOverAction
}

internal data class GameOverState(
    val currentContent: Content = Content.MESSAGE,
    val gameSession: GameSession? = null,
    val gameOverMessage: String? = null
)

internal enum class Content {
    MESSAGE,
    SCORE
}

internal class GameOverStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: GameOverState,
    private val gameSessionUseCases: GameSessionUseCases
): Store<GameOverAction, GameOverState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launch {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.GAME_OVER,
                    throwable = IllegalStateException("Invalid state: missing game session"),
                    identifier = "GameOverStore:setup"
                )
            )
            return@launch
        }

        val gameOver = gameSessionUseCases.getGameOver(gameSession = gameSession)
        val updatedGameSession = gameSessionUseCases.score(gameSession = gameSession, gameOver = gameOver)

        updateState {
            it.copy(
                gameSession = updatedGameSession.copy(utc = getLocalDateTime(utc = updatedGameSession.utc)),
                gameOverMessage = getGameOverMessage(gameOver = gameOver)
            )
        }
    }

    private fun getGameOverMessage(gameOver: GameOver): String =
        when (gameOver) {
            // Ship is destroyed
            GameOver.INTEGRITY_ZERO -> "game_over_screen__integrity_zero"
            GameOver.INTEGRITY_ZERO_YEARS_FEW -> "game_over_screen__integrity_zero_years_few"
            GameOver.INTEGRITY_ZERO_YEARS_SOME -> "game_over_screen__integrity_zero_years_some"
            GameOver.INTEGRITY_ZERO_YEARS_LOTS -> "game_over_screen__integrity_zero_years_lots"
            GameOver.INTEGRITY_ZERO_MATERIALS_ZERO -> "game_over_screen__integrity_zero_materials_zero"
            GameOver.INTEGRITY_ZERO_MATERIALS_LOW -> "game_over_screen__integrity_zero_materials_low"
            GameOver.INTEGRITY_ZERO_MATERIALS_ENOUGH -> "game_over_screen__integrity_zero_materials_enough"
            GameOver.INTEGRITY_ZERO_CRYOPODS_ZERO -> "game_over_screen__integrity_zero_cryopods_zero"
            GameOver.INTEGRITY_ZERO_CRYOPODS_ONE -> "game_over_screen__integrity_zero_cryopods_one"
            GameOver.INTEGRITY_ZERO_CRYOPODS_LOW -> "game_over_screen__integrity_zero_cryopods_low"
            GameOver.INTEGRITY_ZERO_CRYOPODS_ENOUGH -> "game_over_screen__integrity_zero_cryopods_enough"
            GameOver.INTEGRITY_ZERO_FUEL_LOW -> "game_over_screen__integrity_zero_fuel_low"
            GameOver.INTEGRITY_ZERO_FUEL_SOME -> "game_over_screen__integrity_zero_fuel_some"
            GameOver.INTEGRITY_ZERO_FUEL_PLENTY -> "game_over_screen__integrity_zero_fuel_plenty"
            GameOver.INTEGRITY_ZERO_YEARS_LOTS_CRYOPODS_BUSTLING -> "game_over_screen__integrity_zero_years_lots_cryopods_bustling"

            // Ship ran out of fuel
            GameOver.FUEL_ZERO -> "game_over_screen__fuel_zero"
            GameOver.FUEL_ZERO_YEARS_FEW -> "game_over_screen__fuel_zero_years_few"
            GameOver.FUEL_ZERO_YEARS_SOME -> "game_over_screen__fuel_zero_years_some"
            GameOver.FUEL_ZERO_YEARS_LOTS -> "game_over_screen__fuel_zero_years_lots"
            GameOver.FUEL_ZERO_MATERIALS_ZERO -> "game_over_screen__fuel_zero_materials_zero"
            GameOver.FUEL_ZERO_MATERIALS_LOW -> "game_over_screen__fuel_zero_materials_low"
            GameOver.FUEL_ZERO_MATERIALS_ENOUGH -> "game_over_screen__fuel_zero_materials_enough"
            GameOver.FUEL_ZERO_CRYOPODS_ZERO -> "game_over_screen__fuel_zero_cryopods_zero"
            GameOver.FUEL_ZERO_CRYOPODS_ONE -> "game_over_screen__fuel_zero_cryopods_one"
            GameOver.FUEL_ZERO_CRYOPODS_NEAR_ZERO -> "game_over_screen__fuel_zero_cryopods_near_zero"
            GameOver.FUEL_ZERO_CRYOPODS_TOO_LOW -> "game_over_screen__fuel_zero_cryopods_too_low"
            GameOver.FUEL_ZERO_CRYOPODS_LOW -> "game_over_screen__fuel_zero_cryopods_low"
            GameOver.FUEL_ZERO_CRYOPODS_ENOUGH -> "game_over_screen__fuel_zero_cryopods_enough"
            GameOver.FUEL_ZERO_INTEGRITY_LOW -> "game_over_screen__fuel_zero_integrity_low"
            GameOver.FUEL_ZERO_INTEGRITY_ENOUGH -> "game_over_screen__fuel_zero_integrity_enough"
            GameOver.FUEL_ZERO_INTEGRITY_PRISTINE -> "game_over_screen__fuel_zero_integrity_pristine"
            GameOver.FUEL_ZERO_MATERIALS_PLENTY_CRYOPODS_BUSTLING -> "game_over_screen__fuel_zero_materials_plenty_cryopods_bustling"
            GameOver.FUEL_ZERO_INTEGRITY_ENOUGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING -> "game_over_screen__fuel_zero_integrity_enough_materials_enough_cryopods_bustling"

            // Solar System Planets
            GameOver.MERCURY -> "game_over_screen__mercury"
            GameOver.VENUS -> "game_over_screen__venus"
            GameOver.EARTH -> "game_over_screen__earth"
            GameOver.MARS -> "game_over_screen__mars"
            GameOver.JUPITER -> "game_over_screen__jupiter"
            GameOver.SATURN -> "game_over_screen__saturn"
            GameOver.URANUS -> "game_over_screen__uranus"
            GameOver.NEPTUNE -> "game_over_screen__neptune"

            // Habitability: Deadly
            GameOver.HABITABILITY_DEADLY -> "game_over_screen__habitability_deadly"
            GameOver.HABITABILITY_DEADLY_CRYOPODS_ENOUGH -> "game_over_screen__habitability_deadly_cryopods_enough"
            GameOver.HABITABILITY_DEADLY_INTEGRITY_LOW -> "game_over_screen__habitability_deadly_integrity_low"
            GameOver.HABITABILITY_DEADLY_INTEGRITY_MID_LOW_MATERIALS_ENOUGH -> "game_over_screen__habitability_deadly_integrity_mid_low_materials_enough"

            // Habitability: Very Low
            GameOver.HABITABILITY_VERY_LOW -> "game_over_screen__habitability_very_low"
            GameOver.HABITABILITY_VERY_LOW_CRYOPODS_ENOUGH_MATERIALS_ENOUGH -> "game_over_screen__habitability_very_low_cryopods_enough_materials_enough"
            GameOver.HABITABILITY_VERY_LOW_CRYOPODS_MID_MATERIALS_ENOUGH -> "game_over_screen__habitability_very_low_cryopods_mid_materials_enough"
            GameOver.HABITABILITY_VERY_LOW_INTEGRITY_LOW -> "game_over_screen__habitability_very_low_integrity_low"

            // Habitability: Low
            GameOver.HABITABILITY_LOW -> "game_over_screen__habitability_low"
            GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH -> "game_over_screen__habitability_low_materials_enough_cryopods_enough"
            GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_INTEGRITY_PRISTINE -> "game_over_screen__habitability_low_materials_enough_cryopods_enough_integrity_pristine"
            GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_FUEL_PLENTY -> "game_over_screen__habitability_low_materials_enough_cryopods_enough_fuel_plenty"
            GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_LOW -> "game_over_screen__habitability_low_materials_enough_cryopods_low"
            GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ZERO -> "game_over_screen__habitability_low_materials_enough_cryopods_zero"
            GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ENOUGH -> "game_over_screen__habitability_low_materials_low_cryopods_enough"
            GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_LOW -> "game_over_screen__habitability_low_materials_low_cryopods_low"
            GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ZERO -> "game_over_screen__habitability_low_materials_low_cryopods_zero"

            // Habitability: Medium
            GameOver.HABITABILITY_MEDIUM -> "game_over_screen__habitability_medium"
            GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH -> "game_over_screen__habitability_medium_materials_enough_cryopods_enough"
            GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS -> "game_over_screen__habitability_medium_materials_enough_cryopods_enough_years_lots"
            GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_BUSTLING -> "game_over_screen__habitability_medium_materials_enough_cryopods_bustling"
            GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_LOW -> "game_over_screen__habitability_medium_materials_enough_cryopods_low"
            GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ZERO -> "game_over_screen__habitability_medium_materials_enough_cryopods_zero"
            GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH -> "game_over_screen__habitability_medium_materials_low_cryopods_enough_integrity_enough"
            GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH -> "game_over_screen__habitability_medium_materials_low_cryopods_enough"
            GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_LOW -> "game_over_screen__habitability_medium_materials_low_cryopods_low"
            GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ZERO -> "game_over_screen__habitability_medium_materials_low_cryopods_zero"

            // Habitability: High
            GameOver.HABITABILITY_HIGH -> "game_over_screen__habitability_high"
            GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH -> "game_over_screen__habitability_high_materials_enough_cryopods_enough"
            GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS -> "game_over_screen__habitability_high_materials_enough_cryopods_enough_years_lots"
            GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING -> "game_over_screen__habitability_high_materials_enough_cryopods_bustling"
            GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_LOW -> "game_over_screen__habitability_high_materials_enough_cryopods_low"
            GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ZERO -> "game_over_screen__habitability_high_materials_enough_cryopods_zero"
            GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH -> "game_over_screen__habitability_high_materials_low_cryopods_enough_integrity_enough"
            GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH -> "game_over_screen__habitability_high_materials_low_cryopods_enough"
            GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_LOW -> "game_over_screen__habitability_high_materials_low_cryopods_low"
            GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ZERO -> "game_over_screen__habitability_high_materials_low_cryopods_zero"

            // Default
            GameOver.GAME_OVER -> "game_over_screen__game_over"
        }

    override fun setBackNavigation(): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: GameOverState, action: GameOverAction) {
        when (action) {
            GameOverAction.Continue -> when (state.currentContent) {
                Content.MESSAGE -> updateState { it.copy(currentContent = Content.SCORE) }
                Content.SCORE -> navigate(screen = Screen.MAIN_MENU)
            }
        }
    }

    companion object {
        private const val TAG = "GameOverStore"
    }
}
