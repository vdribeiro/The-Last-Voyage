package com.hybris.tlv.ui.screen.stellarexplorer

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
internal class StellarExplorerScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testCore.sqlDriver.clearDatabase()
    }

    @Test
    fun stellarExplorerWithoutData() = runComposeUiTest {
        val store = storeFactory.createStellarExplorerStore()
        setContent {
            AppTheme {
                StellarExplorerScreen(store = store)
            }
        }
    }

    @Test
    fun stellarExplorerWithData() = runComposeUiTest {
        runBlocking { }
        val store = storeFactory.createStellarExplorerStore()
        setContent {
            AppTheme {
                StellarExplorerScreen(store = store)
            }
        }
    }
}
