package com.hybris.tlv.ui.theme.component.list

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.Toggle
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun CheatSheet(
    modifier: Modifier = Modifier,
    integrity: Boolean = false,
    onIntegrityClick: () -> Unit = {},
    sensorRange: Boolean = false,
    onSensorRangeClick: () -> Unit = {},
    fuel: Boolean = false,
    onFuelClick: () -> Unit = {},
    materials: Boolean = false,
    onMaterialsClick: () -> Unit = {},
    cryopods: Boolean = false,
    onCryopodsClick: () -> Unit = {}
) {
    val translationVersion by TranslationCache.versionFlow.collectAsState()
    val titleTranslation = remember(key1 = translationVersion) { getTranslation(key = "cheats_screen__title") }
    val integrityTranslation = remember(key1 = translationVersion) { getTranslation(key = "cheats_screen__integrity") }
    val sensorRangeTranslation = remember(key1 = translationVersion) { getTranslation(key = "cheats_screen__sensor_range") }
    val fuelTranslation = remember(key1 = translationVersion) { getTranslation(key = "cheats_screen__fuel") }
    val materialsTranslation = remember(key1 = translationVersion) { getTranslation(key = "cheats_screen__materials") }
    val cryopodsTranslation = remember(key1 = translationVersion) { getTranslation(key = "cheats_screen__cryopods") }

    val typography = LocalTypography.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        Text(
            text = titleTranslation,
            style = typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            item {
                Toggle(
                    text = integrityTranslation,
                    checked = integrity,
                    onCheckedChange = { onIntegrityClick() }
                )
            }
            item {
                Toggle(
                    text = sensorRangeTranslation,
                    checked = sensorRange,
                    onCheckedChange = { onSensorRangeClick() }
                )
            }
            item {
                Toggle(
                    text = fuelTranslation,
                    checked = fuel,
                    onCheckedChange = { onFuelClick() }
                )
            }
            item {
                Toggle(
                    text = materialsTranslation,
                    checked = materials,
                    onCheckedChange = { onMaterialsClick() }
                )
            }
            item {
                Toggle(
                    text = cryopodsTranslation,
                    checked = cryopods,
                    onCheckedChange = { onCryopodsClick() }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CheatSheetPreview() = AppTheme {
    TranslationCache.set(
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
    CheatSheet(materials = true)
}
