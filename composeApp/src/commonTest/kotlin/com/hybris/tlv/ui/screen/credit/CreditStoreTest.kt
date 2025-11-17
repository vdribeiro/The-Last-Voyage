package com.hybris.tlv.ui.screen.credit

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.credits
import com.hybris.tlv.getNavigation
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen

internal class CreditStoreTest {

    private val store: CreditStore get() = getStoreFactory().createCreditStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Credit))
    }

    @Test
    fun `init`() = runBlocking {
        getUseCases().credit.syncCredits()
        val creditStore = store
        assertEquals(expected = credits, actual = creditStore.stateFlow.value.credits)
    }

    @Test
    fun `send action back`() = runBlocking {
        getUseCases().credit.syncCredits()
        store
        assertEquals(expected = Screen.Credit, actual = getNavigation().stateFlow.value.screen)
        getNavigation().back()
        assertEquals(expected = Screen.MainMenu, actual = getNavigation().stateFlow.value.screen)
    }
}
