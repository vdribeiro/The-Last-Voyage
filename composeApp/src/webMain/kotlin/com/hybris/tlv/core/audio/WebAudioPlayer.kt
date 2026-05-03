@file:OptIn(ExperimentalWasmJsInterop::class)

package com.hybris.tlv.core.audio

import kotlinx.browser.document
import org.w3c.dom.HTMLAudioElement

internal class WebAudioPlayer: AudioPlayer() {

    private val player: HTMLAudioElement = (document.createElement(localName = "audio") as HTMLAudioElement).apply { preload = "auto" }
    private var currentIndex = -1

    override fun isPlaying(): Boolean = !player.paused && !player.ended

    override fun play(loop: Boolean) {
        val nextIndex = currentIndex + 1
        if (!loop && nextIndex >= playlist.size) {
            stop()
            return
        }
        currentIndex = nextIndex % playlist.size

        val trackPath = playlist.getOrNull(index = currentIndex)?.path ?: throw Throwable("Unable to get track at index $currentIndex")

        player.apply {
            src = trackPath
            onended = { this@WebAudioPlayer.play(loop = loop) }
            if (enabled) play()
        }
    }

    override fun resume() {
        if (enabled) player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.pause()
        player.currentTime = 0.0
        player.removeAttribute(qualifiedName = "src")
        player.load()
        currentIndex = -1
    }
}

internal actual fun createAudioPlayer(): AudioPlayer = WebAudioPlayer()
