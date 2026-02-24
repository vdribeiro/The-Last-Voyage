package com.hybris.tlv.ui.screen.gameover

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class GameOverStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = getStoreFactory().getGameOverStore()
        assertTrue(store.achievements.isEmpty())
        assertEquals(expected = 0, actual = store.index)
        assertFalse(actual = store.state.loading)
        assertEquals(expected = Content.MESSAGE, actual = store.state.currentContent)
        assertNotNull(actual = store.state.gameSession)
        assertNotNull(actual = store.state.gameOver)
        assertNull(actual = store.state.achievement)
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        getStoreFactory().getGameOverStore()
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun nextContent() = runUnitTest {
        assertNavigation(list = emptyList())
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = getStoreFactory().getGameOverStore()
        assertEquals(expected = Content.MESSAGE, actual = store.state.currentContent)
        store.send(action = GameOverAction.Next)
        assertEquals(expected = Content.SCORE, actual = store.state.currentContent)
        store.send(action = GameOverAction.Next)
        assertNavigation(list = listOf(Screen.MainMenu))
    }

    @Test
    fun nextContentWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = getStoreFactory().getGameOverStore()
        store.send(action = GameOverAction.Next)
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun achievements() = runUnitTest {
        dependency.get().useCases.achievement.prepopulateAchievements()
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val gameSession = dependency.get().useCases.gameSession.getLatestGameSession()!!
        dependency.get().useCases.gameSession.updateGameSession(
            gameSession = gameSession.copy(
                currentStellarHostId = "sol",
                settledPlanetId = "3earth",
                finalHabitability = 0.0
            )
        )
        val store = getStoreFactory().getGameOverStore()
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
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        getStoreFactory().getGameOverStore().navigateBack()
        assertNavigation(list = listOf(Screen.GameOver))
    }
}
