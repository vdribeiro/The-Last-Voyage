package com.hybris.tlv.ui.screen.splash

import com.hybris.tlv.mock.Mock
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
            syncUseCases = mock.useCases.sync
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.SPLASH)
    }

    @Test
    fun `init`() = runBlocking {
        val splashStore = store
        assertEquals(expected = 1f, actual = splashStore.stateFlow.value.progress)
    }
}
