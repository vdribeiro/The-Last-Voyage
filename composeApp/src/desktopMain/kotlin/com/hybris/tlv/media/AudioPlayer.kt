package com.hybris.tlv.media

import com.hybris.tlv.logger.Logger
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

internal actual class AudioPlayer {

    private var currentPlayer: MediaPlayer? = null
    private var currentPlaylist = mutableListOf<String>()
    private var shuffledPlaylist = listOf<String>()
    private var currentIndex = -1

    private fun playTrackAtIndex(index: Int) {
        stop()

        val trackPath = shuffledPlaylist.getOrNull(index = index) ?: return
        val resourceUrl = Thread.currentThread().contextClassLoader.getResource(trackPath)

        when (resourceUrl) {
            null -> Logger.error(tag = TAG, message = "Could not find audio resource: $trackPath")
            else -> {
                val media = Media(resourceUrl.toString())
                currentPlayer = MediaPlayer(media).apply {
                    setOnEndOfMedia { playNextTrack() }
                    setOnError { Logger.error(tag = TAG, message = "MediaPlayer error: $error") }
                    play()
                }
                currentIndex = index
            }
        }
    }

    private fun playNextTrack() {
        if (shuffledPlaylist.isEmpty()) return
        val nextIndex = (currentIndex + 1) % shuffledPlaylist.size
        playTrackAtIndex(nextIndex)
    }

    actual fun play(vararg playlist: String) {
        runCatching {
            if (currentPlaylist.sorted() == playlist.toList().sorted()) {
                resume()
                return@runCatching
            }

            currentPlaylist.clear()
            currentPlaylist.addAll(elements = playlist)

            shuffledPlaylist = currentPlaylist.shuffled()
            playTrackAtIndex(index = 0)
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error playing media: ${it.message}")
        }
    }

    actual fun resume() {
        runCatching {
            currentPlayer?.play()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error resuming media: ${it.message}")
        }
    }

    actual fun pause() {
        runCatching {
            currentPlayer?.pause()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error pausing media: ${it.message}")
        }
    }

    actual fun toggle() {
        runCatching {
            if (currentPlayer?.status == MediaPlayer.Status.PLAYING) pause() else resume()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error toggling media: ${it.message}")
        }
    }

    actual fun stop() {
        runCatching {
            currentPlayer?.stop()
            currentPlayer?.dispose()
            currentPlayer = null
            currentIndex = -1
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error stopping media: ${it.message}")
        }
    }

    actual fun release() {
        stop()
    }

    companion object {
        private const val TAG = "AudioPlayer"
    }
}
