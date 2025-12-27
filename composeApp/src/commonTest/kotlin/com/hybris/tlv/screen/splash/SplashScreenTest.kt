package com.hybris.tlv.screen.splash

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import com.hybris.tlv.TestCase
import com.hybris.tlv.screen.stellarexplorer.StellarExplorerScreen

@OptIn(ExperimentalTestApi::class)
internal class SplashScreenTest: TestCase() {

    @Test
    fun splash() = runUITest {
        val store = storeFactory.getSplashStore(reset = true)
        setScreen { SplashScreen(store = store) }

    }
}
