package com.hybris.tlv.ui.theme.component.bottombar

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.list.LazyColumn

@Composable
internal fun <T> ButtonsBar(
    modifier: Modifier = Modifier,
    buttons: ImmutableList<T> = persistentListOf(),
    id: (T) -> String = { it.hashCode().toString() },
    text: @Composable (T) -> String? = { null },
    loading: (T) -> Boolean = { false },
    enabled: (T) -> Boolean = { true },
    onClick: (T) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        scrollBar = false
    ) {
        items(items = buttons, key = id) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    loading = loading(it),
                    enabled = enabled(it),
                    text = text(it),
                    onClick = { onClick(it) },
                )
            }
        }
        item { Spacer(modifier = Modifier.height(height = 16.dp)) }
    }
}

@Preview
@Composable
private fun ButtonsBarPreview() = Preview {
    ButtonsBar(
        buttons = persistentListOf(
            "Button 1",
            "Button 2",
            "Button 3",
        ),
        text = { it },
        loading = { it == "Button 2" },
        enabled = { it != "Button 3" }
    )
}
