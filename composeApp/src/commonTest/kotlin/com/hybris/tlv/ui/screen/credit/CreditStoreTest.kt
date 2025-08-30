package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.credits
import com.hybris.tlv.mock.mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class CreditStoreTest {

    private val store
        get() = CreditStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = CreditState(),
            creditUseCases = mock.useCases.credit
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
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
        store
        assertEquals(expected = NavigationManager.Screen.CREDIT, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }
}
