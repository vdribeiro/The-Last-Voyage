package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.credits
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class CreditStoreTest {

    private val mock = Mock()
    private val store
        get() = CreditStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = CreditState(),
            creditUseCases = mock.useCases.credit
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.CREDIT)
    }

    @Test
    fun `init`() = runBlocking {
        mock.internalCredit.syncCredits()
        val creditStore = store
        assertEquals(expected = credits, actual = creditStore.stateFlow.value.credits)
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.internalCredit.syncCredits()
        val creditStore = store
        assertEquals(expected = NavigationManager.Screen.CREDIT, actual = mock.navigation.stateFlow.value.screen)
        creditStore.send(action = CreditAction.Back)
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }
}
