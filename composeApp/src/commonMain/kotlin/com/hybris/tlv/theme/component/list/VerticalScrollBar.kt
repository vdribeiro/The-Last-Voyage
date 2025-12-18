package com.hybris.tlv.theme.component.list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Composable
internal expect fun VerticalScrollBar(
    modifier: Modifier = Modifier,
    state: LazyListState,
    minimalHeight: Dp,
    thickness: Dp,
    shape: Shape,
    hoverDurationMillis: Int,
    hoverColor: Color,
    unhoverColor: Color
)
