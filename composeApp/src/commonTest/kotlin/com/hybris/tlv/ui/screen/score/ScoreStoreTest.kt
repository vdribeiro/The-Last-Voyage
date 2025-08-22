package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.scores
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class ScoreStoreTest {

    private val mock = Mock()
    private val store
        get() = ScoreStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = ScoreState(),
            scoreUseCases = mock.useCases.score
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.SCORE)
    }

    @Test
    fun `init`() = runBlocking {
        val scoreStore = store
        assertEquals(actual = scores, expected = scoreStore.stateFlow.value.scores)
    }

    @Test
    fun `send action back`() = runBlocking {
        val scoreStore = store
        assertEquals(actual = NavigationManager.Screen.SCORE, expected = mock.navigation.stateFlow.value.screen)
        scoreStore.send(action = ScoreAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
