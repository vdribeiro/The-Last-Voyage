package com.hybris.tlv.ui.theme.component.divider

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Divider(
    modifier: Modifier = Modifier,
    horizontal: Boolean = true,
) {
    when (horizontal) {
        true -> HorizontalDivider(modifier = modifier)
        false -> VerticalDivider(modifier = modifier)
    }
}

@Preview
@Composable
private fun DividerPreview() = AppTheme {
    Divider()
}
