package com.hybris.tlv.ui.theme.component.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.component.text.TypewriterText

@Composable
internal fun TypewriterContent(
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    content: @Composable () -> Unit = {}
) {
    val typography = LocalTypography.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        title?.let {
            Text(
                text = it,
                textAlign = TextAlign.Start,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        if (title != null && text != null) Spacer(modifier = Modifier.height(height = 16.dp))
        text?.let {
            TypewriterText(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxWidth(),
                text = it
            )
        }
        content()
    }
}

@Preview
@Composable
private fun TypewriterContentPreview() = Preview {
    TypewriterContent(
        title = "Title",
        text = "Text"
    )
}

@Preview
@Composable
private fun TypewriterContentTextPreview() = Preview {
    TypewriterContent(text = "Text")
}

@Preview
@Composable
private fun TypewriterContentWithContentPreview() = Preview {
    TypewriterContent(
        title = "Title",
        text = "Text",
        content = { Button(text = "Click") }
    )
}
