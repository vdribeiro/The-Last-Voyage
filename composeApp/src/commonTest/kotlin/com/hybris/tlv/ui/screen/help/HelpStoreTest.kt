package com.hybris.tlv.ui.screen.help

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class HelpStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getHelpStore()
        assertEquals(expected = 0, actual = store.versionClick)
        assertFalse(actual = store.state.loading)
        assertEquals(expected = Content.LEARN_MENU, actual = store.state.currentContent)
        assertEquals(expected = TestCase.config.localConfigs.value.formula, actual = store.state.formula)
        assertFalse(actual = store.state.showSnackbar)
    }

    @Test
    fun changeContent() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getHelpStore()
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
        TestCase.assertNavigation(list = listOf(Screen.Tutorial()))
    }

    @Test
    fun versionClick() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getHelpStore()
        assertFalse(actual = store.state.showSnackbar)
        assertEquals(expected = 0, actual = store.versionClick)

        store.send(action = HelpAction.VersionClick(reset = false))
        assertEquals(expected = 1, actual = store.versionClick)
        store.send(action = HelpAction.VersionClick(reset = true))
        assertEquals(expected = 0, actual = store.versionClick)

        for (i in 1..5) {
            store.send(action = HelpAction.VersionClick(reset = false))
            assertEquals(expected = i, actual = store.versionClick)
        }
        assertTrue(actual = store.state.showSnackbar)
    }

    @Test
    fun resetAll() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        val store = TestCase.storeFactory.getHelpStore()
        store.send(action = HelpAction.Reset)
        TestCase.assertNavigation(list = listOf(Screen.Splash(reset = true)))
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Help)
        TestCase.assertNavigation(list = listOf(Screen.Help))
        val store = TestCase.storeFactory.getHelpStore()
        store.send(action = HelpAction.Score)
        assertEquals(expected = Content.SCORE, actual = store.state.currentContent)
        store.back()
        assertEquals(expected = Content.LEARN_MENU, actual = store.state.currentContent)
        store.back()
        TestCase.assertNavigation(list = emptyList())
    }
}
