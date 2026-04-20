package com.hybris.tlv.ui.screen.splash

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.core.platform.Platform
import com.hybris.tlv.core.platform.platform
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.audio.LocalAudioPlayer
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.image.AppLogo
import com.hybris.tlv.ui.theme.component.text.FadeInText
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun SplashScreen(store: Store<SplashState, SplashAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val currentContent = storeState.currentContent

    val audioPlayer = LocalAudioPlayer.current

    val loadingTranslation = getTranslation(key = "splash_screen__loading")

    Screen(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (platform == Platform.Web) audioPlayer.action(AudioPlayer.Action.Resume)
                store.send(action = SplashAction.Next)
            },
        contentAlignment = Alignment.Center,
        loading = storeState.loading,
        loadingDelayMillis = 0L,
        loadingText = loadingTranslation,
        loadingBackground = true,
        loadingProgress = storeState.progress,
        onBackClick = null,
        onHelpClick = null,
        onFeedbackClick = if (storeState.showFeedback) {
            { store.send(action = SplashAction.Feedback) }
        } else null
    ) {
        when (currentContent) {
            Content.SPLASH -> AppLogo(
                modifier = Modifier.align(alignment = Alignment.Center),
                showBackground = true,
                showProgress = true,
                progress = storeState.progress,
                text = getTranslation(key = "splash_screen__start")
            )

            Content.INTRO -> FadeInText(
                modifier = Modifier.padding(all = 16.dp),
                text = getTranslation(key = "splash_screen__intro")
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenZeroPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
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
private fun SplashScreenHalfwayPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
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
private fun SplashScreenFullPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "splash_screen__start",
                value = "Start"
            ),
        )
    )
    SplashScreen(
        store = Store(
            initialState = SplashState(
                loading = false,
                progress = 1.0f
            )
        )
    )
}
