package com.hybris.tlv.ui.screen.cheat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.container.CheatSheet

@Composable
internal fun CheatScreen(store: Store<CheatState, CheatAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        store = store,
        loading = storeState.loading,
        onHelpClick = null,
        onMusicClick = null,
        onFeedbackClick = null
    ) {
        CheatSheet(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
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
private fun CheatScreenLoadingPreview() = Preview {
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
private fun CheatScreenPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "cheats_screen__title",
                value = "Cheats"
            ),
            Translation(
                key = "cheats_screen__integrity",
                value = "Integrity"
            ),
            Translation(
                key = "cheats_screen__sensor_range",
                value = "Sensor Range"
            ),
            Translation(
                key = "cheats_screen__fuel",
                value = "Fuel"
            ),
            Translation(
                key = "cheats_screen__materials",
                value = "Materials"
            ),
            Translation(
                key = "cheats_screen__cryopods",
                value = "Cryopods"
            ),
        )
    )
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
