package com.hybris.tlv.media

import com.hybris.tlv.telemetry.Logger
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
        Logger.error(tag = TAG, message = "Error playing media\n${it.stackTraceToString()}")
    }

    private fun playNextTrack(index: Int? = null) {
        runCatching {
            stop()
            val nextIndex = index ?: ((currentIndex + 1) % playlist.size)
            val trackPath = playlist.getOrNull(index = nextIndex) ?: return
            val resourceUrl = Thread.currentThread().contextClassLoader.getResource(trackPath)

            when (resourceUrl) {
                null -> Logger.error(tag = TAG, message = "Could not find audio resource: $trackPath")
                else -> {
                    player = MediaPlayer(Media(resourceUrl.toString())).apply {
                        setOnEndOfMedia { playNextTrack() }
                        setOnError { Logger.error(tag = TAG, message = "MediaPlayer error: $error") }
                        play()
                    }
                    currentIndex = nextIndex
                }
            }
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error playing media\n${it.stackTraceToString()}")
        }
    }

    actual fun resume() = runCatching {
        player?.play() ?: Unit
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error resuming media\n${it.stackTraceToString()}")
    }

    actual fun pause() = runCatching {
        player?.pause() ?: Unit
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error pausing media\n${it.stackTraceToString()}")
    }

    actual fun toggle() = runCatching {
        if (player?.status == MediaPlayer.Status.PLAYING) pause() else resume()
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error toggling media\n${it.stackTraceToString()}")
    }

    actual fun stop() = runCatching {
        player?.stop()
        player?.dispose()
        player = null
        currentIndex = -1
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error stopping media\n${it.stackTraceToString()}")
    }

    companion object {
        private const val TAG = "AudioPlayer"
    }
}
