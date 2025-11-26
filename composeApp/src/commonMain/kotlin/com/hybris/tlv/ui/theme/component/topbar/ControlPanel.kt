package com.hybris.tlv.ui.theme.component.topbar

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalShapes
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.alpha
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.button.Dropdown
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Input
import com.hybris.tlv.ui.theme.component.text.Text

@OptIn(FlowPreview::class)
@Composable
internal fun ControlPanel(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    search: String = "",
    onSearch: (String) -> Unit = {},
    viewName: String? = null,
    viewIcon: ImageVector? = null,
    onChangeView: () -> Unit = {},
    count: Int? = null,
    properties: List<String> = emptyList(),
    selectedProperty: String? = null,
    ascending: Boolean = true,
    onSortChange: (String) -> Unit = {},
    onSortDirectionChange: () -> Unit = {},
    visibleProperties: List<String> = emptyList(),
    onVisibilityChange: (String) -> Unit = {},
    selectedProperties: List<String> = emptyList(),
    onFiltersChange: (String) -> Unit = {}
) {
    val shapes = LocalShapes.current
    val typography = LocalTypography.current

    var searchQuery by remember { mutableStateOf(value = search) }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { searchQuery }
            .debounce(timeoutMillis = 300L)
            .distinctUntilChanged()
            .collect { onSearch(it) }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Input(
                modifier = Modifier.weight(weight = 1f),
                enabled = enabled,
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                maxLines = 1,
                style = typography.bodyLarge
            )
            if (properties.isNotEmpty()) SearchMenu(
                enabled = enabled,
                properties = properties,
                selectedProperties = selectedProperties,
                onFiltersChange = onFiltersChange
            )
        }
        if (viewIcon != null || viewName != null || count != null || properties.isNotEmpty()) Spacer(modifier = Modifier.height(height = 4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .alpha(alpha = alpha(enabled = enabled))
                    .clip(shape = shapes.large)
                    .clickable(
                        enabled = enabled,
                        onClick = {
                            onChangeView()
                            searchQuery = ""
                        },
                    )
            ) {
                viewIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = "View"
                    )
                }
                if (viewIcon != null && viewName != null) Spacer(modifier = Modifier.width(width = 8.dp))
                viewName?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        style = typography.labelLarge
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                count?.toString()?.let {
                    Text(
                        modifier = Modifier.alpha(alpha = alpha(enabled = enabled)),
                        text = it,
                        maxLines = 1,
                        style = typography.labelLarge
                    )
                }
                if (count != null && properties.isNotEmpty()) Spacer(modifier = Modifier.width(width = 8.dp))
                if (properties.isNotEmpty()) {
                    SortMenu(
                        enabled = enabled,
                        properties = properties,
                        selectedProperty = selectedProperty,
                        ascending = ascending,
                        onSortChange = onSortChange,
                        onSortDirectionChange = onSortDirectionChange

                    )
                    VisibilityMenu(
                        enabled = enabled,
                        properties = properties,
                        visibleProperties = visibleProperties,
                        onVisibilityChange = onVisibilityChange
                    )
                }
            }
        }
    }
}

private data class DropdownItem(
    val enabled: Boolean = true,
    val text: String? = null,
    val onClick: () -> Unit = {},
    val leadingIcon: @Composable (() -> Unit)? = null,
)

@Composable
private fun SearchMenu(
    enabled: Boolean,
    properties: List<String>,
    selectedProperties: List<String>,
    onFiltersChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(
            enabled = enabled,
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ManageSearch,
                contentDescription = "Search Filters"
            )
        }
        Dropdown(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = properties.map {
                DropdownItem(
                    enabled = enabled,
                    text = it,
                    onClick = { onFiltersChange(it) },
                    leadingIcon = {
                        if (selectedProperties.contains(element = it)) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Checked"
                            )
                        } else Spacer(modifier = Modifier.size(size = 24.dp))
                    }
                )
            },
            enabled = { it.enabled },
            text = { it.text },
            onClick = { it.onClick() },
            leadingIcon = { it.leadingIcon }
        )
    }
}

@Composable
private fun SortMenu(
    enabled: Boolean,
    properties: List<String>,
    selectedProperty: String?,
    ascending: Boolean,
    onSortChange: (String) -> Unit,
    onSortDirectionChange: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val sortDirectionIcon = if (ascending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
    Box {
        Button(
            enabled = enabled,
            onClick = { onSortDirectionChange() }
        ) {
            Icon(
                imageVector = sortDirectionIcon,
                contentDescription = "Sort Directions"
            )
        }
    }
    Box {
        Button(
            enabled = enabled,
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Sort Options"
            )
        }
        Dropdown(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = properties.map {
                DropdownItem(
                    enabled = enabled,
                    text = it,
                    onClick = {
                        onSortChange(it)
                        expanded = false
                    },
                    leadingIcon = {
                        if (selectedProperty == it) {
                            Icon(
                                imageVector = sortDirectionIcon,
                                contentDescription = "Sort Direction"
                            )
                        }
                    }
                )
            },
            enabled = { it.enabled },
            text = { it.text },
            onClick = { it.onClick() },
            leadingIcon = { it.leadingIcon }
        )
    }
}

@Composable
private fun VisibilityMenu(
    enabled: Boolean,
    properties: List<String>,
    visibleProperties: List<String>,
    onVisibilityChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(
            enabled = enabled,
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = "Visibility Options"
            )
        }
        Dropdown(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = properties.map {
                DropdownItem(
                    enabled = enabled,
                    text = it,
                    onClick = { onVisibilityChange(it) },
                    leadingIcon = {
                        if (visibleProperties.contains(element = it)) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Visible"
                            )
                        } else Spacer(modifier = Modifier.size(size = 24.dp))
                    }
                )
            },
            enabled = { it.enabled },
            text = { it.text },
            onClick = { it.onClick() },
            leadingIcon = { it.leadingIcon }
        )
    }
}

@Preview
@Composable
private fun ControlPanelPreview() = AppTheme {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        ControlPanel(
            enabled = true,
            search = "Search",
            viewName = "Planets",
            viewIcon = Icons.Default.Public,
            count = 2000,
            properties = listOf("Name", "Status", "Habitability", "Confidence"),
            selectedProperty = "Name",
            ascending = true,
            visibleProperties = listOf("Name", "Status"),
            selectedProperties = listOf("Status")
        )
        ControlPanel(
            enabled = false,
            search = "Search",
            viewName = "Planets",
            viewIcon = Icons.Default.Public,
            count = 2000,
            properties = listOf("Name", "Status", "Habitability", "Confidence"),
            ascending = true,
        )
        ControlPanel(
            enabled = true,
            search = "Search",
            viewName = "Planets",
            count = 2000,
            ascending = false,
        )
        ControlPanel(viewIcon = Icons.Default.Public)
        ControlPanel()
    }
}
