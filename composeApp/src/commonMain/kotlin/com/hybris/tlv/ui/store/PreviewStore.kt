package com.hybris.tlv.ui.store

import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationState

private val previewNavigation by lazy { NavigationManager(initialState = NavigationState()) }
private val previewAudioPlayer by lazy { AudioPlayer() }

internal fun <State, Action> getStore(initialState: State): Store<State, Action> = Store(
    navigation = previewNavigation,
    audioPlayer = previewAudioPlayer,
    initialState = initialState
)
