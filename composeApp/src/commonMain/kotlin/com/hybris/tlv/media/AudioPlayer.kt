package com.hybris.tlv.media

/**
 * Audio player.
 */
internal expect class AudioPlayer() {

    /**
     * Play the given [playlist].
     */
    fun play(vararg playlist: String)

    /**
     * Resume playback.
     */
    fun resume()

    /**
     * Pauses playback.
     */
    fun pause()

    /**
     * Resume or pause playback depending on the current state.
     */
    fun toggle()

    /**
     * Stop playback without resetting the playlist.
     */
    fun stop()
}
