package com.hybris.tlv.ui.screen.cheat

internal sealed interface CheatAction {
    data object ToggleIntegrity: CheatAction
    data object ToggleSensorRange: CheatAction
    data object ToggleFuel: CheatAction
    data object ToggleMaterials: CheatAction
    data object ToggleCryopods: CheatAction
}

internal data class CheatState(
    val loading: Boolean = true,
    val integrity: Boolean = false,
    val sensorRange: Boolean = false,
    val fuel: Boolean = false,
    val materials: Boolean = false,
    val cryopods: Boolean = false
)
