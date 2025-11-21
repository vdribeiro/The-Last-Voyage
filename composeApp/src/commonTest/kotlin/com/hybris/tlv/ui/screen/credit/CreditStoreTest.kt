package com.hybris.tlv.ui.screen.credit

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.credits
import com.hybris.tlv.getCreditStore
import com.hybris.tlv.reset

internal class CreditStoreTest {

    private val store: CreditStore get() = getCreditStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = CreditScreen))
    }

    @Test
    fun `init`() = runBlocking {
//        useCases.credit.syncCredits()
        val creditStore = store
        assertEquals(expected = credits, actual = creditStore.stateFlow.value.credits)
    }

    @Test
    fun `send action back`() = runBlocking {
//        useCases.credit.syncCredits()
        store
//        assertEquals(expected = CreditScreen, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
    }
}
