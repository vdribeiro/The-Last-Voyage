package com.hybris.tlv.ui.theme.component.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.text.TypewriterText
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TypewriterScreen(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    onMusicClick: (() -> Unit)? = null,
    onFeedbackClick: (() -> Unit)? = null,
    topBar: @Composable () -> Unit = {},
    title: String? = null,
    text: String? = null,
    content: @Composable () -> Unit = {},
    buttons: List<Pair<String, () -> Unit>> = emptyList(),
) {
    val typography = LocalTypography.current

    Screen(
        modifier = modifier,
        loading = loading,
        onMusicClick = onMusicClick,
        onFeedbackClick = onFeedbackClick,
        topBar = topBar,
        bottomBar = {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 8.dp),
            ) {
                items(items = buttons, key = { it.first }) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                        onClick = it.second
                    ) {
                        Text(text = it.first)
                    }
                }
                item { Spacer(modifier = Modifier.height(height = 16.dp)) }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            title?.let {
                Text(
                    text = it,
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
            content()
            text?.let {
                TypewriterText(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxWidth(),
                    text = it
                )
            }
        }
    }
}

@Preview
@Composable
private fun TypewriterScreenPreview() = AppTheme {
    TypewriterScreen(
        title = "Title",
        text = "Text",
        buttons = listOf(
            "Button 1" to {},
            "Button 2" to {},
        )
    )
}
