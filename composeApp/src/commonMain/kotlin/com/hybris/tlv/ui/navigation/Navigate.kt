package com.hybris.tlv.ui.navigation

/**
 * Navigation commands.
 */
internal sealed class Navigate {
    /**
     * Navigates to a specific [Screen].
     */
    data class To(val screen: Screen): Navigate()
    /**
     * Navigates back to the previous screen.
     */
    data object Back: Navigate()
}