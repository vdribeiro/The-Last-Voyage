package com.hybris.tlv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics

internal val LocalTesting = compositionLocalOf { false }

/**
 * Conditionally applies `semantics { mergeDescendants = false }` if the composable is currently running in a test environment.
 * This prevents UI elements from being merged, making them easier to find in tests.
 */
@Composable
fun Modifier.mergeDescendants(): Modifier =
    if (LocalTesting.current) semantics(mergeDescendants = true) {} else this
