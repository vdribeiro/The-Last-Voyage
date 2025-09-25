package com.hybris.tlv.media

import androidx.compose.runtime.Composable

/**
 * Audio player.
 */
internal interface AudioPlayer {

    /**
     * Play the given [playlist].
     */
    fun play(vararg playlist: String) {}

    /**
     * Resume playback.
     */
    fun resume() {}

    /**
     * Pauses playback.
     */
    fun pause() {}

    /**
     * Resume or pause playback depending on the current state.
     */
    fun toggle() {}

    /**
     * Stop playback without resetting the playlist.
     */
    fun stop() {}

    /**
     * Destroy the player. The player cannot be used after calling this method.
     */
    fun release() {}
}

@Composable
internal expect fun rememberAudioPlayer(): AudioPlayer

internal object NoAudioPlayer: AudioPlayer
