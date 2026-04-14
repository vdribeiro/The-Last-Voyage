package com.hybris.tlv.data.resource

/**
 * A type-safe index of audio assets.
 * This sealed class acts as a central registry for background music and ambient tracks.
 * By using [AudioResource], the application avoids hardcoding string paths, reducing the risk of errors.
 *
 * @property path The relative file path to the audio asset in `commonMain/resources/tracks`.
 */
internal sealed class AudioResource(val path: String) {
    data object VilleSeppanen: AudioResource(path = "tracks/ville_seppanen-1_g.mp3")
    data object BlindShift: AudioResource(path = "tracks/blind_shift.mp3")
    data object Graduality: AudioResource(path = "tracks/graduality.mp3")
    data object LedTwilight: AudioResource(path = "tracks/led_twilight.mp3")
    data object NeonSky: AudioResource(path = "tracks/neon_sky.mp3")
    data object RainInSpace: AudioResource(path = "tracks/rain_in_space.mp3")
    data object SpaceGras: AudioResource(path = "tracks/space_gras.mp3")
    data object Space: AudioResource(path = "tracks/space.mp3")
}
