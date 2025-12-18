package com.hybris.tlv.screen.credit

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.credits
import com.hybris.tlv.getCreditStore
import com.hybris.tlv.reset
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.useCases
import com.hybris.tlv.usecase.credit.model.CreditType

@OptIn(ExperimentalTestApi::class)
internal class CreditScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun creditWithoutData() = runComposeUiTest {
        val store = getCreditStore()
        setContent {
            AppTheme {
                CreditScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = CREDIT_SCREEN).assertExists()
//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST).assertExists()
//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR).assertDoesNotExist()
//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR_ITEM).assertDoesNotExist()
//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE).assertDoesNotExist()
//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE_ITEM).assertDoesNotExist()
//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC).assertDoesNotExist()
//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC_ITEM).assertDoesNotExist()
//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER).assertDoesNotExist()
//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER_ITEM).assertDoesNotExist()
    }

    @Test
    fun creditWithData() = runComposeUiTest {
        var openedUri: String?
        val mockUriHandler = object: UriHandler {
            override fun openUri(uri: String) {
                openedUri = uri
            }
        }

        runBlocking { useCases.credit.syncCredits() }
        val store = getCreditStore()
        setContent {
            CompositionLocalProvider(value = LocalUriHandler provides mockUriHandler) {
                AppTheme {
                    CreditScreen(store = store)
                }
            }
        }
        waitForIdle()

        val creditsMap = credits.groupBy { it.type }
        creditsMap[CreditType.CREATOR].orEmpty()
        creditsMap[CreditType.SOURCE].orEmpty()
        creditsMap[CreditType.MUSIC].orEmpty()
        creditsMap[CreditType.SUPPORTER].orEmpty()

//        onNodeWithTag(testTag = CREDIT_SCREEN).assertExists()
//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST).assertExists()

//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR).performScrollTo().assertExists().assertTextEquals("credit_screen__creators")
//        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR_ITEM)
//            .assertCountEquals(expectedSize = creators.size)

//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE).performScrollTo().assertExists().assertTextEquals("credit_screen__sources")
//        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE_ITEM)
//            .assertCountEquals(expectedSize = sources.size)

//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC).performScrollTo().assertExists().assertTextEquals("credit_screen__music")
//        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC_ITEM)
//            .assertCountEquals(expectedSize = musics.size)

//        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER).performScrollTo().assertExists().assertTextEquals("credit_screen__supporters")
//        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER_ITEM)
//            .assertCountEquals(expectedSize = supporters.size)

        credits.forEach {
            openedUri = null
            onNodeWithText(text = it.id).performScrollTo().performClick()
            assertEquals(expected = it.link, actual = openedUri)
        }
    }
}
