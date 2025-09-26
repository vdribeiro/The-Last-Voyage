package com.hybris.tlv.ui.navigation

import com.hybris.tlv.testCore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class NavigationManagerTest {

    @Test
    fun navigate() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.SPLASH, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.FEEDBACK)
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.SPLASH)
        assertEquals(expected = NavigationManager.Screen.SPLASH, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.MAIN_MENU)
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.NEW_GAME)
        assertEquals(expected = NavigationManager.Screen.NEW_GAME, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.GAME)
        assertEquals(expected = NavigationManager.Screen.GAME, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.EVENT)
        assertEquals(expected = NavigationManager.Screen.EVENT, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.GAME_OVER)
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.STELLAR_EXPLORER)
        assertEquals(expected = NavigationManager.Screen.STELLAR_EXPLORER, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.SCORE)
        assertEquals(expected = NavigationManager.Screen.SCORE, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.ACHIEVEMENT)
        assertEquals(expected = NavigationManager.Screen.ACHIEVEMENT, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.CREDIT)
        assertEquals(expected = NavigationManager.Screen.CREDIT, actual = testCore.navigation.stateFlow.value.screen)
    }
}
