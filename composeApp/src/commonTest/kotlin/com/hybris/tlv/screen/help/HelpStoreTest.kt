package com.hybris.tlv.screen.help

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import com.hybris.tlv.TestCase
import com.hybris.tlv.navigation.Screen

internal class HelpStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        val store = storeFactory.getHelpStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = Content.LEARN_MENU, actual = store.state.currentContent)
    }

    @Test
    fun changeContent() = runUnitTest {
        val store = storeFactory.getHelpStore()
        assertEquals(expected = Content.LEARN_MENU, actual = store.state.currentContent)
        store.send(action = HelpAction.Navigation)
        assertEquals(expected = Content.NAVIGATION, actual = store.state.currentContent)
        store.send(action = HelpAction.ControlPanel)
        assertEquals(expected = Content.CONTROL_PANEL, actual = store.state.currentContent)
        store.send(action = HelpAction.HostDefinition)
        assertEquals(expected = Content.HOST_DEFINITION, actual = store.state.currentContent)
        store.send(action = HelpAction.HostType)
        assertEquals(expected = Content.HOST_TYPE, actual = store.state.currentContent)
        store.send(action = HelpAction.PlanetDefinition)
        assertEquals(expected = Content.PLANET_DEFINITION, actual = store.state.currentContent)
        store.send(action = HelpAction.PlanetType)
        assertEquals(expected = Content.PLANET_TYPE, actual = store.state.currentContent)
        store.send(action = HelpAction.Habitability)
        assertEquals(expected = Content.HABITABILITY, actual = store.state.currentContent)
        store.send(action = HelpAction.Score)
        assertEquals(expected = Content.SCORE, actual = store.state.currentContent)
        store.send(action = HelpAction.Mechanics)
        assertNavigationBackstack(list = listOf(element = Screen.Tutorial()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        navigate(screen = Screen.Help)
        assertNavigationBackstack(list = listOf(element = Screen.Help))
        storeFactory.getHelpStore().back()
        assertNavigationBackstack(list = emptyList())
    }
}
