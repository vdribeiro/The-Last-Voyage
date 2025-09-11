package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.credit.CreditState
import com.hybris.tlv.usecase.translation.model.domain.Translation

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

private val translations = listOf(
    Translation(
        key = "key",
        value = "value"
    ),
)
