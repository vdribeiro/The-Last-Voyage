package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.credits
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mockCore
import com.hybris.tlv.storeFactory
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class CreditStoreTest {

    private val store: CreditStore get() = storeFactory.createCreditStore()

    @BeforeTest
    fun setup() = runBlocking {
        mockCore.sqlDriver.clearDatabase()
        mockCore.navigation.navigate(screen = NavigationManager.Screen.CREDIT)
    }

    @Test
    fun `init`() = runBlocking {
        mockCore.useCases.credit.prepopulateCredits()
        val creditStore = store
        assertEquals(expected = credits, actual = creditStore.stateFlow.value.credits)
    }

    @Test
    fun `send action back`() = runBlocking {
        mockCore.useCases.credit.prepopulateCredits()
        store
        assertEquals(expected = NavigationManager.Screen.CREDIT, actual = mockCore.navigation.stateFlow.value.screen)
        mockCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mockCore.navigation.stateFlow.value.screen)
    }
}
