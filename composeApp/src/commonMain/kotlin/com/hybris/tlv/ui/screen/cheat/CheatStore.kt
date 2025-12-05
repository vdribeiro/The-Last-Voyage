package com.hybris.tlv.ui.screen.cheat

import kotlinx.coroutines.Job
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.store.Store

internal class CheatStore(
    private val config: ConfigManager
): Store<CheatState, CheatAction>(
    initialState = CheatState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val preferences = config.preferences.value
        val integrity = preferences.cheatIntegrity
        val sensorRange = preferences.cheatSensorRange
        val fuel = preferences.cheatFuel
        val materials = preferences.cheatMaterials
        val cryopods = preferences.cheatCryopods

        updateState {
            it.copy(
                loading = false,
                integrity = integrity,
                sensorRange = sensorRange,
                fuel = fuel,
                materials = materials,
                cryopods = cryopods
            )
        }
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    override fun reducer(state: CheatState, action: CheatAction) {
        launch {
            val state = when (action) {
                CheatAction.ToggleIntegrity -> updateState { it.copy(integrity = !it.integrity) }
                CheatAction.ToggleSensorRange -> updateState { it.copy(sensorRange = !it.sensorRange) }
                CheatAction.ToggleFuel -> updateState { it.copy(fuel = !it.fuel) }
                CheatAction.ToggleMaterials -> updateState { it.copy(materials = !it.materials) }
                CheatAction.ToggleCryopods -> updateState { it.copy(cryopods = !it.cryopods) }
            }
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
    }

    companion object Companion {
        private const val TAG = "CheatStore"
    }
}
