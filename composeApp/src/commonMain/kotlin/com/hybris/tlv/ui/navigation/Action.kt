package com.hybris.tlv.ui.navigation

internal sealed interface Action {
    data object Back: Action
    data object ToggleAudio: Action
}
