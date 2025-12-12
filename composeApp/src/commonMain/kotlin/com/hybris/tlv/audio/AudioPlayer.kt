package com.hybris.tlv.audio

import com.hybris.tlv.telemetry.Telemetry

/**
 * Audio player.
 */
internal open class AudioPlayer {

    protected var playlist = listOf<String>()

    sealed interface Action {
        data class Play(val playlist: List<String>?): Action
        data object Pause: Action
        data object Resume: Action
        data object Toggle: Action
    }

    fun action(action: Action) {
        runCatching {
            when (action) {
                is Action.Play -> {
                    // Check if the playlist is not the same as the current playlist
                    if (action.playlist == null) return@runCatching
                    val sortedPlaylist = action.playlist.sorted()
                    if (playlist.sorted() == sortedPlaylist) return@runCatching
                    // Play
                    playlist = sortedPlaylist.shuffled()
                    stop()
                    play()
                }

                Action.Pause -> pause()
                Action.Resume -> resume()
                Action.Toggle -> if (!isPlaying()) resume() else pause()
            }
        }.onFailure { Telemetry.error(tag = TAG, message = "Error with media action $action", throwable = it) }
    }

    protected open fun isPlaying(): Boolean = false

    /**
     * Play the playlist.
     */
    protected open fun play() {}

    /**
     * Resume playback.
     */
    protected open fun resume() {}

    /**
     * Pauses playback.
     */
    protected open fun pause() {}

    /**
     * Stop playback without resetting the playlist.
     */
    protected open fun stop() {}

    companion object {
        private const val TAG = "AudioPlayer"
    }
}
