package com.hybris.tlv.ui.screen.tutorial

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.reset
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen

internal class TutorialStoreTest {

    private val store: TutorialStore get() = storeFactory.createTutorialStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Splash))
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.MainMenu))
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Tutorial))
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
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Tutorial))
        testDependency.navigation.back()
        assertEquals(expected = Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
