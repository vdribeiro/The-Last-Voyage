package com.hybris.tlv.ui.screen.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.AppLogo
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.ic_launcher_background

@Composable
internal fun SplashScreen(store: Store<SplashState, SplashAction>) {
    val storeState by store.stateFlow.collectAsState()
    val loadingTranslation = getTranslation(key = "splash_screen__loading")

    val typography = LocalTypography.current

    Scaffold(
        modifier = Modifier
            .testTag(tag = SPLASH_SCREEN)
            .fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // App logo with background
            Image(
                modifier = Modifier
                    .testTag(tag = SPLASH_SCREEN_LOGO_BACKGROUND)
                    .size(size = 160.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(resource = Res.drawable.ic_launcher_background),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
            )
            AppLogo(
                modifier = Modifier.testTag(tag = SPLASH_SCREEN_LOGO),
                size = 200,
                showText = false
            )

            // Circular progress around the app logo
            val animatedProgress by animateFloatAsState(
                targetValue = storeState.progress,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .testTag(tag = SPLASH_SCREEN_PROGRESS)
                    .size(size = 160.dp),
                progress = { animatedProgress },
            )

            // Loading text
            Text(
                modifier = Modifier
                    .testTag(tag = SPLASH_SCREEN_LOADING)
                    .align(alignment = Alignment.BottomCenter)
                    .padding(all = 64.dp),
                text = loadingTranslation,
                style = typography.headlineMedium,
                color = Color.White,
            )
        }
    }
}

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

