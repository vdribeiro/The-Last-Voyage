package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface GameOverAction {
    data object Continue: GameOverAction
    data object Back: GameOverAction
}

internal data class GameOverState(
    val currentContent: Content? = null,
    val gameSession: GameSession? = null,
    val gameOverMessage: String? = null
)

internal enum class Content {
    MESSAGE,
    SCORE
}

internal class GameOverStore(
    dispatcher: Dispatcher,
    navigation: Navigation,
    initialState: GameOverState,
    private val locale: Locale,
    private val gameSessionUseCases: GameSessionUseCases
): Store<GameOverAction, GameOverState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(screen = Navigation.Screen.ERROR)
            return@launchInPipeline
        }

        val gameOver = getGameOver(gameSession = gameSession)

        // Base Score = (Cryopod Score) + (Resource Score) + (Journey Score)
        val cryopodScore = gameSession.cryopods * 100
        val resourceScore = gameSession.materials * 2 + gameSession.fuel * 1
        val journeyScore = gameSession.yearsTraveled * 5
        val baseScore = cryopodScore + resourceScore + journeyScore

        // Challenge Multiplier
        val challengeMultiplier = (1.0 + (15 - gameSession.assignedPoints) + 0.05).coerceIn(minimumValue = 0.01, maximumValue = 10.0)

        // Final Score = (Base Score) * Habitability Multiplier * Success Multiplier * Challenge Multiplier
        val score = baseScore * gameOver.second * challengeMultiplier

        val updatedGameSession = gameSession.copy(score = score)
        gameSessionUseCases.updateGameSession(gameSession = updatedGameSession)
        updateState {
            it.copy(
                currentContent = Content.MESSAGE,
                gameSession = updatedGameSession.copy(utc = locale.getLocalDateTime(utc = updatedGameSession.utc)),
                gameOverMessage = gameOver.first
            )
        }
    }

    private fun getGameOver(gameSession: GameSession): Pair<String, Double> {
        var habitabilityMultiplier = 0.25
        var successMultiplier = 0.25
        val message = when {
            gameSession.settledPlanetId == "1mercury" -> "game_over_screen__mercury"
            gameSession.settledPlanetId == "2venus" -> "game_over_screen__venus"
            gameSession.settledPlanetId == "3earth" -> "game_over_screen__earth"
            gameSession.settledPlanetId == "4mars" -> "game_over_screen__mars"
            gameSession.settledPlanetId == "5jupiter" -> "game_over_screen__jupiter"
            gameSession.settledPlanetId == "6saturn" -> "game_over_screen__saturn"
            gameSession.settledPlanetId == "7uranus" -> "game_over_screen__uranus"
            gameSession.settledPlanetId == "8neptune" -> "game_over_screen__neptune"

            gameSession.finalHabitability != null -> buildList {
                when (gameSession.finalHabitability) {
                    in 0.0..20.0 -> {
                        habitabilityMultiplier = 0.25
                        add("game_over_screen__habitability_deadly")
                        if (gameSession.cryopods >= 50) add("game_over_screen__habitability_deadly_cryopods_enough")
                        if (gameSession.integrity < 20) add("game_over_screen__habitability_deadly_integrity_low")
                        if (gameSession.materials >= 50 && gameSession.integrity < 30) add("game_over_screen__habitability_deadly_integrity_mid_low_materials_enough")
                    }

                    in 21.0..40.0 -> {
                        habitabilityMultiplier = 0.50
                        add("game_over_screen__habitability_very_low")
                        if (gameSession.cryopods >= 50 && gameSession.materials >= 50) add("game_over_screen__habitability_very_low_cryopods_enough_materials_enough")
                        if (gameSession.cryopods >= 100 && gameSession.materials >= 50) add("game_over_screen__habitability_very_low_cryopods_mid_materials_enough")
                        if (gameSession.integrity < 20) add("game_over_screen__habitability_very_low_integrity_low")
                    }

                    in 41.0..60.0 -> {
                        habitabilityMultiplier = 1.0
                        when {
                            gameSession.materials >= 300 && gameSession.cryopods >= 150 -> {
                                successMultiplier = 1.0
                                add("game_over_screen__habitability_low_materials_enough_cryopods_enough")
                                if (gameSession.integrity >= 90) add("game_over_screen__habitability_low_materials_enough_cryopods_enough_integrity_pristine")
                                if (gameSession.fuel >= 50) add("game_over_screen__habitability_low_materials_enough_cryopods_enough_fuel_plenty")
                            }

                            gameSession.materials >= 300 && gameSession.cryopods in 1..149 -> add("game_over_screen__habitability_low_materials_enough_cryopods_low")
                            gameSession.materials >= 300 && gameSession.cryopods < 1 -> add("game_over_screen__habitability_low_materials_enough_cryopods_zero")
                            gameSession.materials < 300 && gameSession.cryopods >= 150 -> add("game_over_screen__habitability_low_materials_low_cryopods_enough")
                            gameSession.materials < 300 && gameSession.cryopods in 1..149 -> add("game_over_screen__habitability_low_materials_low_cryopods_low")
                            gameSession.materials < 300 && gameSession.cryopods < 1 -> add("game_over_screen__habitability_low_materials_low_cryopods_zero")
                            else -> add("game_over_screen__habitability_low")
                        }
                    }

                    in 61.0..80.0 -> {
                        habitabilityMultiplier = 1.2
                        when {
                            gameSession.materials >= 100 && gameSession.cryopods >= 100 -> {
                                successMultiplier = 1.0
                                add("game_over_screen__habitability_medium_materials_enough_cryopods_enough")
                                if (gameSession.yearsTraveled > 5000.0) add("game_over_screen__habitability_medium_materials_enough_cryopods_enough_years_lots")
                                if (gameSession.cryopods >= 300) add("game_over_screen__habitability_medium_materials_enough_cryopods_bustling")
                            }

                            gameSession.materials >= 100 && gameSession.cryopods in 1..99 -> add("game_over_screen__habitability_medium_materials_enough_cryopods_low")
                            gameSession.materials >= 100 && gameSession.cryopods < 1 -> add("game_over_screen__habitability_medium_materials_enough_cryopods_zero")
                            gameSession.materials < 100 && gameSession.cryopods >= 100 -> when {
                                gameSession.integrity >= 75 -> {
                                    successMultiplier = 0.75
                                    add("game_over_screen__habitability_medium_materials_low_cryopods_enough_integrity_enough")
                                }

                                else -> {
                                    successMultiplier = 0.5
                                    add("game_over_screen__habitability_medium_materials_low_cryopods_enough")
                                }
                            }

                            gameSession.materials < 100 && gameSession.cryopods in 1..99 -> add("game_over_screen__habitability_medium_materials_low_cryopods_low")
                            gameSession.materials < 100 && gameSession.cryopods < 1 -> add("game_over_screen__habitability_medium_materials_low_cryopods_zero")
                            else -> add("game_over_screen__habitability_medium")
                        }
                    }

                    else -> {
                        habitabilityMultiplier = 1.5
                        when {
                            gameSession.materials >= 50 && gameSession.cryopods >= 50 -> {
                                successMultiplier = 1.0
                                add("game_over_screen__habitability_high_materials_enough_cryopods_enough")
                                if (gameSession.yearsTraveled > 5000.0) add("game_over_screen__habitability_high_materials_enough_cryopods_enough_years_lots")
                                if (gameSession.cryopods >= 300) add("game_over_screen__habitability_high_materials_enough_cryopods_bustling")
                            }

                            gameSession.materials >= 50 && gameSession.cryopods in 1..49 -> add("game_over_screen__habitability_high_materials_enough_cryopods_low")
                            gameSession.materials >= 50 && gameSession.cryopods < 1 -> add("game_over_screen__habitability_high_materials_enough_cryopods_zero")
                            gameSession.materials < 50 && gameSession.cryopods >= 50 -> when {
                                gameSession.integrity >= 50 -> {
                                    successMultiplier = 0.75
                                    add("game_over_screen__habitability_high_materials_low_cryopods_enough_integrity_enough")
                                }

                                else -> {
                                    successMultiplier = 0.5
                                    add("game_over_screen__habitability_high_materials_low_cryopods_enough")
                                }
                            }

                            gameSession.materials < 50 && gameSession.cryopods in 1..49 -> add("game_over_screen__habitability_high_materials_low_cryopods_low")
                            gameSession.materials < 50 && gameSession.cryopods < 1 -> add("game_over_screen__habitability_high_materials_low_cryopods_zero")
                            else -> add("game_over_screen__habitability_high")
                        }
                    }
                }
            }.random()

            gameSession.integrity <= 0 -> buildList {
                add("game_over_screen__integrity_zero")

                if (gameSession.yearsTraveled < 1000.0) add("game_over_screen__integrity_zero_years_few")
                if (gameSession.yearsTraveled in 1000.0..5000.0) add("game_over_screen__integrity_zero_years_some")
                if (gameSession.yearsTraveled > 5000.0) add("game_over_screen__integrity_zero_years_lots")

                if (gameSession.materials < 1) add("game_over_screen__integrity_zero_materials_zero")
                if (gameSession.materials in 1..20) add("game_over_screen__integrity_zero_materials_low")
                if (gameSession.materials > 20) add("game_over_screen__integrity_zero_materials_enough")

                if (gameSession.cryopods < 1) add("game_over_screen__integrity_zero_cryopods_zero")
                if (gameSession.cryopods == 1) add("game_over_screen__integrity_zero_cryopods_one")
                if (gameSession.cryopods in 2..20) add("game_over_screen__integrity_zero_cryopods_low")
                if (gameSession.cryopods > 20) add("game_over_screen__integrity_zero_cryopods_enough")

                if (gameSession.fuel < 10) add("game_over_screen__integrity_zero_fuel_low")
                if (gameSession.fuel in 10..90) add("game_over_screen__integrity_zero_fuel_some")
                if (gameSession.fuel > 90) add("game_over_screen__integrity_zero_fuel_plenty")

                if (gameSession.yearsTraveled >= 2000.0 && gameSession.cryopods >= 300) add("game_over_screen__integrity_zero_years_lots_cryopods_bustling")
            }.random()

            gameSession.fuel <= 0 -> buildList {
                add("game_over_screen__fuel_zero")

                if (gameSession.yearsTraveled < 1000.0) add("game_over_screen__fuel_zero_years_few")
                if (gameSession.yearsTraveled in 1000.0..5000.0) add("game_over_screen__fuel_zero_years_some")
                if (gameSession.yearsTraveled > 5000.0) add("game_over_screen__fuel_zero_years_lots")

                if (gameSession.materials < 1) add("game_over_screen__fuel_zero_materials_zero")
                if (gameSession.materials in 1..20) add("game_over_screen__fuel_zero_materials_low")
                if (gameSession.materials >= 20) add("game_over_screen__fuel_zero_materials_enough")

                if (gameSession.cryopods < 1) add("game_over_screen__fuel_zero_cryopods_zero")
                if (gameSession.cryopods == 1) add("game_over_screen__fuel_zero_cryopods_one")
                if (gameSession.cryopods in 2..10) add("game_over_screen__fuel_zero_cryopods_near_zero")
                if (gameSession.cryopods in 11..20) add("game_over_screen__fuel_zero_cryopods_too_low")
                if (gameSession.cryopods in 21..50) add("game_over_screen__fuel_zero_cryopods_low")
                if (gameSession.cryopods > 50) add("game_over_screen__fuel_zero_cryopods_enough")

                if (gameSession.integrity < 20) add("game_over_screen__fuel_zero_integrity_low")
                if (gameSession.integrity in 20..90) add("game_over_screen__fuel_zero_integrity_enough")
                if (gameSession.integrity > 90) add("game_over_screen__fuel_zero_integrity_pristine")

                if (gameSession.materials >= 100 && gameSession.cryopods >= 300) add("game_over_screen__fuel_zero_materials_plenty_cryopods_bustling")
                if (gameSession.integrity >= 90 && gameSession.materials >= 100 && gameSession.cryopods >= 300) add("game_over_screen__fuel_zero_integrity_enough_materials_enough_cryopods_bustling")
            }.random()

            else -> "game_over_screen__game_over"
        }
        return Pair(first = message, second = habitabilityMultiplier * successMultiplier)
    }

    override fun reducer(state: GameOverState, action: GameOverAction) {
        when (action) {
            GameOverAction.Continue -> when (state.currentContent) {
                Content.MESSAGE -> updateState { it.copy(currentContent = Content.SCORE) }
                Content.SCORE -> navigate(screen = Navigation.Screen.MAIN_MENU)
                else -> navigate(screen = Navigation.Screen.ERROR)
            }

            GameOverAction.Back -> navigate(screen = Navigation.Screen.MAIN_MENU)
        }
    }

    companion object {
        private const val TAG = "GameOverStore"
    }
}
