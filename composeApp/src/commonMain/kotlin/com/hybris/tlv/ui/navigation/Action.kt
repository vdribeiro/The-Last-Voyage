package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface Action {
    @Serializable
    data object Back: Action
    @Serializable
    data object ToggleAudio: Action
}
