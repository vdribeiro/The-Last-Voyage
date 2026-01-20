package com.hybris.tlv.domain.command

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.receiveAsFlow
import com.hybris.tlv.core.telemetry.Telemetry

/**
 * Channel for sending and receiving [Command] objects.
 * It is used for decoupled communication between different parts of the application.
 */
private val commandChannel: Channel<Command> = Channel(
    capacity = Channel.BUFFERED,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

/**
 * Send a [Command] to the [commandChannel].
 */
internal fun sendCommand(command: Command): Boolean =
    commandChannel.trySend(element = command)
        .onFailure { Telemetry.error(tag = TAG, message = "Unable to send command $command", throwable = it) }.isSuccess

/**
 * Listen to [Command]s from the [commandChannel].
 */
internal suspend fun receiveCommand(block: (Command) -> Unit) =
    commandChannel.receiveAsFlow().collect { block(it) }

private const val TAG = "Command"
