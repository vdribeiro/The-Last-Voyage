package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import com.hybris.tlv.App
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.splash.SplashState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun SplashNull() {
    val navigation = navigation(
        screen = Screen.SPLASH,
        stateBuilder = SplashState()
    )
    App(navigation = navigation)
}

@Preview
@Composable
private fun SplashHalfway() {
    val navigation = navigation(
        screen = Screen.SPLASH,
        stateBuilder = SplashState(
            progress = 0.5f
        )
    )
    App(navigation = navigation)
}

@Preview
@Composable
private fun SplashFull() {
    val navigation = navigation(
        screen = Screen.SPLASH,
        stateBuilder = SplashState(
            progress = 1.0f
        )
    )
    App(navigation = navigation)
}
