package com.hybris.tlv.usecase.translation.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.mock.translations
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class TranslationRemoteTest {

    @Test
    fun `get translations`() = runTest {
        assertEquals(expected = Result.Success(list = translations), actual = mock.translationApi.getTranslations())
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.translationApi.getTranslations() is Result.Error)
    }
}
