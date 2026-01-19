package com.hybris.tlv.ui.theme.component.card

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.ui.translation.TranslationCache

@Composable
internal fun SelectableAttribute(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    name: String? = null,
    description: String? = null,
    velocity: Double? = null,
    fuel: Double? = null,
    points: Int? = null,
) {
    val engineSpeedTranslation = getTranslation(key = "new_game_screen__engine_speed")
    val engineFuelTranslation = getTranslation(key = "new_game_screen__engine_fuel")

    val typography = LocalTypography.current

    Card(
        modifier = modifier,
        selected = selected
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                name?.let {
                    Text(
                        text = getTranslation(key = name),
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (selected && description != null) {
                    Spacer(modifier = Modifier.height(height = 4.dp))
                    Text(text = getTranslation(key = description), style = typography.bodyLarge)
                }
                velocity?.let { InfoRow(label = engineSpeedTranslation, value = "${it}c") }
                fuel?.let { InfoRow(label = engineFuelTranslation, value = it) }
            }
            Spacer(modifier = Modifier.weight(weight = 0.1f))
            Text(
                text = points?.toString(),
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
private fun SelectableCardPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "new_game_screen__engine_speed",
                value = "Speed"
            ),
            Translation(
                key = "new_game_screen__engine_fuel",
                value = "Fuel"
            ),
        )
    )
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        SelectableAttribute(
            selected = true,
            name = "Property",
            description = "Hammer Time",
            velocity = 1000.0,
            fuel = 100.0,
            points = 10
        )
        SelectableAttribute(
            name = "Property",
            velocity = 1000.0,
            fuel = 100.0,
            points = 10
        )
        SelectableAttribute(
            name = "Property",
        )
        SelectableAttribute(
            velocity = 1000.0,
            fuel = 100.0,
            points = 10
        )
        SelectableAttribute(
            velocity = 1000.0,
        )
        SelectableAttribute(
            points = 10
        )
    }
}
