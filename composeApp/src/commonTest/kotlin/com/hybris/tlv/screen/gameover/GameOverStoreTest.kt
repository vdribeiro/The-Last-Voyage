package com.hybris.tlv.screen.gameover

import com.hybris.tlv.TestCase

// TODO
internal class GameOverStoreTest: TestCase() {

//    private val store: GameOverStore get() = getGameOverStore()
//
//    @BeforeTest
//    fun setup() = runBlocking {
//        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = GameOverScreen))
//    }
//
//    @Test
//    fun `init`() = runBlocking {
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        val gameOverStore = store
//        assertNotNull(actual = gameOverStore.stateFlow.value.gameSession)
//        assertNotNull(actual = gameOverStore.stateFlow.value.gameOver)
//        assertEquals(expected = Content.MESSAGE, actual = gameOverStore.stateFlow.value.currentContent)
//    }
//
//    @Test
//    fun `init without game session`() = runBlocking {
//        assertEquals(expected = GameOverScreen, actual = getNavigation().stateFlow.value.screen)
//        val gameOverStore = store
//        assertNull(actual = gameOverStore.stateFlow.value.gameSession)
//        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)
//    }
//
//    @Test
//    fun `send action back`() = runBlocking {
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        store
//        assertEquals(expected = GameOverScreen, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//        assertEquals(expected = GameOverScreen, actual = getNavigation().stateFlow.value.screen)
//    }
//
//    @Test
//    fun `send action continue`() = runBlocking {
//        assertEquals(expected = GameOverScreen, actual = getNavigation().stateFlow.value.screen)
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        val gameOverStore = store
//        assertEquals(expected = Content.MESSAGE, actual = gameOverStore.stateFlow.value.currentContent)
//        gameOverStore.send(action = GameOverAction.Next)
//        assertEquals(expected = Content.SCORE, actual = gameOverStore.stateFlow.value.currentContent)
//        gameOverStore.send(action = GameOverAction.Next)
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
//    }
//
//    @Test
//    fun `send action continue without game session`() = runBlocking {
//        val gameOverStore = store
//        gameOverStore.send(action = GameOverAction.Next)
//        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)
//    }
}
