package com.hybris.tlv.ui.theme.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun AttributeRow(
    modifier: Modifier = Modifier,
    name: String? = null,
    canIncrement: Boolean = true,
    max: Int? = null,
    min: Int? = null,
    value: Int? = null,
    increment: () -> Unit = {},
    decrement: () -> Unit = {}
) {
    val typography = LocalTypography.current

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            style = typography.titleLarge,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (value != null && min != null) Button(enabled = value > min, onClick = { decrement() }) {
                Icon(
                    modifier = Modifier.size(size = 36.dp),
                    imageVector = Icons.Default.RemoveCircle,
                    contentDescription = "-$name",
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(size = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value?.toString(),
                    style = typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }

            if (value != null && max != null) Button(enabled = canIncrement && value < max, onClick = { increment() }) {
                Icon(
                    modifier = Modifier.size(size = 36.dp),
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "+$name",
                )
            }
        }
    }
}

@Preview
@Composable
private fun AttributeRowPreview() = Preview {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        AttributeRow(
            name = "Power",
            canIncrement = true,
            max = 10000,
            min = 0,
            value = 9000
        )
        AttributeRow(
            canIncrement = false,
            max = 10000,
            min = 0,
            value = 1000
        )
        AttributeRow(
            canIncrement = true,
            max = 10000,
            min = 0,
            value = 0
        )
        AttributeRow(
            name = "Power",
            canIncrement = false,
            max = 10000,
            min = 0,
            value = 0
        )
        AttributeRow(name = "Power")
    }
}
