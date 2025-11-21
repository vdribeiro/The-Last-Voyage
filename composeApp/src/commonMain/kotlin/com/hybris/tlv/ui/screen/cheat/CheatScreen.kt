package com.hybris.tlv.ui.screen.cheat

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.list.CheatSheet

@Composable
internal fun CheatScreen(store: Store<CheatState, CheatAction>) {
    val storeState by store.stateFlow.collectAsState()

    Screen(
        loading = storeState.loading,
        onBackClick = { store.back() },
    ) {
        CheatSheet(
            integrity = storeState.integrity,
            onIntegrityClick = { store.send(action = CheatAction.ToggleIntegrity) },
            sensorRange = storeState.sensorRange,
            onSensorRangeClick = { store.send(action = CheatAction.ToggleSensorRange) },
            fuel = storeState.fuel,
            onFuelClick = { store.send(action = CheatAction.ToggleFuel) },
            materials = storeState.materials,
            onMaterialsClick = { store.send(action = CheatAction.ToggleMaterials) },
            cryopods = storeState.cryopods,
            onCryopodsClick = { store.send(action = CheatAction.ToggleCryopods) }
        )
    }
}

@Preview
@Composable
private fun CheatLoadingPreview() = AppTheme {
    CheatScreen(
        store = Store(
            initialState = CheatState(
                loading = true,
            )
        )
    )
}

@Preview
@Composable
private fun CheatListPreview() = AppTheme {
    CheatScreen(
        store = Store(
            initialState = CheatState(
                loading = false,
                sensorRange = true,
                fuel = true,
                materials = false,
                cryopods = true,
            )
        )
    )
}
