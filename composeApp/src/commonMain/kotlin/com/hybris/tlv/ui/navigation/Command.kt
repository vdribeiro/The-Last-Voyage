package com.hybris.tlv.ui.navigation

import kotlinx.coroutines.channels.Channel

/**
 * A navigation command that can be sent.
 */
internal sealed class Command {
    /**
     * Navigates to a specific [Screen] indicating if it should be restored from the navigation stack or created anew.
     */
    data class Navigate(val screen: Screen, val restore: Boolean = false): Command()
    /**
     * Navigates back to the previous screen.
     */
    data object Back: Command()
    /**
     * Toggles the audio playback on or off.
     */
    data object ToggleAudio: Command()
}

/**
 * Channel for sending and receiving [Command] objects.
 * It is used for decoupled communication between different parts of the application and the main navigation logic.
 * It is buffered to prevent senders from being suspended if the receiver is not immediately available.
 */
internal val commandChannel: Channel<Command> = Channel(capacity = Channel.BUFFERED)
