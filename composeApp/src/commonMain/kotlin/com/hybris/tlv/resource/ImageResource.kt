package com.hybris.tlv.resource

import org.jetbrains.compose.resources.DrawableResource
import thelastvoyage.composeapp.generated.resources.A
import thelastvoyage.composeapp.generated.resources.B
import thelastvoyage.composeapp.generated.resources.C
import thelastvoyage.composeapp.generated.resources.D
import thelastvoyage.composeapp.generated.resources.F
import thelastvoyage.composeapp.generated.resources.G
import thelastvoyage.composeapp.generated.resources.K
import thelastvoyage.composeapp.generated.resources.L
import thelastvoyage.composeapp.generated.resources.M
import thelastvoyage.composeapp.generated.resources.O
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.S
import thelastvoyage.composeapp.generated.resources.T
import thelastvoyage.composeapp.generated.resources.W
import thelastvoyage.composeapp.generated.resources.Y
import thelastvoyage.composeapp.generated.resources.alkali_metal_clouds_gas_giant
import thelastvoyage.composeapp.generated.resources.ammonia_clouds_gas_giant
import thelastvoyage.composeapp.generated.resources.barren_planet
import thelastvoyage.composeapp.generated.resources.chthonian_planet
import thelastvoyage.composeapp.generated.resources.cloudless_gas_giant
import thelastvoyage.composeapp.generated.resources.cold_eyeball_planet
import thelastvoyage.composeapp.generated.resources.crater_planet
import thelastvoyage.composeapp.generated.resources.desert_planet
import thelastvoyage.composeapp.generated.resources.disrupted_planet
import thelastvoyage.composeapp.generated.resources.earth_analog_planet
import thelastvoyage.composeapp.generated.resources.earth_like_planet
import thelastvoyage.composeapp.generated.resources.ellipsoid_planet
import thelastvoyage.composeapp.generated.resources.eyeball_planet
import thelastvoyage.composeapp.generated.resources.gas_giant
import thelastvoyage.composeapp.generated.resources.hot_eyebal_planet
import thelastvoyage.composeapp.generated.resources.hot_jupiter
import thelastvoyage.composeapp.generated.resources.hot_neptune
import thelastvoyage.composeapp.generated.resources.ic_launcher_background
import thelastvoyage.composeapp.generated.resources.ic_launcher_foreground
import thelastvoyage.composeapp.generated.resources.ice_giant
import thelastvoyage.composeapp.generated.resources.ice_planet
import thelastvoyage.composeapp.generated.resources.iron_planet
import thelastvoyage.composeapp.generated.resources.lava_planet
import thelastvoyage.composeapp.generated.resources.mega_earth
import thelastvoyage.composeapp.generated.resources.mini_neptune
import thelastvoyage.composeapp.generated.resources.ocean_planet
import thelastvoyage.composeapp.generated.resources.protoplanet
import thelastvoyage.composeapp.generated.resources.puffy_planet
import thelastvoyage.composeapp.generated.resources.silicate_clouds_gas_giant
import thelastvoyage.composeapp.generated.resources.sub_earth
import thelastvoyage.composeapp.generated.resources.subsurface_ocean_planet
import thelastvoyage.composeapp.generated.resources.super_earth
import thelastvoyage.composeapp.generated.resources.super_habitable_planet
import thelastvoyage.composeapp.generated.resources.super_jupiter
import thelastvoyage.composeapp.generated.resources.super_neptune
import thelastvoyage.composeapp.generated.resources.super_puff_planet
import thelastvoyage.composeapp.generated.resources.terrestrial_planet
import thelastvoyage.composeapp.generated.resources.ultra_hot_jupiter
import thelastvoyage.composeapp.generated.resources.ultra_hot_neptune
import thelastvoyage.composeapp.generated.resources.ultra_short_period_planet
import thelastvoyage.composeapp.generated.resources.unknown
import thelastvoyage.composeapp.generated.resources.water_clouds_gas_giant

