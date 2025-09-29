package com.hybris.tlv.media

import com.hybris.tlv.lifecycle.observe
import com.hybris.tlv.telemetry.Logger
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.Foundation.NSBundle
import platform.Foundation.NSNotificationCenter
import platform.darwin.NSObjectProtocol

internal actual class AudioPlayer {

    private var player: AVPlayer? = null
    private var playlist = listOf<String>()
    private var currentIndex = -1
    private var endOfSongObserver: NSObjectProtocol? = null

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
            val fileName = trackPath.substringAfterLast(delimiter = '/')
            val resourceName = fileName.substringBeforeLast(delimiter = '.')
            val resourceExtension = fileName.substringAfterLast(delimiter = '.')
            val resourceUrl = NSBundle.mainBundle.URLForResource(
                name = resourceName,
                withExtension = resourceExtension,
                subdirectory = "files"
            )

            when (resourceUrl) {
                null -> Logger.error(tag = TAG, message = "Could not find audio resource: $trackPath")
                else -> {
                    val playerItem = AVPlayerItem(uRL = resourceUrl)
                    endOfSongObserver = NSNotificationCenter.defaultCenter.observe(
                        name = AVPlayerItemDidPlayToEndTimeNotification,
                        key = playerItem,
                    ) { playNextTrack() }
                    player = AVPlayer(playerItem = playerItem).apply {
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
        if ((player?.rate ?: 0.0f) != 0.0f) pause() else resume()
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error toggling media\n${it.stackTraceToString()}")
    }

    actual fun stop() = runCatching {
        player?.pause()
        endOfSongObserver?.let { NSNotificationCenter.defaultCenter.removeObserver(observer = it) }
        endOfSongObserver = null
        player = null
        currentIndex = -1
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error stopping media\n${it.stackTraceToString()}")
    }

    companion object {
        private const val TAG = "AudioPlayer"
    }
}
