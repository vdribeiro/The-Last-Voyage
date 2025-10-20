package com.hybris.tlv.ui.screen.splash

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun SplashScreen(store: Store<SplashState, SplashAction>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent

    val translationVersion by TranslationCache.updateFlow.collectAsState()
    val loadingTranslation = remember(key1 = translationVersion) { getTranslation(key = "splash_screen__loading") }

    val typography = LocalTypography.current

    Screen(
        modifier = Modifier
            .testTag(tag = SPLASH_SCREEN)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                when (currentContent) {
                    Content.SPLASH -> {}
                    Content.INTRO -> store.send(action = SplashAction.Next)
                }
            },
        loading = storeState.loading,
        loadingDelayMillis = 0L,
        loadingText = loadingTranslation,
        loadingBackground = true,
        loadingProgress = storeState.progress,
    ) {
        when (currentContent) {
            Content.SPLASH -> {}
            Content.INTRO -> {
                val isPreview = LocalInspectionMode.current
                var visible by remember { mutableStateOf(value = isPreview) }
                LifecycleCoroutine(Unit) { visible = true }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(durationMillis = 2500))
                ) {
                    Text(
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .verticalScroll(state = rememberScrollState()),
                        text = getTranslation(key = "splash_screen__intro"),
                        textAlign = TextAlign.Center,
                        style = typography.titleLarge,
                    )
                }
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
