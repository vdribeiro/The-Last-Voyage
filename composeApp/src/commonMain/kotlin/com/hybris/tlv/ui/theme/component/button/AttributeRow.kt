package com.hybris.tlv.ui.theme.component.button

import org.jetbrains.compose.ui.tooling.preview.Preview
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun AttributeRow(
    modifier: Modifier = Modifier,
    name: String? = null,
    canIncrement: Boolean = true,
    attributePoint: AttributePoint = AttributePoint()
) {
    val typography = LocalTypography.current

    val value = attributePoint.value
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(all = 2.dp),
            text = name,
            style = typography.titleLarge,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(enabled = value > attributePoint.min, onClick = { attributePoint.decrement() }) {
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
                    text = "$value",
                    style = typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }

            Button(enabled = canIncrement && value < attributePoint.max, onClick = { attributePoint.increment() }) {
                Icon(
                    modifier = Modifier.size(size = 36.dp),
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "+$name",
                )
            }
        }
    }
}

internal data class AttributePoint(
    val max: Int = 10,
    val min: Int = 0,
    val interval: Int = 1,
    val initialValue: Int = 0
) {
    init {
        if (max <= 0) throw IllegalArgumentException("max must be greater than 0")
        if (min < 0) throw IllegalArgumentException("min must be greater or equal to 0")
        if (max <= min) throw IllegalArgumentException("max must be greater than min")
        if (interval <= 0) throw IllegalArgumentException("interval must be greater than 0")
        if ((max - min) % interval != 0) throw IllegalArgumentException("The min-max range must be a multiple of the interval.")
    }

    private var _value: Int by mutableStateOf(value = initialValue.coerceIn(minimumValue = min, maximumValue = max))
    var value: Int
        get() = _value
        set(newValue) {
            _value = newValue.coerceIn(minimumValue = min, maximumValue = max)
        }
    val assignedPoints: Int get() = (value - min) / interval

    fun increment() {
        if (value < max) value += interval
    }

    fun decrement() {
        if (value > min) value -= interval
    }
}

@Preview
@Composable
private fun AttributeRowPreview() = AppTheme {
    Column {
        AttributeRow(
            name = "Power",
            canIncrement = true,
            attributePoint = AttributePoint(
                max = 10000,
                min = 0,
                interval = 100,
                initialValue = 9000
            )
        )
        AttributeRow(
            canIncrement = false,
            attributePoint = AttributePoint(
                max = 10000,
                min = 0,
                interval = 100,
                initialValue = 1000
            )
        )
        AttributeRow(
            canIncrement = true,
            attributePoint = AttributePoint(
                max = 10000,
                min = 0,
                interval = 100,
                initialValue = 0
            )
        )
        AttributeRow(
            canIncrement = false,
            attributePoint = AttributePoint(
                max = 10000,
                min = 0,
                interval = 100,
                initialValue = 0
            )
        )
    }
}
