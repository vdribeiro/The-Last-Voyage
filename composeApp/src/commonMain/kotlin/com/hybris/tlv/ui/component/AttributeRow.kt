package com.hybris.tlv.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onDecrement, enabled = points > minPoints) {
            Icon(
                modifier = Modifier.size(size = 36.dp),
                imageVector = Icons.Default.RemoveCircle,
                contentDescription = "-$name",
            )
        }

        Column(
            modifier = Modifier.weight(weight = 1f).padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "$name: $points",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(height = 4.dp))
            val progress = if (minPoints >= maxPoints) 0.0f else (points.toFloat() - minPoints) / (maxPoints.toFloat() - minPoints)
            val animatedProgress by animateFloatAsState(targetValue = progress.coerceIn(minimumValue = 0.0f, maximumValue = 1.0f))
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().height(height = 8.dp),
                progress = { animatedProgress },
            )
        }

        IconButton(onClick = onIncrement, enabled = canIncrement && points < maxPoints) {
            Icon(
                modifier = Modifier.size(size = 36.dp),
                imageVector = Icons.Default.AddCircle,
                contentDescription = "+$name",
            )
        }
    }
}
