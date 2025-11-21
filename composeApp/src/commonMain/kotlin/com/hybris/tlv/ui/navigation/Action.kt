package com.hybris.tlv.ui.navigation

import kotlinx.coroutines.channels.Channel

internal sealed interface Action {
    data object Back: Action
    data object ToggleAudio: Action
}

internal val actionChannel: Channel<Action> = Channel()
