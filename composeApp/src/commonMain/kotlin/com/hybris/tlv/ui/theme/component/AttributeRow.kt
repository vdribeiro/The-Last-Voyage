package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun AttributeRow(
    modifier: Modifier = Modifier,
    name: String,
    minPoints: Int,
    maxPoints: Int,
    points: Int,
    canIncrement: Boolean,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(all = 8.dp),
            text = name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = onDecrement, enabled = points > minPoints) {
                Icon(
                    modifier = Modifier.size(size = 36.dp),
                    imageVector = Icons.Default.RemoveCircle,
                    contentDescription = "-$name",
                )
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(size = 80.dp),
                text = "$points",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )

            IconButton(onClick = onIncrement, enabled = canIncrement && points < maxPoints) {
                Icon(
                    modifier = Modifier.size(size = 36.dp),
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "+$name",
                )
            }
        }
    }
}
