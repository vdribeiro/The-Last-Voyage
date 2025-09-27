package com.hybris.tlv.ui.navigation

import com.hybris.tlv.testCore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class NavigationManagerTest {

    @Test
    fun navigate() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.Splash, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.Feedback)
        assertEquals(expected = NavigationManager.Screen.Feedback, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.Splash)
        assertEquals(expected = NavigationManager.Screen.Splash, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.MainMenu)
        assertEquals(expected = NavigationManager.Screen.MainMenu, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.NewGame)
        assertEquals(expected = NavigationManager.Screen.NewGame, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.Game)
        assertEquals(expected = NavigationManager.Screen.Game, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.Event)
        assertEquals(expected = NavigationManager.Screen.Event, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.GameOver)
        assertEquals(expected = NavigationManager.Screen.GameOver, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.StellarExplorer)
        assertEquals(expected = NavigationManager.Screen.StellarExplorer, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.Score)
        assertEquals(expected = NavigationManager.Screen.Score, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.Achievement)
        assertEquals(expected = NavigationManager.Screen.Achievement, actual = testCore.navigation.stateFlow.value.screen)

        testCore.navigation.navigate(screen = NavigationManager.Screen.Credit)
        assertEquals(expected = NavigationManager.Screen.Credit, actual = testCore.navigation.stateFlow.value.screen)
    }
}
