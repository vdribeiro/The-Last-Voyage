package com.hybris.tlv.ui.screen.newgame

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import com.hybris.tlv.storeFactory
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

// TODO
@OptIn(ExperimentalTestApi::class)
internal class NewGameScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun newGameWithoutData() = runComposeUiTest {
        val store = storeFactory.createNewGameStore()
        setContent {
            AppTheme {
                NewGameScreen(store = store)
            }
        }
    }

    @Test
    fun newGameWithData() = runComposeUiTest {
        runBlocking { }
        val store = storeFactory.createNewGameStore()
        setContent {
            AppTheme {
                NewGameScreen(store = store)
            }
        }
    }
}
