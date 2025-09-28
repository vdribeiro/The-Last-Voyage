package com.hybris.tlv.ui.screen.tutorial

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class TutorialStoreTest {

    private val store: TutorialStore get() = storeFactory.createTutorialStore()

    @BeforeTest
    fun setup() = runBlocking {
        testCore.sqlDriver.clearDatabase()
        testCore.navigation.navigate(screen = NavigationManager.Screen.Splash)
        testCore.navigation.navigate(screen = NavigationManager.Screen.MainMenu)
        testCore.navigation.navigate(screen = NavigationManager.Screen.Tutorial)
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
        testCore.navigation.navigate(screen = NavigationManager.Screen.Tutorial)
        testCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MainMenu, actual = testCore.navigation.stateFlow.value.screen)
    }
}
