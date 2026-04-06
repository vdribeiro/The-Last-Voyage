package com.hybris.tlv.ui.screen.splash

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.navigation.LocalNavController
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.navigate
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.text.FadeInText
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun SplashScreen(store: Store<SplashState, SplashAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val currentContent = storeState.currentContent

    val navController = LocalNavController.current

    val loadingTranslation = getTranslation(key = "splash_screen__loading")
    val startTranslation = getTranslation(key = "splash_screen__start")

    var isAnimationComplete by remember { mutableStateOf(value = false) }
    val loadingText = if (isAnimationComplete) startTranslation else loadingTranslation

    Screen(
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
        loadingText = loadingText,
        loadingBackground = true,
        loadingProgress = storeState.progress,
        onLoadingFinished = { isAnimationComplete = true },
        onBackClick = null,
        onHelpClick = null,
        onFeedbackClick = if (storeState.showFeedback) {
            { navController?.navigate(screen = Screen.Feedback(tag = null, message = null)) }
        } else null
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
private fun SplashScreenZeroPreview() = Preview {
    InjectTranslations(
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
private fun SplashScreenHalfwayPreview() = Preview {
    InjectTranslations(
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
private fun SplashScreenFullPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "splash_screen__start",
                value = "Start"
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
