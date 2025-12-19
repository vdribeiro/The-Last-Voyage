package com.hybris.tlv.screen.cheat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.navigation.Screen

internal class CheatStoreTest: TestCase() {

    @Test
    fun initStore() = runUnitTest {
        val store = storeFactory.getCheatStore()
        assertEquals(expected = false, actual = store.state().loading)
        assertEquals(expected = false, actual = store.state().integrity)
        assertEquals(expected = false, actual = store.state().sensorRange)
        assertEquals(expected = false, actual = store.state().fuel)
        assertEquals(expected = false, actual = store.state().materials)
        assertEquals(expected = false, actual = store.state().cryopods)
    }

    @Test
    fun enableAndDisableCheats() = runUnitTest {
        val store = storeFactory.getCheatStore()
        assertEquals(expected = false, actual = store.state().loading)

        assertEquals(expected = false, actual = store.state().integrity)
        store.send(action = CheatAction.ToggleIntegrity)
        assertEquals(expected = true, actual = store.state().integrity)
        store.send(action = CheatAction.ToggleIntegrity)
        assertEquals(expected = false, actual = store.state().integrity)

        assertEquals(expected = false, actual = store.state().sensorRange)
        store.send(action = CheatAction.ToggleSensorRange)
        assertEquals(expected = true, actual = store.state().sensorRange)
        store.send(action = CheatAction.ToggleSensorRange)
        assertEquals(expected = false, actual = store.state().sensorRange)

        assertEquals(expected = false, actual = store.state().fuel)
        store.send(action = CheatAction.ToggleFuel)
        assertEquals(expected = true, actual = store.state().fuel)
        store.send(action = CheatAction.ToggleFuel)
        assertEquals(expected = false, actual = store.state().fuel)

        assertEquals(expected = false, actual = store.state().materials)
        store.send(action = CheatAction.ToggleMaterials)
        assertEquals(expected = true, actual = store.state().materials)
        store.send(action = CheatAction.ToggleMaterials)
        assertEquals(expected = false, actual = store.state().materials)

        assertEquals(expected = false, actual = store.state().cryopods)
        store.send(action = CheatAction.ToggleCryopods)
        assertEquals(expected = true, actual = store.state().cryopods)
        store.send(action = CheatAction.ToggleCryopods)
        assertEquals(expected = false, actual = store.state().cryopods)
    }

    @Test
    fun `send action back`() = runUnitTest {
        assertTrue(actual = screens.isEmpty())
        navigate(screen = Screen.Cheat)
        assertEquals(expected = listOf(element = Screen.Cheat), actual = screens)
        val store = storeFactory.getCheatStore()
        store.back()
        assertTrue(actual = screens.isEmpty())
    }
}
