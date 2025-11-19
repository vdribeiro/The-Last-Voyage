package com.hybris.tlv.ui.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getNavigation

internal class NavigationManagerTest {

    @Test
    fun navigate() = runBlocking {
        assertEquals(expected = SplashScreen, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Feedback))
        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = SplashScreen))
        assertEquals(expected = SplashScreen, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = MainMenuScreen))
        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = NewGameScreen))
        assertEquals(expected = NewGameScreen, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = GameScreen))
        assertEquals(expected = GameScreen, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = EventScreen))
        assertEquals(expected = EventScreen, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = GameOverScreen))
        assertEquals(expected = GameOverScreen, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = StellarExplorerScreen))
        assertEquals(expected = StellarExplorerScreen, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = ScoreScreen))
        assertEquals(expected = ScoreScreen, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = AchievementScreen))
        assertEquals(expected = AchievementScreen, actual = getNavigation().stateFlow.value.screen)

        getNavigation().navigate(navigationState = NavigationState(screen = CreditScreen))
        assertEquals(expected = CreditScreen, actual = getNavigation().stateFlow.value.screen)
    }
}
