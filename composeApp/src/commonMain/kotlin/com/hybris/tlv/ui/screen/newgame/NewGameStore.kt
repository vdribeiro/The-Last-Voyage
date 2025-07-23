package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.earth.EarthUseCases
import com.hybris.tlv.usecase.earth.model.Catastrophe
import com.hybris.tlv.usecase.exoplanet.model.Params
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.model.ShipPrototype

internal sealed interface NewGameAction {
    data object Back: NewGameAction
    data class SelectShip(val ship: ShipPrototype): NewGameAction
    data class SelectMath(val math: Params.Math): NewGameAction
    data object Ship: NewGameAction
    data object Advanced: NewGameAction
    data object Start: NewGameAction
    data object StartGame: NewGameAction
}

internal data class NewGameState(
    val currentContent: Content? = null,
    val catastrophes: List<Catastrophe> = emptyList(),
    val selectedCatastrophe: Catastrophe? = null,
    val selectedShip: ShipPrototype = ShipPrototype(
        assignedPoints = 4,
        sensorRange = 1,
        materials = 100,
        fuel = 100,
        cryopods = 100
    ),
    val math: Params.Math = Params.Math(),
)

internal enum class Content {
    SHIP,
    ADVANCED,
    START
}

internal class NewGameStore(
    dispatcher: Dispatcher,
    navigation: Navigation,
    initialState: NewGameState,
    private val earthUseCases: EarthUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<NewGameAction, NewGameState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {

    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val catastrophes = earthUseCases.getCatastrophes()
        updateState {
            it.copy(
                currentContent = Content.SHIP,
                catastrophes = catastrophes,
            )
        }
    }

    private fun startGame(state: NewGameState) = launchInPipeline {
        gameSessionUseCases.startGame(
            GameSessionPrototype(
                assignedPoints = state.selectedShip.assignedPoints,
                sensorRange = state.selectedShip.sensorRange,
                materials = state.selectedShip.materials,
                fuel = state.selectedShip.fuel,
                cryopods = state.selectedShip.cryopods,
                habitableZoneWeight = state.math.habitableZoneWeight,
                planetRadiusWeight = state.math.planetRadiusWeight,
                planetMassWeight = state.math.planetMassWeight,
                planetTelluricityWeight = state.math.planetTelluricityWeight,
                planetEccentricityWeight = state.math.planetEccentricityWeight,
                planetTemperatureWeight = state.math.planetTemperatureWeight,
                planetObliquityWeight = state.math.planetObliquityWeight,
                planetEsiWeight = state.math.planetEsiWeight,
                stellarSpectralTypeWeight = state.math.stellarSpectralTypeWeight,
                stellarMassWeight = state.math.stellarMassWeight,
                stellarAgeWeight = state.math.stellarAgeWeight,
                stellarActivityWeight = state.math.stellarActivityWeight,
                stellarRotationalPeriodWeight = state.math.stellarRotationalPeriodWeight,
                stellarGravityWeight = state.math.stellarGravityWeight,
                stellarMetallicityWeight = state.math.stellarMetallicityWeight,
                stellarEffectiveTemperatureWeight = state.math.stellarEffectiveTemperatureWeight,
                planetProtectionWeight = state.math.planetProtectionWeight,
                planetTidalLockingWeight = state.math.planetTidalLockingWeight,
                planetMassLowerLimit = state.math.planetMassLowerLimit,
                planetMassUpperLimit = state.math.planetMassUpperLimit,
                planetRadiusLowerLimit = state.math.planetRadiusLowerLimit,
                planetRadiusUpperLimit = state.math.planetRadiusUpperLimit,
                stellarHostEffectiveTemperatureMaxDeviation = state.math.stellarHostEffectiveTemperatureMaxDeviation
            )
        )
        navigate(screen = Navigation.Screen.GAME)
    }

    override fun reducer(state: NewGameState, action: NewGameAction) {
        when (action) {
            NewGameAction.Back -> {
                when (state.currentContent) {
                    null,
                    Content.SHIP,
                    Content.ADVANCED,
                    Content.START -> navigate(screen = Navigation.Screen.MAIN_MENU)
                }
            }

            is NewGameAction.SelectShip -> updateState {
                it.copy(selectedShip = action.ship)
            }

            is NewGameAction.SelectMath -> updateState {
                it.copy(math = action.math)
            }

            NewGameAction.Ship -> updateState {
                it.copy(currentContent = Content.SHIP)
            }

            NewGameAction.Advanced -> updateState {
                it.copy(currentContent = Content.ADVANCED)
            }

            NewGameAction.Start -> updateState {
                it.copy(
                    currentContent = Content.START,
                    selectedCatastrophe = state.catastrophes.random(),
                )
            }

            NewGameAction.StartGame -> startGame(state = state)
        }
    }

    companion object {
        private const val TAG = "NewGameStore"
    }
}
