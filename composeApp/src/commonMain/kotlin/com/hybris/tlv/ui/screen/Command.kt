package com.hybris.tlv.ui.screen

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.receiveAsFlow
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Screen

/**
 * A navigation command that can be sent.
 */
internal sealed class Command {
    /**
     * Navigates to a specific [com.hybris.tlv.ui.navigation.Screen] indicating if it should be restored from the navigation stack or created anew.
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
 */
private val commandChannel: Channel<Command> = Channel(
    capacity = Channel.BUFFERED,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

internal fun sendCommand(command: Command): Boolean =
    commandChannel.trySend(element = command)
        .onFailure { Telemetry.error(tag = TAG, message = "Unable to send command $command", throwable = it) }.isSuccess

internal suspend fun receiveCommand(block: (Command) -> Unit) {
    commandChannel.receiveAsFlow().collect { block(it) }
}

private const val TAG = "Command"
