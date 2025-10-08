package com.hybris.tlv.media

import com.hybris.tlv.lifecycle.observe
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.Foundation.NSBundle
import platform.Foundation.NSNotificationCenter
import platform.darwin.NSObjectProtocol

internal class AppleAudioPlayer: AudioPlayer() {

    private var player: AVPlayer? = null
    private var currentIndex = -1
    private var endOfSongObserver: NSObjectProtocol? = null

    override fun isPlaying(): Boolean = (player?.rate ?: 0.0f) != 0.0f

    override fun play() {
        val nextIndex = (currentIndex + 1) % playlist.size
        val trackPath = playlist.getOrNull(index = nextIndex) ?: return
        val fileName = trackPath.substringAfterLast(delimiter = '/')
        val resourceName = fileName.substringBeforeLast(delimiter = '.')
        val resourceExtension = fileName.substringAfterLast(delimiter = '.')
        val resourceUrl = NSBundle.mainBundle.URLForResource(
            name = resourceName,
            withExtension = resourceExtension,
            subdirectory = "files"
        ) ?: return
        val playerItem = AVPlayerItem(uRL = resourceUrl)
        endOfSongObserver = NSNotificationCenter.defaultCenter.observe(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            key = playerItem,
        ) { play() }
        player = AVPlayer(playerItem = playerItem).apply { play() }
        currentIndex = nextIndex
    }

    override fun resume() {
        player?.play()
    }

    override fun pause() {
        player?.pause()
    }

    override fun stop() {
        player?.pause()
        endOfSongObserver?.let { NSNotificationCenter.defaultCenter.removeObserver(observer = it) }
        endOfSongObserver = null
        player = null
        currentIndex = -1
    }
}
