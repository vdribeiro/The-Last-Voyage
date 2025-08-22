package com.hybris.tlv.ui.screen.stellarexplorer

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.stellarExplorers
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class StellarExplorerStoreTest {

    private val mock = Mock()
    private val store
        get() = StellarExplorerStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = StellarExplorerState(),
            stellarExplorerUseCases = mock.useCases.stellarExplorer
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.STELLAR_EXPLORER)
    }

    @Test
    fun `init`() = runBlocking {
        val stellarExplorerStore = store
        assertEquals(actual = stellarExplorers, expected = stellarExplorerStore.stateFlow.value.stellarExplorers)
    }

    @Test
    fun `send action back`() = runBlocking {
        val stellarExplorerStore = store
        assertEquals(actual = NavigationManager.Screen.STELLAR_EXPLORER, expected = mock.navigation.stateFlow.value.screen)
        stellarExplorerStore.send(action = StellarExplorerAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
