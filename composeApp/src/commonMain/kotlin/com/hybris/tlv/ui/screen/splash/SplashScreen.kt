package com.hybris.tlv.ui.screen.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.painterResource
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.ic_launcher_background
import thelastvoyage.composeapp.generated.resources.ic_launcher_foreground

@Composable
internal fun SplashScreen(store: Store<SplashAction, SplashState>) {
    val storeState by store.stateFlow.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(size = 160.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(resource = Res.drawable.ic_launcher_background),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
            )

            Image(
                modifier = Modifier
                    .size(size = 200.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
            )

            val animatedProgress by animateFloatAsState(
                targetValue = storeState.progress,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .size(160.dp),
                progress = { animatedProgress },
            )

            Text(
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(all = 64.dp),
                text = getTranslation(key = "splash_screen__loading"),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
            )
        }
    }
}
