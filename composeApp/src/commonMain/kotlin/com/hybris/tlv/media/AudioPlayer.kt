package com.hybris.tlv.media

import com.hybris.tlv.telemetry.Telemetry

/**
 * Audio player.
 */
internal abstract class AudioPlayer {
    protected var playlist = listOf<String>()
    protected var currentIndex = -1

    /**
     * Play the given [playlist].
     */
    fun play(vararg playlist: String) = runCatching {
        val sortedPlaylist = playlist.toList().sorted()
        if (this.playlist.sorted() == sortedPlaylist) return@runCatching
        this.playlist = sortedPlaylist.shuffled()
        stop()
        playNextTrack()
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Error playing media", throwable = it)
    }

    /**
     * Resume playback.
     */
    fun resume() = runCatching {
        resumePlayer()
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Error resuming media", throwable = it)
    }

    /**
     * Pauses playback.
     */
    fun pause() = runCatching {
        pausePlayer()
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Error pausing media", throwable = it)
    }

    /**
     * Resume or pause playback depending on the current state.
     */
    fun toggle() = runCatching {
        if (isPlaying()) pause() else resume()
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Error toggling media", throwable = it)
    }

    /**
     * Stop playback without resetting the playlist.
     */
    fun stop() = runCatching {
        stopPlayer()
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Error stopping media", throwable = it)
    }

    protected abstract fun playNextTrack()
    protected abstract fun isPlaying(): Boolean
    protected abstract fun resumePlayer()
    protected abstract fun pausePlayer()
    protected abstract fun stopPlayer()

    companion object {
        private const val TAG = "AudioPlayer"
    }
}
