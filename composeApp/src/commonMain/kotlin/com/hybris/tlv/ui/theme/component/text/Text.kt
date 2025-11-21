package com.hybris.tlv.ui.theme.component.text

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.hybris.tlv.ui.theme.AppTheme
import androidx.compose.material3.Text as MaterialText

@Composable
internal fun Text(
    modifier: Modifier = Modifier,
    text: String = "",
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
) {
    MaterialText(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        maxLines = maxLines,
        style = style,
        color = color,
        fontWeight = fontWeight,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
internal fun Text(
    modifier: Modifier = Modifier,
    text: AnnotatedString = AnnotatedString(text = ""),
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
) {
    MaterialText(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        maxLines = maxLines,
        style = style,
        color = color,
        fontWeight = fontWeight,
    )
}

@Preview
@Composable
private fun TextPreview() = AppTheme {
    Text(text = "Text")
}
