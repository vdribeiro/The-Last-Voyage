package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.MockNavigation
import com.hybris.tlv.ui.screen.splash.SplashScreen
import com.hybris.tlv.ui.screen.splash.SplashState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun SplashZero() {
    TranslationCache.set(translations = translations)
    AppTheme {
        SplashScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                audioPlayer = AudioPlayer(),
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
    TranslationCache.set(translations = translations)
    AppTheme {
        SplashScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                audioPlayer = AudioPlayer(),
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
    TranslationCache.set(translations = translations)
    AppTheme {
        SplashScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                audioPlayer = AudioPlayer(),
                initialState = SplashState(
                    progress = 1.0f
                )
            )
        )
    }
}
