package com.hybris.tlv.ui.theme.component.progress

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme

@Composable
internal fun ProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null
) {
    when {
        progress != null -> {
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            )
            CircularProgressIndicator(
                modifier = modifier,
                progress = { animatedProgress },
            )
        }

        else -> CircularProgressIndicator(modifier = modifier)
    }
}

@Preview
@Composable
private fun ProgressIndicatorPreview() = AppTheme {
    ProgressIndicator()
}
