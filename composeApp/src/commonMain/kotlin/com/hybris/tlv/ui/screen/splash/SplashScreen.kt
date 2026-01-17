package com.hybris.tlv.ui.screen.splash

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.theme.AppTheme
import com.hybris.tlv.theme.component.text.FadeInText
import com.hybris.tlv.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun SplashScreen(store: Store<SplashState, SplashAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val currentContent = storeState.currentContent

    val loadingTranslation = getTranslation(key = "splash_screen__loading")

    Screen(
        store = store,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                when (currentContent) {
                    Content.SPLASH -> {}
                    Content.INTRO -> store.send(action = SplashAction.Next)
                }
            },
        contentAlignment = Alignment.Center,
        loading = storeState.loading,
        loadingDelayMillis = 0L,
        loadingText = loadingTranslation,
        loadingBackground = true,
        loadingProgress = storeState.progress,
        back = false,
        help = false,
        music = false,
        feedback = false
    ) {
        when (currentContent) {
            Content.SPLASH -> {}
            Content.INTRO -> FadeInText(
                modifier = Modifier.padding(all = 16.dp),
                text = getTranslation(key = "splash_screen__intro")
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenZeroPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "splash_screen__loading",
                value = "Loading..."
            ),
        )
    )
    SplashScreen(
        store = Store(
            initialState = SplashState(
                progress = 0.0f
            )
        )
    )
}

@Preview
@Composable
private fun SplashScreenHalfwayPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "splash_screen__loading",
                value = "Loading..."
            ),
        )
    )
    SplashScreen(
        store = Store(
            initialState = SplashState(
                progress = 0.5f
            )
        )
    )
}

@Preview
@Composable
private fun SplashScreenFullPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "splash_screen__loading",
                value = "Loading..."
            ),
        )
    )
    SplashScreen(
        store = Store(
            initialState = SplashState(
                progress = 1.0f
            )
        )
    )
}
