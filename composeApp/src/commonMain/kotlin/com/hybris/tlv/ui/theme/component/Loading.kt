package com.hybris.tlv.ui.theme.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
internal fun Loading(
    modifier: Modifier = Modifier,
    delayMillis: Long = 300L,
    progress: Float? = null,
    text: String? = null,
) {
    var show by remember { mutableStateOf(value = false) }
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = delayMillis)
        show = true
    }
    if (!show) return

    AppLogo(
        modifier = modifier,
        showBackground = true,
        text = text
    )
    // Circular progress around the app logo
    when {
        progress != null -> {
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            )
            CircularProgressIndicator(
                modifier = Modifier.size(size = 160.dp),
                progress = { animatedProgress },
            )
        }

        else -> CircularProgressIndicator(modifier = Modifier.size(size = 160.dp))
    }
}
