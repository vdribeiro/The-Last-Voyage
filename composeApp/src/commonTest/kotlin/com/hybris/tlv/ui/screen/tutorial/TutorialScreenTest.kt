package com.hybris.tlv.ui.screen.tutorial

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class TutorialScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testCore.sqlDriver.clearDatabase()
    }

    @Test
    fun tutorial() = runComposeUiTest {
        val store = storeFactory.createTutorialStore()
        setContent {
            AppTheme {
                TutorialScreen(store = store)
            }
        }
    }
}
