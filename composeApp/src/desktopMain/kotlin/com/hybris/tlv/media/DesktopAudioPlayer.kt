package com.hybris.tlv.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.hybris.tlv.LocalWindowState
import com.hybris.tlv.logger.Logger
import java.util.concurrent.atomic.AtomicBoolean
import javafx.embed.swing.JFXPanel
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

private val isJfxInitialized = AtomicBoolean(false)

internal class DesktopAudioPlayer: AudioPlayer {

    private var currentPlayer: MediaPlayer? = null
    private var currentPlaylist = mutableListOf<String>()
    private var shuffledPlaylist = listOf<String>()
    private var currentIndex = -1

    init {
        if (!isJfxInitialized.getAndSet(true)) JFXPanel()
    }

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

    override fun play(vararg playlist: String) = runCatching {
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

    override fun resume() {
        runCatching {
            currentPlayer?.play()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error resuming media: ${it.message}")
        }
    }

    override fun pause() {
        runCatching {
            currentPlayer?.pause()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error pausing media: ${it.message}")
        }
    }

    override fun stop() = runCatching {
        currentPlayer?.stop()
        currentPlayer?.dispose()
        currentPlayer = null
        currentIndex = -1
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error stopping media: ${it.message}")
    }

    override fun release() {
        stop()
    }

    companion object {
        private const val TAG = "AudioPlayer"
    }
}

@Composable
internal actual fun rememberAudioPlayer(): AudioPlayer {
    val windowState = LocalWindowState.current
    val player = remember { DesktopAudioPlayer() }

    LaunchedEffect(key1 = windowState?.isMinimized) {
        when {
            windowState?.isMinimized == true -> player.pause()
            else -> player.resume()
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            player.release()
        }
    }

    return player
}