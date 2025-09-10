package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.credits
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class CreditStoreTest {

    private val store by lazy {
        CreditStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = CreditState(),
            creditUseCases = mock.useCases.credit
        )
    }

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.CREDIT)
    }

    @Test
    fun `init`() = runBlocking {
        mock.useCases.sync.sync().last()
        val creditStore = store.apply { setup(state = CreditState()) }
        assertEquals(expected = credits, actual = creditStore.stateFlow.value.credits)
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.useCases.sync.sync().last()
        store.setup(state = CreditState())
        assertEquals(expected = NavigationManager.Screen.CREDIT, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }
}
