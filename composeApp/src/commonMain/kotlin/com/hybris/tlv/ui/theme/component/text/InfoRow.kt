package com.hybris.tlv.ui.theme.component.text

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography

@Composable
internal fun InfoRow(
    modifier: Modifier = Modifier,
    label: String = "",
    value: Any? = null
) {
    val typography = LocalTypography.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(label) }
        value?.let {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(": ") }
            append("$it")
        }
    }
    Text(
        modifier = modifier,
        text = annotatedText,
        style = typography.bodyLarge,
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
