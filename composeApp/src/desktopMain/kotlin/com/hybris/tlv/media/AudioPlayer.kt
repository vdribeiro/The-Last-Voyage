package com.hybris.tlv.media

import com.hybris.tlv.telemetry.Telemetry
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

internal actual class AudioPlayer {

    private var player: MediaPlayer? = null
    private var playlist = listOf<String>()
    private var currentIndex = -1

    actual fun play(vararg playlist: String) = runCatching {
        val sortedPlaylist = playlist.toList().sorted()
        if (this.playlist.sorted() == sortedPlaylist) return@runCatching
        this.playlist = sortedPlaylist.shuffled()
        playNextTrack(index = 0)
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Error playing media", throwable = it)
    }

    private fun playNextTrack(index: Int? = null) {
        runCatching {
            stop()
            val nextIndex = index ?: ((currentIndex + 1) % playlist.size)
            val trackPath = playlist.getOrNull(index = nextIndex) ?: return
            val resourceUrl = Thread.currentThread().contextClassLoader.getResource(trackPath)

            when (resourceUrl) {
                null -> Telemetry.error(tag = TAG, message = "Could not find audio resource: $trackPath")
                else -> {
                    player = MediaPlayer(Media(resourceUrl.toString())).apply {
                        setOnEndOfMedia { playNextTrack() }
                        setOnError { Telemetry.error(tag = TAG, message = "MediaPlayer error", throwable = error) }
                        play()
                    }
                    currentIndex = nextIndex
                }
            }
        }.getOrElse {
            Telemetry.error(tag = TAG, message = "Error playing media", throwable = it)
        }
    }

    actual fun resume() = runCatching {
        player?.play() ?: Unit
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Error resuming media", throwable = it)
    }

    actual fun pause() = runCatching {
        player?.pause() ?: Unit
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Error pausing media", throwable = it)
    }

    actual fun toggle() = runCatching {
        if (player?.status == MediaPlayer.Status.PLAYING) pause() else resume()
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Error toggling media", throwable = it)
    }

    actual fun stop() = runCatching {
        player?.stop()
        player?.dispose()
        player = null
        currentIndex = -1
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Error stopping media", throwable = it)
    }

    companion object {
        private const val TAG = "AudioPlayer"
    }
}
