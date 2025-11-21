package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.text.Text
import androidx.compose.material3.Surface as MaterialSurface

@Composable
internal fun Surface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    MaterialSurface(modifier = modifier, content = content)
}

@Preview
@Composable
private fun SurfacePreview() = AppTheme {
    Surface {
        Row(
            modifier = Modifier.padding(all = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Content")
        }
    }
}
