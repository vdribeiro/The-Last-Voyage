package com.hybris.tlv.screen.cheat

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.lifecycle.viewModelScope
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.screen.Store
import com.hybris.tlv.telemetry.Telemetry

internal class CheatStore(
    private val config: ConfigManager
): Store<CheatState, CheatAction>(
    initialState = CheatState()
) {
    init {
        setup()
    }

    private fun setup() {
        Telemetry.info(tag = TAG, message = "Setup")

        setInitialState()
        observeState()

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun setInitialState(): Job = launch(id = "setInitialState") {
        val preferences = config.preferences.value
        updateState {
            it.copy(
                loading = false,
                integrity = preferences.cheatIntegrity,
                sensorRange = preferences.cheatSensorRange,
                fuel = preferences.cheatFuel,
                materials = preferences.cheatMaterials,
                cryopods = preferences.cheatCryopods
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeState(): Job =
        stateFlow
            .filter { !it.loading } // Prevent overwriting Config with default values on startup
            .distinctUntilChanged() // Only trigger if the specific values actually change
            .onEach { state -> // Sequential saving to avoid cancellation data loss
                config.setPreferences {
                    it.copy(
                        cheatIntegrity = state.integrity,
                        cheatSensorRange = state.sensorRange,
                        cheatFuel = state.fuel,
                        cheatMaterials = state.materials,
                        cheatCryopods = state.cryopods
                    )
                }
                Telemetry.info(tag = TAG, message = "Cheats: $state")
            }
            .flowOn(context = Dispatcher.Default)
            .launchIn(scope = viewModelScope)

    override fun reducer(state: CheatState, action: CheatAction) {
        when (action) {
            CheatAction.ToggleIntegrity -> updateState { it.copy(integrity = !it.integrity) }
            CheatAction.ToggleSensorRange -> updateState { it.copy(sensorRange = !it.sensorRange) }
            CheatAction.ToggleFuel -> updateState { it.copy(fuel = !it.fuel) }
            CheatAction.ToggleMaterials -> updateState { it.copy(materials = !it.materials) }
            CheatAction.ToggleCryopods -> updateState { it.copy(cryopods = !it.cryopods) }
        }
    }

    companion object Companion {
        private const val TAG = "CheatStore"
    }
}
