package com.hybris.tlv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics

internal val LocalTesting = compositionLocalOf { false }

/**
 * Conditionally applies 'mergeDescendants' if the composable is currently running in a test environment.
 * This indicates that the owning component and its descendants should be treated as one logical entity. This makes them easier to find in tests.
 */
@Composable
fun Modifier.mergeDescendants(): Modifier =
    if (LocalTesting.current) semantics(mergeDescendants = true) {} else this
