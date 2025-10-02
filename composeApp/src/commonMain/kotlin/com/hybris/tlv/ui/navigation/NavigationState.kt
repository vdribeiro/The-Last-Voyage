package com.hybris.tlv.ui.navigation

/**
 * Current navigation state.
 */
internal data class NavigationState(
    val screen: Screen = Screen.Splash,
    val stateBuilder: Any? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NavigationState) return false
        return screen == other.screen
    }

    override fun hashCode(): Int = screen.hashCode()
}
