package com.hybris.tlv.ui.component

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable

internal data class LazyListIndex(
    val index: Int = 0,
    val scrollOffset: Int = 0
) {
    @Composable
    fun getState() = rememberLazyListState(
        initialFirstVisibleItemIndex = index,
        initialFirstVisibleItemScrollOffset = scrollOffset
    )
}
