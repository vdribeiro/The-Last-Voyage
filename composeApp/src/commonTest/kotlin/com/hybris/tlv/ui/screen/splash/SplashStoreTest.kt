package com.hybris.tlv.ui.screen.splash

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.Screen
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal class SplashStoreTest {

    private val store: SplashStore get() = storeFactory.createSplashStore()

    @BeforeTest
    fun setup() = runBlocking {
        testDependency.sqlDriver.clearDatabase()
        testDependency.navigation.navigate(screen = Screen.Splash)
    }

    @Test
    fun `init`() = runBlocking {
        val splashStore = store
        delay(timeMillis = 100L)
        assertEquals(expected = 1f, actual = splashStore.stateFlow.value.progress)
    }
}
