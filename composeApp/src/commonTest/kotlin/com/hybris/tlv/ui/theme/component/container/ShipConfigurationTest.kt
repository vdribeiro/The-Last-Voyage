package com.hybris.tlv.ui.theme.component.container

import kotlin.test.Test
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.theme.component.button.AttributePoint

@OptIn(ExperimentalTestApi::class)
internal class ShipConfigurationTest: TestCase() {

    @Test
    fun points() = runUITest {
        setUI {
            ShipConfiguration<String>(
                remainingPoints = 10,
                sensorRange = AttributePoint(max = 10, min = 1, interval = 1, initialValue = 3),
                materials = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 100),
                fuel = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 200),
                cryopods = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 300),
            )
        }

        onNodeWithText(text = "new_game_screen__ship_points: 10").assertIsDisplayed()
        onNodeWithText(text = "ship_sensor").assertIsDisplayed()
        onNodeWithText(text = "3").assertIsDisplayed()
        onNodeWithText(text = "ship_fuel").assertIsDisplayed()
        onNodeWithText(text = "100").assertIsDisplayed()
        onNodeWithText(text = "ship_materials").assertIsDisplayed()
        onNodeWithText(text = "200").assertIsDisplayed()
        onNodeWithText(text = "ship_cryopods").assertIsDisplayed()
        onNodeWithText(text = "300").assertIsDisplayed()
    }

    @Test
    fun engineSelection() = runUITest {
        val engines = listOf("Nuclear", "Ion")
        val selectedEngineId = mutableStateOf(value = "Nuclear")

        setUI {
            ShipConfiguration(
                selectedEngineId = selectedEngineId.value,
                engines = engines,
                id = { it },
                description = { if (it == "Nuclear") "Nuclear go boom" else "Ion go blast" },
                velocity = { if (it == "Nuclear") 0.3 else 0.1 },
                fuelConsumption = { if (it == "Nuclear") 1.2 else 2.1 },
                cost = { if (it == "Nuclear") 5 else 6 },
                onEngineClick = { selectedEngineId.value = it }
            )
        }

        onNodeWithText(text = "new_game_screen__engine_select").assertIsDisplayed()

        onNodeWithText(text = "Nuclear").assertIsDisplayed()
        onNodeWithText(text = "new_game_screen__engine_speed: 0.3c").assertIsDisplayed()
        onNodeWithText(text = "new_game_screen__engine_fuel: 1.2").assertIsDisplayed()
        onNodeWithText(text = "5").assertIsDisplayed()

        onNodeWithText(text = "Ion").assertIsDisplayed()
        onNodeWithText(text = "new_game_screen__engine_speed: 0.1c").assertIsDisplayed()
        onNodeWithText(text = "new_game_screen__engine_fuel: 2.1").assertIsDisplayed()
        onNodeWithText(text = "6").assertIsDisplayed()

        onNodeWithText(text = "Nuclear go boom").assertIsDisplayed()
        onNodeWithText(text = "Ion go blast").assertDoesNotExist()

        onNodeWithText(text = "Ion").performClick()

        onNodeWithText(text = "Nuclear go boom").assertDoesNotExist()
        onNodeWithText(text = "Ion go blast").assertIsDisplayed()
    }
}
