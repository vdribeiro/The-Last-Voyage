package com.hybris.tlv.ui.theme.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography

@Composable
internal fun InfoRow(
    modifier: Modifier = Modifier,
    label: String? = null,
    value: Any? = null,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTypography.current.bodyLarge
) {
    val annotatedText = if (label != null || value != null) {
        buildAnnotatedString {
            label?.let { withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(text = label) } }
            if (label != null && value != null) withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(text = ": ") }
            val stringValue = value?.toString().orEmpty()
            if (stringValue.isNotBlank()) append(text = stringValue)
        }
    } else null
    Text(
        modifier = modifier,
        text = annotatedText,
        textAlign = textAlign,
        style = style,
    )
}

@Preview
@Composable
private fun InfoRowPreview() = AppTheme {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        InfoRow(
            label = "Label",
            value = "Value"
        )
        InfoRow(
            label = "Label",
            value = "Value",
            textAlign = TextAlign.End
        )
        InfoRow(label = "Label")
        InfoRow(value = "Value")
        InfoRow()
    }
}
