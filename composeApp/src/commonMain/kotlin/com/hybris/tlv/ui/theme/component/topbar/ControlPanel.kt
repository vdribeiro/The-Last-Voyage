package com.hybris.tlv.ui.theme.component.topbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
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
import com.hybris.tlv.ui.theme.alpha
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.button.Dropdown
import com.hybris.tlv.ui.theme.component.button.DropdownItem
import com.hybris.tlv.ui.theme.component.container.Surface
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.image.defaultIcon
import com.hybris.tlv.ui.theme.component.text.Input
import com.hybris.tlv.ui.theme.component.text.Text
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(FlowPreview::class)
@Composable
internal fun ControlPanel(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    search: String = "",
    onSearch: (String) -> Unit = {},
    viewName: String = "",
    viewIcon: ImageVector = defaultIcon,
    onChangeView: () -> Unit = {},
    count: String = "0",
    properties: List<String> = emptyList(),
    selectedProperty: String = "",
    ascending: Boolean = true,
    onSortChange: (String) -> Unit = {},
    onSortDirectionChange: () -> Unit = {},
    visibleProperties: List<String> = emptyList(),
    onVisibilityChange: (String) -> Unit = {},
    selectedProperties: List<String> = emptyList(),
    onFiltersChange: (String) -> Unit = {}
) {
    val shapes = LocalShapes.current

    var searchQuery by remember { mutableStateOf(value = search) }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { searchQuery }
            .debounce(timeoutMillis = 300L)
            .distinctUntilChanged()
            .collect { onSearch(it) }
    }

    Surface(modifier = modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Input(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(horizontal = 8.dp),
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
                )
                SearchMenu(
                    enabled = enabled,
                    properties = properties,
                    selectedProperties = selectedProperties,
                    onFiltersChange = onFiltersChange
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
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
                        .padding(all = 8.dp)
                ) {
                    Icon(
                        imageVector = viewIcon,
                        contentDescription = "View"
                    )
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    Text(text = viewName, maxLines = 1)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .alpha(alpha = alpha(enabled = enabled)),
                        text = count,
                        maxLines = 1
                    )
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
            }
        )
    }
}

@Composable
private fun SortMenu(
    enabled: Boolean,
    properties: List<String>,
    selectedProperty: String,
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
            }
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
            }
        )
    }
}

@Preview
@Composable
private fun ControlPanelPreview() = AppTheme {
    ControlPanel()
}
