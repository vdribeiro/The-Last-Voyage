package com.hybris.tlv.audio

import com.hybris.tlv.flag.FeatureFlags.flags
import com.hybris.tlv.resource.AudioResource
import com.hybris.tlv.core.telemetry.Telemetry

/**
 * Audio player that provides basic audio playback functionality, including playing, pausing, resuming, and stopping audio.
 * Mainly designed to be extended by platform-specific implementations.
 */
internal open class AudioPlayer {

    /**
     * Whether the audio player is enabled or disabled.
     */
    protected var enabled: Boolean = true
    /**
     * The current playlist.
     */
    protected var playlist = listOf<AudioResource>()

    /**
     * Represents an action to be performed by the audio player.
     */
    sealed interface Action {
        /**
         * Starts playback of a new playlist.
         */
        data class Play(val playlist: List<AudioResource>): Action
        /**
         * Pauses the current playback.
         */
        data object Pause: Action
        /**
         * Resumes the current playback.
         */
        data object Resume: Action
        /**
         * Toggles the audio player on or off.
         */
        data object Toggle: Action
    }

    /**
     * Performs the given audio player action.
     * If the playlist is the same as the current playlist, then play does nothing.
     */
    fun action(action: Action) {
        runCatching {
            if (!flags.value.music) {
                stop()
                return
            }

            when (action) {
                is Action.Play -> {
                    // Check if the playlist is not the same as the current playlist
                    val sortedPlaylist = action.playlist.sortedBy { it.path }
                    if (playlist.sortedBy { it.path } == sortedPlaylist) return@runCatching
                    // Play
                    playlist = sortedPlaylist.shuffled()
                    stop()
                    play()
                    // After setting up the playlist, check if the audio player is enabled
                    if (!enabled) pause()
                }

                Action.Pause -> pause()
                Action.Resume -> if (enabled) resume()
                Action.Toggle -> {
                    enabled = !enabled
                    if (!isPlaying()) resume() else pause()
                }
            }
        }.onFailure { Telemetry.error(tag = TAG, message = "Error with media action $action", throwable = it) }
    }

    /**
     * Returns whether the audio player is currently playing.
     */
    protected open fun isPlaying(): Boolean = false

    /**
     * Starts playing the current playlist.
     */
    protected open fun play() {}

    /**
     * Resumes playback.
     */
    protected open fun resume() {}

    /**
     * Pauses playback.
     */
    protected open fun pause() {}

    /**
     * Stops playback.
     */
    protected open fun stop() {}

    companion object {
        private const val TAG = "AudioPlayer"
    }
}

/**
 * Creates a new instance of [AudioPlayer].
 */
internal expect fun createAudioPlayer(): AudioPlayer
