package com.hybris.tlv.ui.theme.component.text

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography

@Composable
internal fun InfoRow(
    modifier: Modifier = Modifier,
    label: String = "",
    value: Any? = null,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTypography.current.bodyLarge
) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(label) }
        val stringValue = value?.toString().orEmpty()
        if (stringValue.isNotBlank()) {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(": ") }
            append(stringValue)
        }
    }
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
    InfoRow(
        label = "Label",
        value = "Value"
    )
}
