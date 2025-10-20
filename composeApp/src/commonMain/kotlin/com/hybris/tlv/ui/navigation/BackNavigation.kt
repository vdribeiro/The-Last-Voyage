package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A composable modifier that sets up back navigation handlers.
 */
@Composable
internal expect fun Modifier.backNavigation(onBack: () -> Unit): Modifier
