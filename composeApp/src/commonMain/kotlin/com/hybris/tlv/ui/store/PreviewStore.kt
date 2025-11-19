package com.hybris.tlv.ui.store

import com.hybris.tlv.media.AudioPlayer

private val previewAudioPlayer by lazy { AudioPlayer() }

internal fun <State, Action> getStore(initialState: State): Store<State, Action> = Store(
    audioPlayer = previewAudioPlayer,
    initialState = initialState
)
