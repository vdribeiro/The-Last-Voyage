package com.hybris.tlv.screen.gameover

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.navigation.Screen

internal class GameOverStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameOverStore()
        assertTrue(store.achievements.isEmpty())
        assertEquals(expected = 0, actual = store.index)
        assertFalse(actual = store.state.loading)
        assertEquals(expected = Content.MESSAGE, actual = store.state.currentContent)
        assertNotNull(actual = store.state.gameSession)
        assertNotNull(actual = store.state.gameOver)
        assertNotNull(actual = store.state.achievement)
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        storeFactory.getGameOverStore()
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun nextContent() = runUnitTest {
        assertNavigation(list = emptyList())
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameOverStore()
        assertEquals(expected = Content.MESSAGE, actual = store.state.currentContent)
        store.send(action = GameOverAction.Next)
        assertEquals(expected = Content.SCORE, actual = store.state.currentContent)
        store.send(action = GameOverAction.Next)
        assertNavigation(list = listOf(Screen.MainMenu))
    }

    @Test
    fun nextContentWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = storeFactory.getGameOverStore()
        store.send(action = GameOverAction.Next)
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun achievements() = runUnitTest {
        useCases.achievement.prepopulateAchievements()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = useCases.gameSession.getLatestGameSession()!!
        useCases.gameSession.updateGameSession(
            gameSession = gameSession.copy(
                currentStellarHostId = "sol",
                settledPlanetId = "3earth",
                finalHabitability = 0.0
            )
        )
        val store = storeFactory.getGameOverStore()
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
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.GameOver)
        assertNavigation(list = listOf(Screen.GameOver))
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        storeFactory.getGameOverStore().back()
        assertNavigation(list = listOf(Screen.GameOver))
    }
}
