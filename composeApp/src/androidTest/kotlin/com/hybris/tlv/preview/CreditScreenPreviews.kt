package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.credits
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.translations
import com.hybris.tlv.ui.screen.credit.CreditScreen
import com.hybris.tlv.ui.screen.credit.CreditState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun CreditLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        CreditScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = CreditState(
                    loading = true,
                    credits = emptyList()
                )
            )
        )
    }
}

@Preview
@Composable
private fun CreditList() {
    TranslationCache.set(translations = translations)
    AppTheme {
        CreditScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = CreditState(
                    loading = false,
                    credits = credits
                )
            )
        )
    }
}
