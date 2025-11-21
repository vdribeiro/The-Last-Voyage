package com.hybris.tlv.ui.screen.tutorial

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getTutorialStore
import com.hybris.tlv.reset

internal class TutorialStoreTest {

    private val store: TutorialStore get() = getTutorialStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = SplashScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = MainMenuScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = TutorialScreen))
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
//        getNavigation().navigate(navigationState = NavigationState(screen = TutorialScreen))
//        getNavigation().back()
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
    }
}
