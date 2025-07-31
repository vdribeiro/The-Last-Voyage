package com.hybris.tlv.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
internal fun ControlPanel(
    modifier: Modifier,
    enabled: Boolean,
    onSearch: (String) -> Unit,
    viewName: String,
    onChangeView: () -> Unit,
    properties: List<String>,
    selectedProperty: String,
    ascending: Boolean,
    onSortChange: (String) -> Unit,
    onSortDirectionChange: () -> Unit,
    visibleProperties: List<String>,
    onVisibilityChange: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf(value = "") }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { searchQuery }
            .debounce(timeoutMillis = 300L)
            .distinctUntilChanged()
            .collect { onSearch(it) }
    }

    Surface(modifier = modifier.fillMaxWidth()) {
        Column {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                enabled = enabled,
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    enabled = enabled,
                    onClick = {
                        searchQuery = ""
                        onChangeView()
                    }
                ) {
                    Text(text = viewName)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
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
        IconButton(
            enabled = enabled,
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Sort Options"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            properties.forEach { property ->
                DropdownMenuItem(
                    enabled = enabled,
                    text = { Text(text = property) },
                    onClick = {
                        onSortChange(property)
                        expanded = false
                    },
                    trailingIcon = {
                        if (selectedProperty == property) {
                            Icon(
                                imageVector = sortDirectionIcon,
                                contentDescription = "Sort Direction"
                            )
                        }
                    }
                )
            }
        }
    }
    Box {
        IconButton(
            enabled = enabled,
            onClick = { onSortDirectionChange() }
        ) {
            Icon(
                imageVector = sortDirectionIcon,
                contentDescription = "Sort Directions"
            )
        }
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
        IconButton(
            enabled = enabled,
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = "Visibility Options"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            properties.forEach { property ->
                DropdownMenuItem(
                    enabled = enabled,
                    text = { Text(text = property) },
                    onClick = { onVisibilityChange(property) },
                    leadingIcon = {
                        if (visibleProperties.contains(element = property)) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Visible"
                            )
                        } else Spacer(Modifier.size(24.dp))
                    }
                )
            }
        }
    }
}