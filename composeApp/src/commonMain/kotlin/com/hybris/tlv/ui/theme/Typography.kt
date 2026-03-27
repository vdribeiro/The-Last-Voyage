package com.hybris.tlv.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.hybris.tlv.data.resource.FontResource

private val bodyLarge = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.0.sp,
    letterSpacing = 0.5.sp,
)
private val bodyMedium = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.2.sp,
)
private val bodySmall = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.0.sp,
    letterSpacing = 0.4.sp,
)
private val displayLarge = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 57.sp,
    lineHeight = 64.0.sp,
    letterSpacing = (-0.2).sp,
)
private val displayMedium = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 45.sp,
    lineHeight = 52.0.sp,
    letterSpacing = 0.0.sp,
)
private val displaySmall = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 36.sp,
    lineHeight = 44.0.sp,
    letterSpacing = 0.0.sp,
)
private val headlineLarge = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 32.sp,
    lineHeight = 40.0.sp,
    letterSpacing = 0.0.sp,
)
private val headlineMedium = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 28.sp,
    lineHeight = 36.0.sp,
    letterSpacing = 0.0.sp,
)
private val headlineSmall = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
    lineHeight = 32.0.sp,
    letterSpacing = 0.0.sp,
)
private val labelLarge = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.1.sp,
)
private val labelMedium = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.0.sp,
    letterSpacing = 0.5.sp,
)
private val labelSmall = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.0.sp,
    letterSpacing = 0.5.sp,
)
private val titleLarge = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.0.sp,
    letterSpacing = 0.0.sp,
)
private val titleMedium = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.0.sp,
    letterSpacing = 0.2.sp,
)
private val titleSmall = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.1.sp,
)

private val typography = Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall,
)

internal val LocalTypography = staticCompositionLocalOf { typography }

@Composable
internal fun getTypography(): Typography {
    val inter = FontResource.Inter.family
    return Typography(
        displayLarge = displayLarge.copy(fontFamily = inter),
        displayMedium = displayMedium.copy(fontFamily = inter),
        displaySmall = displaySmall.copy(fontFamily = inter),
        headlineLarge = headlineLarge.copy(fontFamily = inter),
        headlineMedium = headlineMedium.copy(fontFamily = inter),
        headlineSmall = headlineSmall.copy(fontFamily = inter),
        titleLarge = titleLarge.copy(fontFamily = inter),
        titleMedium = titleMedium.copy(fontFamily = inter),
        titleSmall = titleSmall.copy(fontFamily = inter),
        bodyLarge = bodyLarge.copy(fontFamily = inter),
        bodyMedium = bodyMedium.copy(fontFamily = inter),
        bodySmall = bodySmall.copy(fontFamily = inter),
        labelLarge = labelLarge.copy(fontFamily = inter),
        labelMedium = labelMedium.copy(fontFamily = inter),
        labelSmall = labelSmall.copy(fontFamily = inter),
    )
}

