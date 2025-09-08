package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.Core
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.gameSessionPrototype
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class ScoreStoreTest {

    private val mock by lazy {
        Core(
            dispatcher = TestDispatchers(),
            sqlDriver = createSqlDriver(inMemory = true),
            httpClient = HttpClientFactory.buildHttpClient()
        )
    }
    private val store
        get() = ScoreStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = ScoreState(),
            gameSessionUseCases = mock.useCases.gameSession
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.SCORE)
    }

    @Test
    fun `init`() = runBlocking {
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = mock.useCases.gameSession.getLatestGameSession()!!
        mock.useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val scoreStore = store
        assertEquals(expected = listOf(mock.useCases.gameSession.getLatestGameSession()), actual = scoreStore.stateFlow.value.scores)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = NavigationManager.Screen.SCORE, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }
}
