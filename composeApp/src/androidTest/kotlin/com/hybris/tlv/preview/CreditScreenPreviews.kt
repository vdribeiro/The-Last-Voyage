package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.screen.credit.CreditScreen
import com.hybris.tlv.ui.screen.credit.CreditState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun CreditLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        CreditScreen(
            store = getStore(
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
            store = getStore(
                initialState = CreditState(
                    loading = false,
                    credits = credits
                )
            )
        )
    }
}
