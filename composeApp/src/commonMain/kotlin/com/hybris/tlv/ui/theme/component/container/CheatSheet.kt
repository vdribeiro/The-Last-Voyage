package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.Toggle
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.translation.TranslationCache
import com.hybris.tlv.ui.translation.getTranslation

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
    val titleTranslation = getTranslation(key = "cheats_screen__title")
    val integrityTranslation = getTranslation(key = "cheats_screen__integrity")
    val sensorRangeTranslation = getTranslation(key = "cheats_screen__sensor_range")
    val fuelTranslation = getTranslation(key = "cheats_screen__fuel")
    val materialsTranslation = getTranslation(key = "cheats_screen__materials")
    val cryopodsTranslation = getTranslation(key = "cheats_screen__cryopods")

    val typography = LocalTypography.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            text = titleTranslation,
            style = typography.headlineMedium,
        )
        LazyColumn(
            modifier = Modifier
                .testTag(tag = "cheat_list")
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            item {
                Toggle(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = integrityTranslation,
                    checked = integrity,
                    onCheckedChange = { onIntegrityClick() }
                )
            }
            item {
                Toggle(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = sensorRangeTranslation,
                    checked = sensorRange,
                    onCheckedChange = { onSensorRangeClick() }
                )
            }
            item {
                Toggle(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = fuelTranslation,
                    checked = fuel,
                    onCheckedChange = { onFuelClick() }
                )
            }
            item {
                Toggle(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = materialsTranslation,
                    checked = materials,
                    onCheckedChange = { onMaterialsClick() }
                )
            }
            item {
                Toggle(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
