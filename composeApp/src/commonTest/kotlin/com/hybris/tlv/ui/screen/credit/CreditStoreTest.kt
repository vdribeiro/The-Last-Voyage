package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.Core
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.credits
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class CreditStoreTest {

    private val mock by lazy {
        Core(
            dispatcher = TestDispatchers(),
            sqlDriver = createSqlDriver(inMemory = true),
            httpClient = HttpClientFactory.buildHttpClient()
        )
    }
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
        mock.useCases.sync.sync().last()
        val creditStore = store
        assertEquals(expected = credits, actual = creditStore.stateFlow.value.credits)
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.useCases.sync.sync().last()
        store
        assertEquals(expected = NavigationManager.Screen.CREDIT, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }
}
