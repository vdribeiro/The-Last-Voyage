package com.hybris.tlv.ui.screen.splash

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.screen.Screen
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SplashScreen(store: Store<SplashState, SplashAction>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent

    val loadingTranslation = getTranslation(key = "splash_screen__loading")

    Screen(
        modifier = Modifier.testTag(tag = SPLASH_SCREEN),
        loading = storeState.loading,
        loadingDelayMillis = 0L,
        loadingProgress = storeState.progress,
        loadingText = loadingTranslation
    ) {
        when (currentContent) {
            Content.SPLASH -> {}
            Content.INTRO -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 32.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { store.send(action = SplashAction.Next) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 32.dp),
                    text = getTranslation(key = "splash_screen__intro"),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun SplashZeroPreview() = AppTheme {
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
private fun SplashHalfwayPreview() = AppTheme {
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
private fun SplashFullPreview() = AppTheme {
    SplashScreen(
        store = getStore(
            initialState = SplashState(
                progress = 1.0f
            )
        )
    )
}
