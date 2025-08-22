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
        assertEquals(actual = NavigationManager.Screen.SPLASH, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.ERROR)
        assertEquals(actual = NavigationManager.Screen.ERROR, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.SPLASH)
        assertEquals(actual = NavigationManager.Screen.SPLASH, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.MAIN_MENU)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.NEW_GAME)
        assertEquals(actual = NavigationManager.Screen.NEW_GAME, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.GAME)
        assertEquals(actual = NavigationManager.Screen.GAME, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.EVENT)
        assertEquals(actual = NavigationManager.Screen.EVENT, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.GAME_OVER)
        assertEquals(actual = NavigationManager.Screen.GAME_OVER, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.EXPLORE)
        assertEquals(actual = NavigationManager.Screen.EXPLORE, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.STELLAR_EXPLORER)
        assertEquals(actual = NavigationManager.Screen.STELLAR_EXPLORER, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.SCORE)
        assertEquals(actual = NavigationManager.Screen.SCORE, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.ACHIEVEMENT)
        assertEquals(actual = NavigationManager.Screen.ACHIEVEMENT, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.CREDIT)
        assertEquals(actual = NavigationManager.Screen.CREDIT, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `set music`() = runBlocking {
        mock.navigation.setMusic(enabled = true)
        assertTrue(actual = mock.navigation.stateFlow.value.music)
        mock.navigation.setMusic(enabled = false)
        assertFalse(actual = mock.navigation.stateFlow.value.music)
    }
}
