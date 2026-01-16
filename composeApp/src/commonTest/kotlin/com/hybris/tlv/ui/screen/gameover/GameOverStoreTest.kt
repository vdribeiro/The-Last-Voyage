package com.hybris.tlv.ui.screen.gameover

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.ui.navigation.Screen

internal class GameOverStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getGameOverStore()
        assertTrue(store.achievements.isEmpty())
        assertEquals(expected = 0, actual = store.index)
        assertFalse(actual = store.state.loading)
        assertEquals(expected = Content.MESSAGE, actual = store.state.currentContent)
        assertNotNull(actual = store.state.gameSession)
        assertNotNull(actual = store.state.gameOver)
        assertNull(actual = store.state.achievement)
    }

    @Test
    fun initWithoutGameSession() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.storeFactory.getGameOverStore()
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun nextContent() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getGameOverStore()
        assertEquals(expected = Content.MESSAGE, actual = store.state.currentContent)
        store.send(action = GameOverAction.Next)
        assertEquals(expected = Content.SCORE, actual = store.state.currentContent)
        store.send(action = GameOverAction.Next)
        TestCase.assertNavigation(list = listOf(Screen.MainMenu))
    }

    @Test
    fun nextContentWithoutGameSession() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        val store = TestCase.storeFactory.getGameOverStore()
        store.send(action = GameOverAction.Next)
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun achievements() = TestCase.runUnitTest {
        TestCase.useCases.achievement.prepopulateAchievements()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = TestCase.useCases.gameSession.getLatestGameSession()!!
        TestCase.useCases.gameSession.updateGameSession(
            gameSession = gameSession.copy(
                currentStellarHostId = "sol",
                settledPlanetId = "3earth",
                finalHabitability = 0.0
            )
        )
        val store = TestCase.storeFactory.getGameOverStore()
        assertTrue(actual = store.achievements.isNotEmpty())
        assertEquals(expected = 0, actual = store.index)
        store.send(action = GameOverAction.Next)
        assertEquals(expected = store.achievements.first(), actual = store.state.achievement)

        store.achievements.forEachIndexed { index, _ ->
            assertEquals(expected = store.achievements.getOrNull(index = index), actual = store.state.achievement)
            assertEquals(expected = index, actual = store.index)
            store.send(action = GameOverAction.NextAchievement)
        }
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.GameOver)
        TestCase.assertNavigation(list = listOf(Screen.GameOver))
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        TestCase.storeFactory.getGameOverStore().back()
        TestCase.assertNavigation(list = listOf(Screen.GameOver))
    }
}
