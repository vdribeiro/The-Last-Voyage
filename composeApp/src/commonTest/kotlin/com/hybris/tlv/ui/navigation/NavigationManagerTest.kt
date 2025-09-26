package com.hybris.tlv.ui.navigation

import com.hybris.tlv.mockCore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class NavigationManagerTest {

    @Test
    fun navigate() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.SPLASH, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.FEEDBACK)
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.SPLASH)
        assertEquals(expected = NavigationManager.Screen.SPLASH, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.MAIN_MENU)
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.NEW_GAME)
        assertEquals(expected = NavigationManager.Screen.NEW_GAME, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.GAME)
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.EVENT)
        assertEquals(expected = NavigationManager.Screen.EVENT, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.GAME_OVER)
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.STELLAR_EXPLORER)
        assertEquals(expected = NavigationManager.Screen.STELLAR_EXPLORER, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.SCORE)
        assertEquals(expected = NavigationManager.Screen.SCORE, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.ACHIEVEMENT)
        assertEquals(expected = NavigationManager.Screen.ACHIEVEMENT, actual = mockCore.navigation?.stateFlow?.value?.screen)

        mockCore.navigation?.navigate(screen = NavigationManager.Screen.CREDIT)
        assertEquals(expected = NavigationManager.Screen.CREDIT, actual = mockCore.navigation?.stateFlow?.value?.screen)
    }
}
