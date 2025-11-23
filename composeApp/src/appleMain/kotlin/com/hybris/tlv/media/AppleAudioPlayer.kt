package com.hybris.tlv.media

import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.Foundation.NSBundle
import platform.Foundation.NSNotificationCenter
import platform.darwin.NSObjectProtocol
import com.hybris.tlv.lifecycle.observe

internal class AppleAudioPlayer: AudioPlayer() {

    private val player: AVPlayer by lazy {
        AVPlayer()
    }
    private var currentIndex = -1
    private var endOfSongObserver: NSObjectProtocol? = null
    private var paused: Boolean = false

    override fun isPlaying(): Boolean = player.rate != 0.0f

    override fun play() {
        val nextIndex = (currentIndex + 1) % playlist.size
        val trackPath = playlist.getOrNull(index = nextIndex) ?: throw Throwable("Unable to get track at index $nextIndex")
        val resourceName = trackPath.substringBeforeLast(delimiter = '.')
        val resourceExtension = trackPath.substringAfterLast(delimiter = '.')
        val resourceUrl = NSBundle.mainBundle.URLForResource(
            name = resourceName,
            withExtension = resourceExtension,
        ) ?: throw Throwable("Unable to get resource $resourceName.$resourceExtension")
        val playerItem = AVPlayerItem(uRL = resourceUrl)
        endOfSongObserver = NSNotificationCenter.defaultCenter.observe(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            key = playerItem,
        ) { play() }
        player.replaceCurrentItemWithPlayerItem(item = playerItem)
        if (!paused) player.play()
        currentIndex = nextIndex
    }

    override fun resume() {
        player.play()
        paused = false
    }

    override fun pause() {
        player.pause()
        paused = true
    }

    override fun stop() {
        player.pause()
        player.replaceCurrentItemWithPlayerItem(item = null)
        endOfSongObserver?.let { NSNotificationCenter.defaultCenter.removeObserver(observer = it) }
        endOfSongObserver = null
        currentIndex = -1
    }
}
