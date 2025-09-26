package com.hybris.tlv.ui.screen.score

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mockCore
import com.hybris.tlv.storeFactory
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

// TODO
@OptIn(ExperimentalTestApi::class)
internal class ScoreScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        mockCore.sqlDriver.clearDatabase()
    }

    @Test
    fun scoreWithoutData() = runComposeUiTest {
        val store = storeFactory.createScoreStore()
        setContent {
            AppTheme {
                ScoreScreen(store = store)
            }
        }
    }

    @Test
    fun scoreWithData() = runComposeUiTest {
        runBlocking { }
        val store = storeFactory.createScoreStore()
        setContent {
            AppTheme {
                ScoreScreen(store = store)
            }
        }
    }
}
