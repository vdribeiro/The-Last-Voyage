package com.hybris.tlv.ui.navigation

import androidx.compose.ui.Modifier

/**
 * A modifier that sets up back navigation handlers.
 */
internal expect fun Modifier.backNavigation(onBack: () -> Unit): Modifier
