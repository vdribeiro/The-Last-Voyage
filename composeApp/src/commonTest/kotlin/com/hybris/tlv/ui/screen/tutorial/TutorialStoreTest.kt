package com.hybris.tlv.ui.screen.tutorial

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getNavigation
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.reset
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Route

internal class TutorialStoreTest {

    private val store: TutorialStore get() = getStoreFactory().createTutorialStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        getNavigation().navigate(navigationState = NavigationState(route = Route.Splash))
        getNavigation().navigate(navigationState = NavigationState(route = Route.MainMenu))
        getNavigation().navigate(navigationState = NavigationState(route = Route.Tutorial))
    }

    @Test
    fun `complete tutorial`() = runBlocking {
        val tutorialStore = store
        assertEquals(expected = Tutorial.GOAL, actual = tutorialStore.stateFlow.value.tutorialStep)
        tutorialStore.send(action = TutorialAction.Next)
        assertEquals(expected = Tutorial.SHIP, actual = tutorialStore.stateFlow.value.tutorialStep)
        tutorialStore.send(action = TutorialAction.Next)
        assertEquals(expected = Tutorial.SYSTEM, actual = tutorialStore.stateFlow.value.tutorialStep)
        tutorialStore.send(action = TutorialAction.Next)
        assertEquals(expected = Tutorial.TRAVEL, actual = tutorialStore.stateFlow.value.tutorialStep)
        tutorialStore.send(action = TutorialAction.Next)
        assertEquals(expected = Tutorial.GAME_OVER, actual = tutorialStore.stateFlow.value.tutorialStep)
        tutorialStore.send(action = TutorialAction.Next)
        assertEquals(expected = Tutorial.GAME_OVER, actual = tutorialStore.stateFlow.value.tutorialStep)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        getNavigation().navigate(navigationState = NavigationState(route = Route.Tutorial))
        getNavigation().back()
        assertEquals(expected = Route.MainMenu, actual = getNavigation().stateFlow.value.route)
    }
}
