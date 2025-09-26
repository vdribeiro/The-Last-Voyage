package com.hybris.tlv.media

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.hybris.tlv.applicationContext
import com.hybris.tlv.logger.Logger

internal actual class AudioPlayer {

    private var player: ExoPlayer? = null
    private var playlist = listOf<String>()

    actual fun play(vararg playlist: String) = runCatching {
        val sortedPlaylist = playlist.toList().sorted()
        if (this.playlist.sorted() == sortedPlaylist) return@runCatching
        this.playlist = sortedPlaylist.shuffled()
        playNextTrack()
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error playing media: ${it.message}")
    }

    private fun playNextTrack() {
        runCatching {
            stop()

            player = ExoPlayer.Builder(applicationContext).build()
            val mediaItems = this.playlist.map { MediaItem.fromUri("asset:///${it}".toUri()) }
            player?.apply {
                setMediaItems(mediaItems)
                shuffleModeEnabled = true
            }
            resume()
        }.getOrElse {
            Logger.error(tag = TAG, message = "Error playing media: ${it.message}")
        }
    }

    actual fun resume() = runCatching {
        player?.apply {
            if (mediaItemCount <= 0) return@runCatching
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error resuming media: ${it.message}")
    }

    actual fun pause() = runCatching {
        player?.pause() ?: Unit
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error pausing media: ${it.message}")
    }

    actual fun toggle() = runCatching {
        if (player?.isPlaying == true) pause() else resume()
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error toggling media: ${it.message}")
    }

    actual fun stop() = runCatching {
        player?.stop() ?: Unit
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error stopping media: ${it.message}")
    }

    companion object {
        private const val TAG = "AudioPlayer"
    }
}
