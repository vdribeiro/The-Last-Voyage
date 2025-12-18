package com.hybris.tlv.screen.tutorial

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
        assertEquals(expected = Content.GOAL, actual = tutorialStore.stateFlow.value.currentContent)
        tutorialStore.send(action = TutorialAction.Next)
        assertEquals(expected = Content.SHIP, actual = tutorialStore.stateFlow.value.currentContent)
        tutorialStore.send(action = TutorialAction.Next)
        assertEquals(expected = Content.SYSTEM, actual = tutorialStore.stateFlow.value.currentContent)
        tutorialStore.send(action = TutorialAction.Next)
        assertEquals(expected = Content.TRAVEL, actual = tutorialStore.stateFlow.value.currentContent)
        tutorialStore.send(action = TutorialAction.Next)
        assertEquals(expected = Content.GAME_OVER, actual = tutorialStore.stateFlow.value.currentContent)
        tutorialStore.send(action = TutorialAction.Next)
        assertEquals(expected = Content.GAME_OVER, actual = tutorialStore.stateFlow.value.currentContent)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
//        getNavigation().navigate(navigationState = NavigationState(screen = TutorialScreen))
//        getNavigation().back()
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
    }
}
