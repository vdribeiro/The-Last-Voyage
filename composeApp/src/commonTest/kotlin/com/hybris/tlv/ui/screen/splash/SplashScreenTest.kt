package com.hybris.tlv.ui.screen.splash

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

// TODO
@OptIn(ExperimentalTestApi::class)
internal class SplashScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testCore.sqlDriver.clearDatabase()
    }

    @Test
    fun splashWithoutData() = runComposeUiTest {
        val store = storeFactory.createSplashStore()
        setContent {
            AppTheme {
                SplashScreen(store = store)
            }
        }
    }

    @Test
    fun splashWithData() = runComposeUiTest {
        runBlocking { }
        val store = storeFactory.createSplashStore()
        setContent {
            AppTheme {
                SplashScreen(store = store)
            }
        }
    }
}
