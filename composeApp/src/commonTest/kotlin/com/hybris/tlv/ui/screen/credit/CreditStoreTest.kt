package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.credits
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class CreditStoreTest {

    private val store: CreditStore get() = storeFactory.createCreditStore()

    @BeforeTest
    fun setup() = runBlocking {
        testDependency.sqlDriver.clearDatabase()
        testDependency.navigation.navigate(screen = NavigationManager.Screen.Credit)
    }

    @Test
    fun `init`() = runBlocking {
        testDependency.useCases.credit.prepopulateCredits()
        val creditStore = store
        assertEquals(expected = credits, actual = creditStore.stateFlow.value.credits)
    }

    @Test
    fun `send action back`() = runBlocking {
        testDependency.useCases.credit.prepopulateCredits()
        store
        assertEquals(expected = NavigationManager.Screen.Credit, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
