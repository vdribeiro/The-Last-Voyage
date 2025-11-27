package com.hybris.tlv.ui.navigation

import kotlinx.coroutines.channels.Channel

internal sealed class Command {
    data class Navigate(val screen: Screen, val restore: Boolean = false): Command()
    data object Back: Command()
    data object ToggleAudio: Command()
}

internal val commandChannel: Channel<Command> = Channel()
