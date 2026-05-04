package com.hybris.tlv.ui.screen.cheat

import kotlinx.coroutines.Job
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.ui.screen.Store

internal class CheatStore(
    private val config: ConfigManager
): Store<CheatState, CheatAction>(
    initialState = CheatState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val preferences = config.preferences
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

        stateFlow.observe(id = "filterCheats") { state ->
            config.setPreferences {
                it.copy(
                    cheatIntegrity = state.integrity,
                    cheatSensorRange = state.sensorRange,
                    cheatFuel = state.fuel,
                    cheatMaterials = state.materials,
                    cheatCryopods = state.cryopods
                )
            }.savePreferences()
            Telemetry.info(tag = TAG, message = "Cheats: $state")
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

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
