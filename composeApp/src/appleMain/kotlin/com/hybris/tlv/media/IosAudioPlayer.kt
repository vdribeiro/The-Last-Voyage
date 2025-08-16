package com.hybris.tlv.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.hybris.tlv.logger.Logger
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.Foundation.NSBundle
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationWillResignActiveNotification
import platform.darwin.NSObjectProtocol

internal class IosAudioPlayer: AudioPlayer {

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

    override fun play(vararg playlist: String) = runCatching {
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

    override fun resume() {
        runCatching {
            player?.play()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error resuming media: ${it.message}")
        }
    }

    override fun pause() {
        runCatching {
            player?.pause()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error pausing media: ${it.message}")
        }
    }

    override fun stop() = runCatching {
        player?.pause()
        endOfSongObserver?.let { NSNotificationCenter.defaultCenter.removeObserver(observer = it) }
        endOfSongObserver = null
        player = null
        currentIndex = -1
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error stopping media: ${it.message}")
    }

    override fun release() {
        stop()
    }

    companion object {
        private const val TAG = "MediaPlayer"
    }
}

@Composable
internal actual fun rememberAudioPlayer(): AudioPlayer {
    val player = remember { IosAudioPlayer() }

    DisposableEffect(player) {
        val pauseObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationWillResignActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            player.pause()
        }

        val resumeObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            player.resume()
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(pauseObserver)
            NSNotificationCenter.defaultCenter.removeObserver(resumeObserver)
            player.release()
        }
    }

    return player
}
