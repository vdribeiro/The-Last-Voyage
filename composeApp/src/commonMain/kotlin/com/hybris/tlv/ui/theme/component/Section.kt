package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.colorScheme
import com.hybris.tlv.ui.theme.typography

internal data class Section(
    val title: String,
    val description: String
)

@Composable
internal fun Section(
    modifier: Modifier = Modifier,
    title: String,
    sections: List<Section>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 24.dp),
    ) {
        item {
            Text(
                text = title,
                style = typography.headlineLarge,
            )
        }
        items(items = sections, key = { it.title }) { section ->
            Text(
                text = section.title,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
            )
            Spacer(Modifier.height(height = 8.dp))
            Text(
                text = section.description,
                style = typography.bodyLarge,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}