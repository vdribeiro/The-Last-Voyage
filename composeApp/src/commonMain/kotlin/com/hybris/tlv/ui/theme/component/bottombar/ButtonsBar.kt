package com.hybris.tlv.ui.theme.component.bottombar

import org.jetbrains.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.list.LazyColumn

@Composable
internal fun ButtonsBar(
    modifier: Modifier = Modifier,
    buttons: List<Pair<String, (() -> Unit)?>> = emptyList(),
) {
    LazyColumn(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        scrollBar = false
    ) {
        items(items = buttons, key = { it.first }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = it.second != null,
                    text = it.first,
                    onClick = it.second ?: {},
                )
            }
        }
        item { Spacer(modifier = Modifier.height(height = 16.dp)) }
    }
}

@Preview
@Composable
private fun ButtonsBarPreview() = AppTheme {
    ButtonsBar(
        buttons = listOf(
            "Button 1" to {},
            "Button 2" to {},
            "Button 3" to {},
        )
    )
}