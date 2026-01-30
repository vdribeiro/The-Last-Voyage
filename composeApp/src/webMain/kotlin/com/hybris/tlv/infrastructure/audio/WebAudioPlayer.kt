@file:OptIn(ExperimentalWasmJsInterop::class)

package com.hybris.tlv.infrastructure.audio

import kotlinx.browser.document
import org.w3c.dom.HTMLAudioElement


internal class WebAudioPlayer: AudioPlayer() {

    private val player: HTMLAudioElement by lazy {
        (document.createElement(localName = "audio") as HTMLAudioElement).apply { preload = "auto" }
    }
    private var currentIndex = -1
    private var paused: Boolean = false

    override fun isPlaying(): Boolean = !player.paused

    override fun play() {
        val nextIndex = (currentIndex + 1) % playlist.size
        val trackPath = playlist.getOrNull(index = nextIndex)?.path ?: throw Throwable("Unable to get track at index $nextIndex")
        player.apply {
            src = trackPath
            onended = { play() }
            if (!paused) play()
        }
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
    }
}

internal actual fun createAudioPlayer(): AudioPlayer = WebAudioPlayer()
