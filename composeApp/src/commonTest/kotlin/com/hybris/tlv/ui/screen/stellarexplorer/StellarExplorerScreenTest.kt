package com.hybris.tlv.ui.screen.stellarexplorer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class StellarExplorerScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testDependency.sqlDriver.clearDatabase()
    }

    @Test
    fun stellarExplorerWithoutData() = runComposeUiTest {
        val store = storeFactory.createStellarExplorerStore()
        setContent {
            AppTheme {
                StellarExplorerScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN).assertExists()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_CONTROL_PANEL).assertExists()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_STELLAR_HOST_CONTENT).assertExists()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_PLANET_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_STELLAR_HOST_CONTENT_HOST).assertDoesNotExist()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_STELLAR_HOST_CONTENT_PLANET).assertDoesNotExist()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_PLANET_CONTENT_HOST).assertDoesNotExist()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_PLANET_CONTENT_PLANET).assertDoesNotExist()
    }

    @Test
    fun stellarExplorerWithData() = runComposeUiTest {
        runBlocking {
            testDependency.useCases.space.prepopulateStellarHosts()
            testDependency.useCases.space.prepopulatePlanets()
        }
        val store = storeFactory.createStellarExplorerStore()
        setContent {
            AppTheme {
                StellarExplorerScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN).assertExists()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_CONTROL_PANEL).assertExists()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_STELLAR_HOST_CONTENT).assertExists()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_STELLAR_HOST_CONTENT_PLANET).assertDoesNotExist()
        onAllNodesWithTag(testTag = STELLAR_EXPLORER_SCREEN_STELLAR_HOST_CONTENT_HOST).assertCountEquals(expectedSize = 2)
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_PLANET_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_PLANET_CONTENT_HOST).assertDoesNotExist()
        onNodeWithTag(testTag = STELLAR_EXPLORER_SCREEN_PLANET_CONTENT_PLANET).assertDoesNotExist()
    }
}
