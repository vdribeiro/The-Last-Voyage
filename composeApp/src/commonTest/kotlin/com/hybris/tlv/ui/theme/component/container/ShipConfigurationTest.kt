package com.hybris.tlv.ui.theme.component.container

import kotlin.test.Test
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.test.TestCase

internal class ShipConfigurationTest: TestCase() {

    @Test
    fun points() = runUITest {
        setUI {
            ShipConfiguration<Pair<String, Int>, String>(
                remainingPoints = 10,
                attributes = persistentListOf(
                    "Sensor Range" to 3,
                    "Fuel" to 100,
                ),
                attributeName = { it.first },
                attributeValue = { it.second },
            )
        }

        onNodeWithText(text = "new_game_screen__ship_points: 10").assertIsDisplayed()
        onNodeWithText(text = "Sensor Range").assertIsDisplayed()
        onNodeWithText(text = "3").assertIsDisplayed()
        onNodeWithText(text = "Fuel").assertIsDisplayed()
        onNodeWithText(text = "100").assertIsDisplayed()
    }

    @Test
    fun engineSelection() = runUITest {
        val engines = persistentListOf("Nuclear", "Ion")
        val selectedEngineId = mutableStateOf(value = "Nuclear")

        setUI {
            ShipConfiguration<String, String>(
                selectedEngineId = selectedEngineId.value,
                engines = engines,
                engineId = { it },
                engineName = { it },
                engineDescription = { if (it == "Nuclear") "Nuclear go boom" else "Ion go blast" },
                engineVelocity = { if (it == "Nuclear") 0.3 else 0.1 },
                engineFuelConsumption = { if (it == "Nuclear") 1.2 else 2.1 },
                engineCost = { if (it == "Nuclear") 5 else 6 },
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
