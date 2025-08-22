package com.hybris.tlv.ui.screen.splash

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.splashs
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class SplashStoreTest {

    private val mock = Mock()
    private val store
        get() = SplashStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = SplashState(),
            splashUseCases = mock.useCases.splash
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.SPLASH)
    }

    @Test
    fun `init`() = runBlocking {
        val splashStore = store
        assertEquals(actual = splashs, expected = splashStore.stateFlow.value.splashs)
    }

    @Test
    fun `send action back`() = runBlocking {
        val splashStore = store
        assertEquals(actual = NavigationManager.Screen.SPLASH, expected = mock.navigation.stateFlow.value.screen)
        splashStore.send(action = SplashAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
