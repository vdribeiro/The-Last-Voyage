package com.hybris.tlv.ui.theme.component.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalTypography

@Composable
internal fun FadeInText(
    modifier: Modifier = Modifier,
    text: String? = null,
    duration: Int = 2500
) {
    val typography = LocalTypography.current

    val isPreview = LocalInspectionMode.current
    var visible by remember { mutableStateOf(value = isPreview) }
    if (!isPreview) LaunchedEffect(key1 = Unit) { visible = true }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = duration))
    ) {
        Text(
            modifier = modifier
                .verticalScroll(state = rememberScrollState()),
            text = text,
            textAlign = TextAlign.Center,
            style = typography.titleLarge,
        )
    }
}

@Preview
@Composable
private fun FadeInTextPreview() = Preview {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        FadeInText(text = "Fade Text")
        FadeInText()
    }
}
