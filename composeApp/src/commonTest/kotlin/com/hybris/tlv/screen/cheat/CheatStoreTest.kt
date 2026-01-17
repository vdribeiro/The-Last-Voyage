package com.hybris.tlv.screen.cheat

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.navigation.Screen

internal class CheatStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        val store = storeFactory.getCheatStore()
        assertFalse(actual = store.state.loading)
        assertFalse(actual = store.state.integrity)
        assertFalse(actual = store.state.sensorRange)
        assertFalse(actual = store.state.fuel)
        assertFalse(actual = store.state.materials)
        assertFalse(actual = store.state.cryopods)
    }

    @Test
    fun enableAndDisableCheats() = runUnitTest {
        val store = storeFactory.getCheatStore()
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
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Cheat)
        assertNavigation(list = listOf(Screen.Cheat))
        storeFactory.getCheatStore().back()
        assertNavigation(list = emptyList())
    }
}
