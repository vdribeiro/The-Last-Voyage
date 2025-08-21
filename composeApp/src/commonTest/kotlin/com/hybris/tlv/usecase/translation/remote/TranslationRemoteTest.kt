package com.hybris.tlv.usecase.translation.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.translations
import com.hybris.tlv.usecase.SyncResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class TranslationRemoteTest {

    private val mock = Mock()

    @Test
    fun `write and get translations`() = runBlocking {
        assertEquals(expected = Result.Success(list = emptyList()), actual = mock.translationApi.getTranslations().last())
        assertEquals(expected = SyncResult.Success, actual = mock.translationApi.rewriteTranslations(translations = translations).last())
        assertEquals(expected = Result.Success(list = translations), actual = mock.translationApi.getTranslations().last())
    }
}
