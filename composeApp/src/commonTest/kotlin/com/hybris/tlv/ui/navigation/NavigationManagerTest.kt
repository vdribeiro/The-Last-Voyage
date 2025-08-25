package com.hybris.tlv.ui.navigation

import com.hybris.tlv.mock.Mock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class NavigationManagerTest {

    private val mock = Mock()

    @Test
    fun navigate() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.SPLASH, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.ERROR)
        assertEquals(expected = NavigationManager.Screen.ERROR, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.SPLASH)
        assertEquals(expected = NavigationManager.Screen.SPLASH, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.MAIN_MENU)
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.NEW_GAME)
        assertEquals(expected = NavigationManager.Screen.NEW_GAME, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.GAME)
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.EVENT)
        assertEquals(expected = NavigationManager.Screen.EVENT, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.GAME_OVER)
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.LEARN)
        assertEquals(expected = NavigationManager.Screen.LEARN, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.STELLAR_EXPLORER)
        assertEquals(expected = NavigationManager.Screen.STELLAR_EXPLORER, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.SCORE)
        assertEquals(expected = NavigationManager.Screen.SCORE, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.ACHIEVEMENT)
        assertEquals(expected = NavigationManager.Screen.ACHIEVEMENT, actual = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.CREDIT)
        assertEquals(expected = NavigationManager.Screen.CREDIT, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `set music`() = runBlocking {
        mock.navigation.setMusic(enabled = true)
        assertTrue(actual = mock.navigation.stateFlow.value.music)
        mock.navigation.setMusic(enabled = false)
        assertFalse(actual = mock.navigation.stateFlow.value.music)
    }
}
