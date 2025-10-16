package com.hybris.tlv.ui.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.testDependency

internal class NavigationManagerTest {

    @Test
    fun navigate() = runBlocking {
        assertEquals(expected = Screen.Splash, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.Feedback)
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.Splash)
        assertEquals(expected = Screen.Splash, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.MainMenu)
        assertEquals(expected = Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.NewGame)
        assertEquals(expected = Screen.NewGame, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.Game)
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.Event)
        assertEquals(expected = Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.GameOver)
        assertEquals(expected = Screen.GameOver, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.StellarExplorer)
        assertEquals(expected = Screen.StellarExplorer, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.Score)
        assertEquals(expected = Screen.Score, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.Achievement)
        assertEquals(expected = Screen.Achievement, actual = testDependency.navigation.stateFlow.value.screen)

        testDependency.navigation.navigate(screen = Screen.Credit)
        assertEquals(expected = Screen.Credit, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
