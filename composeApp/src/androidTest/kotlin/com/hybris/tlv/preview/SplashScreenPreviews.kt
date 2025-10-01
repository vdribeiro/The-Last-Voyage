package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import com.hybris.tlv.getStore
import com.hybris.tlv.ui.screen.splash.SplashScreen
import com.hybris.tlv.ui.screen.splash.SplashState
import com.hybris.tlv.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun SplashZero() {
    AppTheme {
        SplashScreen(
            store = getStore(
                initialState = SplashState(
                    progress = 0.0f
                )
            )
        )
    }
}

@Preview
@Composable
private fun SplashHalfway() {
    AppTheme {
        SplashScreen(
            store = getStore(
                initialState = SplashState(
                    progress = 0.5f
                )
            )
        )
    }
}

@Preview
@Composable
private fun SplashFull() {
    AppTheme {
        SplashScreen(
            store = getStore(
                initialState = SplashState(
                    progress = 1.0f
                )
            )
        )
    }
}
