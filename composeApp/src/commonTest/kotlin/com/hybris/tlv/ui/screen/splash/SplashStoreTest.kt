package com.hybris.tlv.ui.screen.splash

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal class SplashStoreTest {

    private val store
        get() = SplashStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = SplashState(),
            syncUseCases = mock.useCases.sync
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.SPLASH)
    }

    @Test
    fun `init`() = runBlocking {
        val splashStore = store
        delay(timeMillis = 1000L)
        assertEquals(expected = 1f, actual = splashStore.stateFlow.value.progress)
    }

    @Test
    fun `send action start`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.SPLASH, actual = mock.navigation.stateFlow.value.screen)
        val gameStore = store
        gameStore.send(action = SplashAction.Start)
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }
}
