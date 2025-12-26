package com.hybris.tlv.screen.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.hybris.tlv.TestCase
import com.hybris.tlv.credits
import com.hybris.tlv.usecase.credit.model.CreditType

@OptIn(ExperimentalTestApi::class)
internal class CreditScreenTest: TestCase() {

    @Test
    fun creditWithoutData() = runUITest {
        val store = storeFactory.getCreditStore()
        setScreen { CreditScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "credit_screen__creators").assertDoesNotExist()
        onNodeWithText(text = "credit_screen__sources").assertDoesNotExist()
        onNodeWithText(text = "credit_screen__music").assertDoesNotExist()
        onNodeWithText(text = "credit_screen__supporters").assertDoesNotExist()
        onNodeWithTag(testTag = "credits_grid").assertIsDisplayed()
        onNodeWithTag(testTag = "credits_grid")
            .onChildren()
            .assertCountEquals(expectedSize = 0)
    }

    @Test
    fun creditWithData() = runUITest {
        var openedUri: String?
        val mockUriHandler = object: UriHandler {
            override fun openUri(uri: String) {
                openedUri = uri
            }
        }

        useCases.credit.prepopulateCredits()
        val store = storeFactory.getCreditStore()
        setScreen(LocalUriHandler provides mockUriHandler) { CreditScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "credit_screen__creators").assertIsDisplayed()
        onNodeWithText(text = "credit_screen__sources").assertIsDisplayed()
        onNodeWithText(text = "credit_screen__music").assertIsDisplayed()
        onNodeWithText(text = "credit_screen__supporters").assertIsDisplayed()
        onNodeWithTag(testTag = "credits_grid").assertIsDisplayed()
        onNodeWithTag(testTag = "credits_grid")
            .onChildren()
            .assertCountEquals(expectedSize = 9)

        val creditsMap = credits.groupBy { it.type }
        creditsMap[CreditType.CREATOR].orEmpty()
        creditsMap[CreditType.SOURCE].orEmpty()
        creditsMap[CreditType.MUSIC].orEmpty()
        creditsMap[CreditType.SUPPORTER].orEmpty()

        credits.forEach {
            openedUri = null
            onNodeWithText(text = it.id).performScrollTo().performClick()
            assertEquals(expected = it.link, actual = openedUri)
        }
    }
}
