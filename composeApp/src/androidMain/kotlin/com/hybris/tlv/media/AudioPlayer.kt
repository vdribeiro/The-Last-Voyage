package com.hybris.tlv.media

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.hybris.tlv.applicationContext
import com.hybris.tlv.logger.Logger

internal actual class AudioPlayer {

    private val player = ExoPlayer.Builder(applicationContext).build()
    private val playlist = mutableListOf<String>()

    actual fun play(vararg playlist: String) = runCatching {
        if (this.playlist.sorted() == playlist.toList().sorted()) return@runCatching
        this.playlist.apply {
            clear()
            addAll(elements = playlist)
        }
        stop()
        val mediaItems = playlist.map { MediaItem.fromUri("asset:///${it}".toUri()) }
        player.setMediaItems(mediaItems)
        player.shuffleModeEnabled = true
        resume()
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error playing media: ${it.message}")
    }

    actual fun resume() = runCatching {
        player.apply {
            if (mediaItemCount <= 0) return@runCatching
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error resuming media: ${it.message}")
    }

    actual fun pause() = runCatching { player.pause() }.getOrElse {
        Logger.error(tag = TAG, message = "Error pausing media: ${it.message}")
    }

    actual fun toggle() = runCatching {
        if (player.isPlaying) pause() else resume()
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error toggling media: ${it.message}")
    }

    actual fun stop() = runCatching { player.stop() }.getOrElse {
        Logger.error(tag = TAG, message = "Error stopping media: ${it.message}")
    }

    actual fun release() = runCatching { player.release() }.getOrElse {
        Logger.error(tag = TAG, message = "Error releasing media: ${it.message}")
    }

    companion object {
        private const val TAG = "AudioPlayer"
    }
}
