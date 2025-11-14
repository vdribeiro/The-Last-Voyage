package com.hybris.tlv.ui.theme.component.list

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalShapes
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun LazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    scrollBar: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(all = 0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    scrollBarMinimalHeight: Dp = 64.dp,
    scrollBarThickness: Dp = 8.dp,
    scrollBarShape: Shape = LocalShapes.current.extraSmall,
    scrollBarHoverDurationMillis: Int = 300,
    scrollBarHoverColor: Color = Color.White,
    scrollBarUnhoverColor: Color = scrollBarHoverColor.copy(alpha = 0.3f),
    content: LazyListScope.() -> Unit = {}
) {
    Box {
        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            overscrollEffect = overscrollEffect,
            content = content
        )
        if (scrollBar) VerticalScrollBar(
            modifier = Modifier
                .padding(all = 4.dp)
                .fillMaxHeight()
                .align(Alignment.CenterEnd),
            state = state,
            minimalHeight = scrollBarMinimalHeight,
            thickness = scrollBarThickness,
            shape = scrollBarShape,
            hoverDurationMillis = scrollBarHoverDurationMillis,
            hoverColor = scrollBarHoverColor,
            unhoverColor = scrollBarUnhoverColor
        )
    }
}

@Preview
@Composable
private fun LazyColumnPreview() = AppTheme {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        scrollBar = true
    ) {
        item { Card { Text(text = "Preview") } }
    }
}
