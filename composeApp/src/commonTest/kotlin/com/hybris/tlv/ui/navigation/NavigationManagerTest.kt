package com.hybris.tlv.ui.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getNavigation

internal class NavigationManagerTest {

    @Test
    fun navigate() = runBlocking {
        assertEquals(expected = Splash, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Feedback))
        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Splash))
        assertEquals(expected = Splash, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = MainMenu))
        assertEquals(expected = MainMenu, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = NewGame))
        assertEquals(expected = NewGame, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Game))
        assertEquals(expected = Game, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Event))
        assertEquals(expected = Event, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = GameOver))
        assertEquals(expected = GameOver, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = StellarExplorer))
        assertEquals(expected = StellarExplorer, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Score))
        assertEquals(expected = Score, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Achievement))
        assertEquals(expected = Achievement, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Credit))
        assertEquals(expected = Credit, actual = getNavigation().stateFlow.value.screen)
    }
}
