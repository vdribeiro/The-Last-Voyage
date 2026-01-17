package com.hybris.tlv.command

import com.hybris.tlv.ui.navigation.Screen

/**
 * A navigation command that can be sent.
 */
internal sealed class Command {
    /**
     * Navigates to a specific [Screen].
     */
    data class Navigate(val screen: Screen): Command()
    /**
     * Navigates back to the previous screen.
     */
    data object Back: Command()
    /**
     * Toggles the audio player on or off.
     */
    data object ToggleAudio: Command()
}
