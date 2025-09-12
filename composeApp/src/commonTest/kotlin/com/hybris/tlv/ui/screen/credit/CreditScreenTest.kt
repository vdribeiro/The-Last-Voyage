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
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.credits
import com.hybris.tlv.storeFactory
import com.hybris.tlv.usecase.credit.model.CreditType
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
internal class CreditScreenTest {

    @Test
    fun creditNull() = runComposeUiTest {
        val creditsMap = credits.groupBy { it.type }
        val creators = creditsMap[CreditType.CREATOR].orEmpty()
        val sources = creditsMap[CreditType.SOURCE].orEmpty()
        val musics = creditsMap[CreditType.MUSIC].orEmpty()
        val supporters = creditsMap[CreditType.SUPPORTER].orEmpty()

        setContent {
            CreditScreen(store = storeFactory.createCreditStore())
        }

        onNodeWithTag(testTag = CREDIT_SCREEN).assertExists()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST).assertExists()

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR).assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR_ITEM)
            .assertCountEquals(expectedSize = creators.size)

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE).assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE_ITEM)
            .assertCountEquals(expectedSize = sources.size)

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC).assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC_ITEM)
            .assertCountEquals(expectedSize = musics.size)

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER).assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER_ITEM)
            .assertCountEquals(expectedSize = supporters.size)
    }

    @Test
    fun creditEmpty() = runComposeUiTest {
        setContent {
            CreditScreen(
                store = storeFactory.createCreditStore(
                    state = CreditState(
                        credits = emptyList()
                    )
                )
            )
        }

        onNodeWithTag(testTag = CREDIT_SCREEN).assertExists()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST).assertExists()

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC).assertDoesNotExist()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER).assertDoesNotExist()
    }

    @Test
    fun creditList() = runComposeUiTest {
        val creditsMap = credits.groupBy { it.type }
        val creators = creditsMap[CreditType.CREATOR].orEmpty()
        val sources = creditsMap[CreditType.SOURCE].orEmpty()
        val musics = creditsMap[CreditType.MUSIC].orEmpty()
        val supporters = creditsMap[CreditType.SUPPORTER].orEmpty()

        var openedUri: String? = null
        val mockUriHandler = object: UriHandler {
            override fun openUri(uri: String) {
                openedUri = uri
            }
        }

        setContent {
            CompositionLocalProvider(value = LocalUriHandler provides mockUriHandler) {
                CreditScreen(
                    store = storeFactory.createCreditStore(
                        state = CreditState(
                            credits = credits
                        )
                    )
                )
            }
        }

        onNodeWithTag(testTag = CREDIT_SCREEN).assertExists()
        onNodeWithTag(testTag = CREDIT_SCREEN_LIST).assertExists()

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR).assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_CREATOR_ITEM)
            .assertCountEquals(expectedSize = creators.size)

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE).assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_SOURCE_ITEM)
            .assertCountEquals(expectedSize = sources.size)

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC).assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_MUSIC_ITEM)
            .assertCountEquals(expectedSize = musics.size)

        onNodeWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER).assertExists()
        onAllNodesWithTag(testTag = CREDIT_SCREEN_LIST_SUPPORTER_ITEM)
            .assertCountEquals(expectedSize = supporters.size)

        credits.forEach {
            openedUri = null
            onNodeWithText(text = it.id).apply {
                assertExists()
                performClick()
                assertEquals(expected = openedUri, actual = it.link)
            }
        }
    }
}
