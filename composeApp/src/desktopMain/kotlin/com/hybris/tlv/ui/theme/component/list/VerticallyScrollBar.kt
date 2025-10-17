package com.hybris.tlv.ui.theme.component.list

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Composable
internal actual fun VerticallyScrollBar(
    modifier: Modifier,
    state: LazyListState,
    minimalHeight: Dp,
    thickness: Dp,
    shape: Shape,
    hoverDurationMillis: Int,
    unhoverColor: Color,
    hoverColor: Color
) {
    val currentStyle = LocalScrollbarStyle.current
    VerticalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(scrollState = state),
        style = currentStyle.copy(
            minimalHeight = minimalHeight,
            thickness = thickness,
            shape = shape,
            hoverDurationMillis = hoverDurationMillis,
            unhoverColor = unhoverColor,
            hoverColor = hoverColor
        )
    )
}
