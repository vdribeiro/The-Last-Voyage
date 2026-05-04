package com.hybris.tlv.ui.screen.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.hybris.tlv.domain.credit.CreditType
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.count

internal class CreditScreenTest: TestCase() {

    @Test
    fun creditWithoutData() = runUITest {
        val store = storeFactory.get().getCreditStore()
        setUI { CreditScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "credit_screen__creators").assertDoesNotExist()
        onNodeWithText(text = "credit_screen__sources").assertDoesNotExist()
        onNodeWithText(text = "credit_screen__music").assertDoesNotExist()
        onNodeWithText(text = "credit_screen__supporters").assertDoesNotExist()
        onNodeWithTag(testTag = "credits_grid").assertIsDisplayed()
        onNodeWithTag(testTag = "credits_grid").count(count = 0)
    }

    @Test
    fun creditWithData() = runUITest {
        var openedUri: String?
        val mockUriHandler = object: UriHandler {
            override fun openUri(uri: String) {
                openedUri = uri
            }
        }

        dependency.get().useCases.credit.prepopulateCredits()
        val store = storeFactory.get().getCreditStore()
        setUI(compositionValues = listOf(LocalUriHandler provides mockUriHandler)) { CreditScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "credit_screen__creators").assertIsDisplayed()
        onNodeWithText(text = "credit_screen__sources").assertIsDisplayed()
        onNodeWithText(text = "credit_screen__music").assertIsDisplayed()
        onNodeWithText(text = "credit_screen__supporters").assertIsDisplayed()
        onNodeWithTag(testTag = "credits_grid").assertIsDisplayed()
        onNodeWithTag(testTag = "credits_grid").count(count = 9)

        val creditsMap = FakeData.credits.get().groupBy { it.type }
        creditsMap[CreditType.CREATOR].orEmpty()
        creditsMap[CreditType.SOURCE].orEmpty()
        creditsMap[CreditType.MUSIC].orEmpty()
        creditsMap[CreditType.SUPPORTER].orEmpty()

        FakeData.credits.get().forEach {
            openedUri = null
            onNodeWithText(text = it.id).performScrollTo().performClick()
            assertEquals(expected = it.link, actual = openedUri)
        }
    }
}
