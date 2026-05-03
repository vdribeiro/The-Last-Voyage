package com.hybris.tlv.core.audio

import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.resource.AudioResource
import com.hybris.tlv.domain.flag.FeatureFlags.flags

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
         * Starts playback of a new [playlist].
         * If [loop] is true, then the playlist is repeated.
         * If [shuffle] is true, then the playlist order is shuffled.
         */
        data class Play(val playlist: List<AudioResource>, val loop: Boolean, val shuffle: Boolean): Action
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
     * Executes a specific [Action] on the audio player.
     * It handles the media lifecycle and includes a safety check for playlist redundancy:
     * If [Action.Play] is requested with a playlist identical to the active one, the request is ignored to prevent unnecessary restarts.
     *
     * @param action The [Action] to be performed (Play, Pause, Resume, or Toggle).
     */
    fun action(action: Action) {
        runCatching {
            if (!flags.music) {
                stop()
                return
            }

            when (action) {
                is Action.Play -> {
                    // Check if the given playlist is the same as the current playlist
                    val sortedPlaylist = action.playlist.sortedBy { it.path }
                    if (playlist.sortedBy { it.path } == sortedPlaylist) return@runCatching
                    // Play
                    playlist = action.playlist.let { if (action.shuffle) it.shuffled() else it }

                    stop()
                    play(loop = action.loop)
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
        }.onFailure {
            Telemetry.error(tag = TAG, message = "Error with media action $action", throwable = it)
        }
    }

    /**
     * Check the play status.
     *
     * @return `true` if the audio player is currently playing, `false` otherwise.
     */
    protected open fun isPlaying(): Boolean = false

    /**
     * Starts playing the current [playlist].
     *
     * @param loop if the playlist repeats when it ends.
     */
    protected open fun play(loop: Boolean) {}

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
