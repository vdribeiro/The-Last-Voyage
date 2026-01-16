package com.hybris.tlv.command

/**
 * A navigation command that can be sent.
 */
internal sealed class Command {
    /**
     * Navigates to a specific [com.hybris.tlv.ui.navigation.Screen].
     */
    data class Navigate(val screen: com.hybris.tlv.ui.navigation.Screen): Command()
    /**
     * Navigates back to the previous screen.
     */
    data object Back: Command()
    /**
     * Toggles the audio player on or off.
     */
    data object ToggleAudio: Command()
}
