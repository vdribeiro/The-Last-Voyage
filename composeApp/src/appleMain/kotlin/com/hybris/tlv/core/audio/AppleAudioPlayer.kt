package com.hybris.tlv.core.audio

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
import com.hybris.tlv.ui.lifecycle.observe

internal class AppleAudioPlayer: AudioPlayer() {

    private val player: AVPlayer = AVPlayer()
    private var currentIndex = -1
    private var endOfSongObserver: NSObjectProtocol? = null

    override fun isPlaying(): Boolean = player.rate != 0.0f

    override fun play(loop: Boolean) {
        val nextIndex = currentIndex + 1
        if (!loop && nextIndex >= playlist.size) {
            stop()
            return
        }
        currentIndex = nextIndex % playlist.size

        val trackPath = playlist.getOrNull(index = currentIndex)?.path ?: throw Throwable("Unable to get track at index $currentIndex")
        val resourceUrl = NSBundle.mainBundle.URLForResource(
            name = trackPath.substringBeforeLast(delimiter = '.'),
            withExtension = trackPath.substringAfterLast(delimiter = '.'),
        ) ?: throw Throwable("Unable to get resource $trackPath")
        val playerItem = AVPlayerItem(uRL = resourceUrl)

        endOfSongObserver?.let { NSNotificationCenter.defaultCenter.removeObserver(observer = it) }
        endOfSongObserver = NSNotificationCenter.defaultCenter.observe(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            key = playerItem,
        ) { this@AppleAudioPlayer.play(loop = loop) }
        player.replaceCurrentItemWithPlayerItem(item = playerItem)
        resume()
    }

    override fun resume() {
        if (enabled) player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.pause()
        player.replaceCurrentItemWithPlayerItem(item = null)
        endOfSongObserver?.let { NSNotificationCenter.defaultCenter.removeObserver(observer = it) }
        endOfSongObserver = null
        currentIndex = -1
    }
}

internal actual fun createAudioPlayer(): AudioPlayer = AppleAudioPlayer()
