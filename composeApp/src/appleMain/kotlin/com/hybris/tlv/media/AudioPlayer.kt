package com.hybris.tlv.media

import com.hybris.tlv.logger.Logger
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.Foundation.NSBundle
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.darwin.NSObjectProtocol

internal actual class AudioPlayer {

    private var player: AVPlayer? = null
    private var playlist = mutableListOf<String>()
    private var shuffledPlaylist = listOf<String>()
    private var currentIndex = -1
    private var endOfSongObserver: NSObjectProtocol? = null

    private fun playTrackAtIndex(index: Int) {
        stop()

        val trackPath = shuffledPlaylist.getOrNull(index) ?: return

        val fileName = trackPath.substringAfterLast(delimiter = '/')
        val resourceName = fileName.substringBeforeLast(delimiter = '.')
        val resourceExtension = fileName.substringAfterLast(delimiter = '.')

        val resourceUrl = NSBundle.mainBundle.URLForResource(
            name = resourceName,
            withExtension = resourceExtension,
            subdirectory = "files"
        )

        if (resourceUrl != null) {
            val playerItem = AVPlayerItem(uRL = resourceUrl)

            endOfSongObserver = NSNotificationCenter.defaultCenter.addObserverForName(
                name = AVPlayerItemDidPlayToEndTimeNotification,
                `object` = playerItem,
                queue = NSOperationQueue.mainQueue
            ) { _ ->
                playNextTrack()
            }

            player = AVPlayer(playerItem = playerItem)
            player?.play()
            currentIndex = index
        } else {
            Logger.error(tag = TAG, message = "Could not find audio resource: $trackPath")
        }
    }

    private fun playNextTrack() {
        if (shuffledPlaylist.isEmpty()) return
        val nextIndex = (currentIndex + 1) % shuffledPlaylist.size
        playTrackAtIndex(nextIndex)
    }

    actual fun play(vararg playlist: String) = runCatching {
        if (this.playlist.sorted() == playlist.toList().sorted()) {
            resume()
            return@runCatching
        }

        this.playlist.clear()
        this.playlist.addAll(elements = playlist)

        this.shuffledPlaylist = this.playlist.shuffled()
        playTrackAtIndex(index = 0)
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error playing media: ${it.message}")
    }

    actual fun resume() {
        runCatching {
            player?.play()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error resuming media: ${it.message}")
        }
    }

    actual fun pause() {
        runCatching {
            player?.pause()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error pausing media: ${it.message}")
        }
    }

    actual fun toggle() {
        runCatching {
            if ((player?.rate ?: 0.0f) != 0.0f) pause() else resume()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error toggling media: ${it.message}")
        }
    }

    actual fun stop() = runCatching {
        player?.pause()
        endOfSongObserver?.let { NSNotificationCenter.defaultCenter.removeObserver(observer = it) }
        endOfSongObserver = null
        player = null
        currentIndex = -1
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error stopping media: ${it.message}")
    }

    actual fun release() {
        stop()
    }

    companion object {
        private const val TAG = "AudioPlayer"
    }
}
