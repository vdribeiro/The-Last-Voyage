package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.credits
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.credit.CreditState

@Preview
@Composable
private fun CreditNull() {
    val navigation = navigation(
        screen = Screen.CREDIT,
        state = CreditState()
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun CreditList() {
    val navigation = navigation(
        screen = Screen.CREDIT,
        state = CreditState(
            credits = credits
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}
