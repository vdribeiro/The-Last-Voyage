package com.hybris.tlv.ui.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getNavigation

internal class NavigationManagerTest {

    @Test
    fun navigate() = runBlocking {
        assertEquals(expected = Route.Splash, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.Feedback))
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.Splash))
        assertEquals(expected = Route.Splash, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.MainMenu))
        assertEquals(expected = Route.MainMenu, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.NewGame))
        assertEquals(expected = Route.NewGame, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.Game))
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.Event))
        assertEquals(expected = Route.Event, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.GameOver))
        assertEquals(expected = Route.GameOver, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.StellarExplorer))
        assertEquals(expected = Route.StellarExplorer, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.Score))
        assertEquals(expected = Route.Score, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.Achievement))
        assertEquals(expected = Route.Achievement, actual = getNavigation().stateFlow.value.route)

        getNavigation().navigate(navigationState = NavigationState(route = Route.Credit))
        assertEquals(expected = Route.Credit, actual = getNavigation().stateFlow.value.route)
    }
}