/**
 * Resource class for images.
 */
internal sealed class ImageResource(
    val path: String,
    val drawable: DrawableResource
) {
    // Icons
    data object LauncherBackground: ImageResource(path = "drawable/ic_launcher_background.png", drawable = Res.drawable.ic_launcher_background)
    data object LauncherForeground: ImageResource(path = "drawable/ic_launcher_foreground.png", drawable = Res.drawable.ic_launcher_foreground)

    // Stellar Hosts
    data object O: ImageResource(path = "drawable/O.jpg", drawable = Res.drawable.O)
    data object B: ImageResource(path = "drawable/B.jpg", drawable = Res.drawable.B)
    data object A: ImageResource(path = "drawable/A.jpg", drawable = Res.drawable.A)
    data object F: ImageResource(path = "drawable/F.jpg", drawable = Res.drawable.F)
    data object G: ImageResource(path = "drawable/G.jpg", drawable = Res.drawable.G)
    data object K: ImageResource(path = "drawable/K.jpg", drawable = Res.drawable.K)
    data object M: ImageResource(path = "drawable/M.jpg", drawable = Res.drawable.M)
    data object W: ImageResource(path = "drawable/W.jpg", drawable = Res.drawable.W)
    data object L: ImageResource(path = "drawable/L.jpg", drawable = Res.drawable.L)
    data object T: ImageResource(path = "drawable/T.jpg", drawable = Res.drawable.T)
    data object Y: ImageResource(path = "drawable/Y.jpg", drawable = Res.drawable.Y)
    data object C: ImageResource(path = "drawable/C.jpg", drawable = Res.drawable.C)
    data object S: ImageResource(path = "drawable/S.jpg", drawable = Res.drawable.S)
    data object D: ImageResource(path = "drawable/D.jpg", drawable = Res.drawable.D)
    data object Unknown: ImageResource(path = "drawable/unknown.jpg", drawable = Res.drawable.unknown)

    // Planets
    data object SubEarth: ImageResource(path = "drawable/sub_earth.jpg", drawable = Res.drawable.sub_earth)
    data object SuperEarth: ImageResource(path = "drawable/super_earth.jpg", drawable = Res.drawable.super_earth)
    data object MegaEarth: ImageResource(path = "drawable/mega_earth.jpg", drawable = Res.drawable.mega_earth)
    data object MiniNeptune: ImageResource(path = "drawable/mini_neptune.jpg", drawable = Res.drawable.mini_neptune)
    data object SuperNeptune: ImageResource(path = "drawable/super_neptune.jpg", drawable = Res.drawable.super_neptune)
    data object IceGiant: ImageResource(path = "drawable/ice_giant.jpg", drawable = Res.drawable.ice_giant)
    data object GasGiant: ImageResource(path = "drawable/gas_giant.jpg", drawable = Res.drawable.gas_giant)
    data object SuperJupiter: ImageResource(path = "drawable/super_jupiter.jpg", drawable = Res.drawable.super_jupiter)
    data object TerrestrialPlanet: ImageResource(path = "drawable/terrestrial_planet.jpg", drawable = Res.drawable.terrestrial_planet)
    data object IronPlanet: ImageResource(path = "drawable/iron_planet.jpg", drawable = Res.drawable.iron_planet)
    data object PuffyPlanet: ImageResource(path = "drawable/puffy_planet.jpg", drawable = Res.drawable.puffy_planet)
    data object SuperPuffPlanet: ImageResource(path = "drawable/super_puff_planet.jpg", drawable = Res.drawable.super_puff_planet)
    data object OceanPlanet: ImageResource(path = "drawable/ocean_planet.jpg", drawable = Res.drawable.ocean_planet)
    data object SubsurfaceOceanPlanet: ImageResource(path = "drawable/subsurface_ocean_planet.jpg", drawable = Res.drawable.subsurface_ocean_planet)
    data object LavaPlanet: ImageResource(path = "drawable/lava_planet.jpg", drawable = Res.drawable.lava_planet)
    data object DesertPlanet: ImageResource(path = "drawable/desert_planet.jpg", drawable = Res.drawable.desert_planet)
    data object IcePlanet: ImageResource(path = "drawable/ice_planet.jpg", drawable = Res.drawable.ice_planet)
    data object HotJupiter: ImageResource(path = "drawable/hot_jupiter.jpg", drawable = Res.drawable.hot_jupiter)
    data object UltraHotJupiter: ImageResource(path = "drawable/ultra_hot_jupiter.jpg", drawable = Res.drawable.ultra_hot_jupiter)
    data object HotNeptune: ImageResource(path = "drawable/hot_neptune.jpg", drawable = Res.drawable.hot_neptune)
    data object UltraHotNeptune: ImageResource(path = "drawable/ultra_hot_neptune.jpg", drawable = Res.drawable.ultra_hot_neptune)
    data object UltraShortPeriodPlanet: ImageResource(path = "drawable/ultra_short_period_planet.jpg", drawable = Res.drawable.ultra_short_period_planet)
    data object EyeballPlanet: ImageResource(path = "drawable/eyeball_planet.jpg", drawable = Res.drawable.eyeball_planet)
    data object HotEyeballPlanet: ImageResource(path = "drawable/hot_eyebal_planet.jpg", drawable = Res.drawable.hot_eyebal_planet)
    data object ColdEyeballPlanet: ImageResource(path = "drawable/cold_eyeball_planet.jpg", drawable = Res.drawable.cold_eyeball_planet)
    data object AmmoniaCloudsGasGiant: ImageResource(path = "drawable/ammonia_clouds_gas_giant.jpg", drawable = Res.drawable.ammonia_clouds_gas_giant)
    data object WaterCloudsGasGiant: ImageResource(path = "drawable/water_clouds_gas_giant.jpg", drawable = Res.drawable.water_clouds_gas_giant)
    data object CloudlessGasGiant: ImageResource(path = "drawable/cloudless_gas_giant.jpg", drawable = Res.drawable.cloudless_gas_giant)
    data object AlkaliMetalCloudsGasGiant: ImageResource(path = "drawable/alkali_metal_clouds_gas_giant.jpg", drawable = Res.drawable.alkali_metal_clouds_gas_giant)
    data object SilicateCloudsGasGiant: ImageResource(path = "drawable/silicate_clouds_gas_giant.jpg", drawable = Res.drawable.silicate_clouds_gas_giant)
    data object BarrenPlanet: ImageResource(path = "drawable/barren_planet.jpg", drawable = Res.drawable.barren_planet)
    data object EarthLikePlanet: ImageResource(path = "drawable/earth_like_planet.jpg", drawable = Res.drawable.earth_like_planet)
    data object EarthAnalogPlanet: ImageResource(path = "drawable/earth_analog_planet.jpg", drawable = Res.drawable.earth_analog_planet)
    data object SuperHabitablePlanet: ImageResource(path = "drawable/super_habitable_planet.jpg", drawable = Res.drawable.super_habitable_planet)
    data object Protoplanet: ImageResource(path = "drawable/protoplanet.jpg", drawable = Res.drawable.protoplanet)
    data object DisruptedPlanet: ImageResource(path = "drawable/disrupted_planet.jpg", drawable = Res.drawable.disrupted_planet)
    data object ChthonianPlanet: ImageResource(path = "drawable/chthonian_planet.jpg", drawable = Res.drawable.chthonian_planet)
    data object CraterPlanet: ImageResource(path = "drawable/crater_planet.jpg", drawable = Res.drawable.crater_planet)
    data object EllipsoidPlanet: ImageResource(path = "drawable/ellipsoid_planet.jpg", drawable = Res.drawable.ellipsoid_planet)
}
