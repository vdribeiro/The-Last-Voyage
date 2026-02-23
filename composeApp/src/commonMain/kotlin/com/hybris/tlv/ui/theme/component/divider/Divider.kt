package com.hybris.tlv.ui.theme.component.divider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview

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
private fun DividerPreview() = Preview {
    Column {
        Divider(modifier = Modifier.padding(all = 4.dp))
        Divider(modifier = Modifier.padding(all = 4.dp), horizontal = false)
    }
}
