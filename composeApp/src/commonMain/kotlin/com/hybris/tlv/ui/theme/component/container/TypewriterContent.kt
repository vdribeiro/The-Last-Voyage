package com.hybris.tlv.ui.theme.component.container

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.component.text.TypewriterText
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TypewriterContent(
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    content: @Composable () -> Unit = {}
) {
    val typography = LocalTypography.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        title?.let {
            Text(
                text = it,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(height = 16.dp))
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
private fun TypewriterContentPreview() = AppTheme {
    TypewriterContent(
        title = "Title",
        text = "Text"
    )
}
