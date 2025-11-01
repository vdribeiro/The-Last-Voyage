package com.hybris.tlv.ui.screen.credit

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.credits
import com.hybris.tlv.reset
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen

internal class CreditStoreTest {

    private val store: CreditStore get() = storeFactory.createCreditStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Credit))
    }

    @Test
    fun `init`() = runBlocking {
        testDependency.useCases.credit.syncCredits()
        val creditStore = store
        assertEquals(expected = credits, actual = creditStore.stateFlow.value.credits)
    }

    @Test
    fun `send action back`() = runBlocking {
        testDependency.useCases.credit.syncCredits()
        store
        assertEquals(expected = Screen.Credit, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
