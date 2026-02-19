package com.hybris.tlv.ui.theme.component.bottombar

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
import com.hybris.tlv.core.security.uuid
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.list.LazyColumn

@Composable
internal fun ButtonsBar(
    modifier: Modifier = Modifier,
    buttons: List<BottomButton> = emptyList(),
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        scrollBar = false
    ) {
        items(items = buttons, key = { it.id }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    loading = it.loading,
                    enabled = it.enabled,
                    text = it.text,
                    onClick = { it.onClick() },
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
            BottomButton(text = "Button 1"),
            BottomButton(enabled = false, text = "Button 2"),
            BottomButton(),
        ),
    )
}

internal data class BottomButton(
    val id: String = uuid(),
    val loading: Boolean = false,
    val enabled: Boolean = true,
    val text: String? = null,
    val onClick: () -> Unit = {}
)
