package com.hybris.tlv.ui.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getNavigation

internal class NavigationManagerTest {

    @Test
    fun navigate() = runBlocking {
        assertEquals(expected = Screen.Splash, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Feedback))
        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Splash))
        assertEquals(expected = Screen.Splash, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.MainMenu))
        assertEquals(expected = Screen.MainMenu, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.NewGame))
        assertEquals(expected = Screen.NewGame, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Game))
        assertEquals(expected = Screen.Game, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Event))
        assertEquals(expected = Screen.Event, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.GameOver))
        assertEquals(expected = Screen.GameOver, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.StellarExplorer))
        assertEquals(expected = Screen.StellarExplorer, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Score))
        assertEquals(expected = Screen.Score, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Achievement))
        assertEquals(expected = Screen.Achievement, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Credit))
        assertEquals(expected = Screen.Credit, actual = getNavigation().stateFlow.value.screen)
    }
}
