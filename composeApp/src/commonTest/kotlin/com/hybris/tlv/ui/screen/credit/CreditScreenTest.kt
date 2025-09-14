package com.hybris.tlv.ui.screen.credit

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.credits
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import com.hybris.tlv.storeFactory
import com.hybris.tlv.usecase.credit.model.CreditType
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class CreditScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun creditEmpty() = runComposeUiTest {
        val store = storeFactory.createCreditStore()
        setContent {
            CreditScreen(store = store)
        }

        onNodeWithTag(testTag = CREDIT_SCREEN).assertExists()
        onNodeWithTag(testTag = CREDIT_SCREEN_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST).assertExists()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR_ITEM).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE_ITEM).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC_ITEM).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER_ITEM).assertDoesNotExist()
    }

    @Test
    fun creditList() = runComposeUiTest {
        var openedUri: String?
        val mockUriHandler = object: UriHandler {
            override fun openUri(uri: String) {
                openedUri = uri
            }
        }

        runBlocking { mock.useCases.credit.prepopulateCredits() }
        val store = storeFactory.createCreditStore()
        setContent {
            CompositionLocalProvider(value = LocalUriHandler provides mockUriHandler) {
                CreditScreen(store = store)
            }
        }

        val creditsMap = credits.groupBy { it.type }
        val creators = creditsMap[CreditType.CREATOR].orEmpty()
        val sources = creditsMap[CreditType.SOURCE].orEmpty()
        val musics = creditsMap[CreditType.MUSIC].orEmpty()
        val supporters = creditsMap[CreditType.SUPPORTER].orEmpty()

        onNodeWithTag(testTag = CREDIT_SCREEN).assertExists()
        onNodeWithTag(testTag = CREDIT_SCREEN_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST).assertExists()

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR).performScrollTo().assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR_ITEM)
            .assertCountEquals(expectedSize = creators.size)

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE).performScrollTo().assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE_ITEM)
            .assertCountEquals(expectedSize = sources.size)

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC).performScrollTo().assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC_ITEM)
            .assertCountEquals(expectedSize = musics.size)

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER).performScrollTo().assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER_ITEM)
            .assertCountEquals(expectedSize = supporters.size)

        credits.forEach {
            openedUri = null
            onNodeWithText(text = it.id).performScrollTo().performClick()
            assertEquals(expected = it.link, actual = openedUri)
        }
    }
}
