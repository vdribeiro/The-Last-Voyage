package com.hybris.tlv.ui.screen.cheat

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class CheatStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getCheatStore()
        assertFalse(actual = store.state.loading)
        assertFalse(actual = store.state.integrity)
        assertFalse(actual = store.state.sensorRange)
        assertFalse(actual = store.state.fuel)
        assertFalse(actual = store.state.materials)
        assertFalse(actual = store.state.cryopods)
    }

    @Test
    fun enableAndDisableCheats() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getCheatStore()
        assertFalse(actual = store.state.loading)

        assertFalse(actual = store.state.integrity)
        store.send(action = CheatAction.ToggleIntegrity)
        assertTrue(actual = store.state.integrity)
        store.send(action = CheatAction.ToggleIntegrity)
        assertFalse(actual = store.state.integrity)

        assertFalse(actual = store.state.sensorRange)
        store.send(action = CheatAction.ToggleSensorRange)
        assertTrue(actual = store.state.sensorRange)
        store.send(action = CheatAction.ToggleSensorRange)
        assertFalse(actual = store.state.sensorRange)

        assertFalse(actual = store.state.fuel)
        store.send(action = CheatAction.ToggleFuel)
        assertTrue(actual = store.state.fuel)
        store.send(action = CheatAction.ToggleFuel)
        assertFalse(actual = store.state.fuel)

        assertFalse(actual = store.state.materials)
        store.send(action = CheatAction.ToggleMaterials)
        assertTrue(actual = store.state.materials)
        store.send(action = CheatAction.ToggleMaterials)
        assertFalse(actual = store.state.materials)

        assertFalse(actual = store.state.cryopods)
        store.send(action = CheatAction.ToggleCryopods)
        assertTrue(actual = store.state.cryopods)
        store.send(action = CheatAction.ToggleCryopods)
        assertFalse(actual = store.state.cryopods)
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Cheat)
        TestCase.assertNavigation(list = listOf(Screen.Cheat))
        TestCase.storeFactory.getCheatStore().back()
        TestCase.assertNavigation(list = emptyList())
    }
}
