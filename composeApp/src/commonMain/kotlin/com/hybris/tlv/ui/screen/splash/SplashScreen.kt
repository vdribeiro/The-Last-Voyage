package com.hybris.tlv.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SplashScreen(store: Store<SplashState, SplashAction>) {
    val storeState by store.stateFlow.collectAsState()
    val loadingTranslation = getTranslation(key = "splash_screen__loading")

    Screen(
        modifier = Modifier.testTag(tag = SPLASH_SCREEN),
        loading = true,
        loadingDelayMillis = 0L,
        loadingProgress = storeState.progress,
        loadingText = loadingTranslation
    )
}

@Preview
@Composable
private fun SplashZero() = AppTheme {
    SplashScreen(
        store = getStore(
            initialState = SplashState(
                progress = 0.0f
            )
        )
    )
}

@Preview
@Composable
private fun SplashHalfway() = AppTheme {
    SplashScreen(
        store = getStore(
            initialState = SplashState(
                progress = 0.5f
            )
        )
    )
}

@Preview
@Composable
private fun SplashFull() = AppTheme {
    SplashScreen(
        store = getStore(
            initialState = SplashState(
                progress = 1.0f
            )
        )
    )
}
