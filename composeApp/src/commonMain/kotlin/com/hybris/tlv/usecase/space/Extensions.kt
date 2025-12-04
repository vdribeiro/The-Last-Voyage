package com.hybris.tlv.usecase.space

import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.usecase.space.formula.Constants.PARSEC
import com.hybris.tlv.usecase.space.formula.Constants.SUN_SURFACE_GRAVITY
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.model.StellarHost
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

internal fun Double.roundTo(decimalPlaces: Int): Double {
    val factor = 10.0.pow(n = decimalPlaces)
    return round(x = this * factor) / factor
}

internal fun Double?.sanitize(): Double? = when {
    this == null -> null
    isNaN() || isInfinite() || this == Double.NEGATIVE_INFINITY || this == Double.POSITIVE_INFINITY -> null
    else -> this
}

internal fun Double.stellarHostGravityToSunGravity(): Double = 10.0.pow(x = this - SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)

internal fun Double.sunGravityToStellarHostGravity(): Double = (log10(x = this) + SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)

internal fun Double.parsecsToLightYears(): Double = this * PARSEC

internal fun Double.lightYearsToParsecs(): Double = this / PARSEC

internal fun String?.spectralTypeToImage(): ImageResource =
    when (this?.firstOrNull()?.uppercase()) {
        "O" -> ImageResource(path = "O.jpg", drawable = Res.drawable.O)
        "B" -> ImageResource(path = "B.jpg", drawable = Res.drawable.B)
        "A" -> ImageResource(path = "A.jpg", drawable = Res.drawable.A)
        "F" -> ImageResource(path = "F.jpg", drawable = Res.drawable.F)
        "G" -> ImageResource(path = "G.jpg", drawable = Res.drawable.G)
        "K" -> ImageResource(path = "K.jpg", drawable = Res.drawable.K)
        "M" -> ImageResource(path = "M.jpg", drawable = Res.drawable.M)
        "W" -> ImageResource(path = "W.jpg", drawable = Res.drawable.W)
        "L" -> ImageResource(path = "L.jpg", drawable = Res.drawable.L)
        "T" -> ImageResource(path = "T.jpg", drawable = Res.drawable.T)
        "Y" -> ImageResource(path = "Y.jpg", drawable = Res.drawable.Y)
        "C" -> ImageResource(path = "C.jpg", drawable = Res.drawable.C)
        "S" -> ImageResource(path = "S.jpg", drawable = Res.drawable.S)
        "D" -> ImageResource(path = "D.jpg", drawable = Res.drawable.D)
        else -> ImageResource(path = "unknown.jpg", drawable = Res.drawable.unknown)
    }

internal fun PlanetType?.toImage(): ImageResource =
    when (this) {
        PlanetType.SUB_EARTH -> ImageResource(path = "sub_earth.jpg", drawable = Res.drawable.sub_earth)
        PlanetType.SUPER_EARTH -> ImageResource(path = "super_earth.jpg", drawable = Res.drawable.super_earth)
        PlanetType.MEGA_EARTH -> ImageResource(path = "mega_earth.jpg", drawable = Res.drawable.mega_earth)
        PlanetType.MINI_NEPTUNE -> ImageResource(path = "mini_neptune.jpg", drawable = Res.drawable.mini_neptune)
        PlanetType.SUPER_NEPTUNE -> ImageResource(path = "super_neptune.jpg", drawable = Res.drawable.super_neptune)
        PlanetType.ICE_GIANT -> ImageResource(path = "ice_giant.jpg", drawable = Res.drawable.ice_giant)
        PlanetType.GAS_GIANT -> ImageResource(path = "gas_giant.jpg", drawable = Res.drawable.gas_giant)
        PlanetType.SUPER_JUPITER -> ImageResource(path = "super_jupiter.jpg", drawable = Res.drawable.super_jupiter)
        PlanetType.TERRESTRIAL_PLANET -> ImageResource(path = "terrestrial_planet.jpg", drawable = Res.drawable.terrestrial_planet)
        PlanetType.IRON_PLANET -> ImageResource(path = "iron_planet.jpg", drawable = Res.drawable.iron_planet)
        PlanetType.PUFFY_PLANET -> ImageResource(path = "puffy_planet.jpg", drawable = Res.drawable.puffy_planet)
        PlanetType.SUPER_PUFF_PLANET -> ImageResource(path = "super_puff_planet.jpg", drawable = Res.drawable.super_puff_planet)
        PlanetType.OCEAN_PLANET -> ImageResource(path = "ocean_planet.jpg", drawable = Res.drawable.ocean_planet)
        PlanetType.SUBSURFACE_OCEAN_PLANET -> ImageResource(path = "subsurface_ocean_planet.jpg", drawable = Res.drawable.subsurface_ocean_planet)
        PlanetType.LAVA_PLANET -> ImageResource(path = "lava_planet.jpg", drawable = Res.drawable.lava_planet)
        PlanetType.DESERT_PLANET -> ImageResource(path = "desert_planet.jpg", drawable = Res.drawable.desert_planet)
        PlanetType.ICE_PLANET -> ImageResource(path = "ice_planet.jpg", drawable = Res.drawable.ice_planet)
        PlanetType.HOT_JUPITER -> ImageResource(path = "hot_jupiter.jpg", drawable = Res.drawable.hot_jupiter)
        PlanetType.ULTRA_HOT_JUPITER -> ImageResource(path = "ultra_hot_jupiter.jpg", drawable = Res.drawable.ultra_hot_jupiter)
        PlanetType.HOT_NEPTUNE -> ImageResource(path = "hot_neptune.jpg", drawable = Res.drawable.hot_neptune)
        PlanetType.ULTRA_HOT_NEPTUNE -> ImageResource(path = "ultra_hot_neptune.jpg", drawable = Res.drawable.ultra_hot_neptune)
        PlanetType.ULTRA_SHORT_PERIOD_PLANET -> ImageResource(path = "ultra_short_period_planet.jpg", drawable = Res.drawable.ultra_short_period_planet)
        PlanetType.EYEBALL_PLANET -> ImageResource(path = "eyeball_planet.jpg", drawable = Res.drawable.eyeball_planet)
        PlanetType.HOT_EYEBALL_PLANET -> ImageResource(path = "hot_eyebal_planet.jpg", drawable = Res.drawable.hot_eyebal_planet)
        PlanetType.COLD_EYEBALL_PLANET -> ImageResource(path = "cold_eyeball_planet.jpg", drawable = Res.drawable.cold_eyeball_planet)
        PlanetType.AMMONIA_CLOUDS_GAS_GIANT -> ImageResource(path = "ammonia_clouds_gas_giant.jpg", drawable = Res.drawable.ammonia_clouds_gas_giant)
        PlanetType.WATER_CLOUDS_GAS_GIANT -> ImageResource(path = "water_clouds_gas_giant.jpg", drawable = Res.drawable.water_clouds_gas_giant)
        PlanetType.CLOUDLESS_GAS_GIANT -> ImageResource(path = "cloudless_gas_giant.jpg", drawable = Res.drawable.cloudless_gas_giant)
        PlanetType.ALKALI_METAL_CLOUDS_GAS_GIANT -> ImageResource(path = "alkali_metal_clouds_gas_giant.jpg", drawable = Res.drawable.alkali_metal_clouds_gas_giant)
        PlanetType.SILICATE_CLOUDS_GAS_GIANT -> ImageResource(path = "silicate_clouds_gas_giant.jpg", drawable = Res.drawable.silicate_clouds_gas_giant)
        PlanetType.BARREN_PLANET -> ImageResource(path = "barren_planet.jpg", drawable = Res.drawable.barren_planet)
        PlanetType.EARTH_LIKE_PLANET -> ImageResource(path = "earth_like_planet.jpg", drawable = Res.drawable.earth_like_planet)
        PlanetType.EARTH_ANALOG_PLANET -> ImageResource(path = "earth_analog_planet.jpg", drawable = Res.drawable.earth_analog_planet)
        PlanetType.SUPERHABITABLE_PLANET -> ImageResource(path = "super_habitable_planet.jpg", drawable = Res.drawable.super_habitable_planet)
        PlanetType.PROTOPLANET -> ImageResource(path = "protoplanet.jpg", drawable = Res.drawable.protoplanet)
        PlanetType.DISRUPTED_PLANET -> ImageResource(path = "disrupted_planet.jpg", drawable = Res.drawable.disrupted_planet)
        PlanetType.CHTHONIAN_PLANET -> ImageResource(path = "chthonian_planet.jpg", drawable = Res.drawable.chthonian_planet)
        PlanetType.CRATER_PLANET -> ImageResource(path = "crater_planet.jpg", drawable = Res.drawable.crater_planet)
        PlanetType.ELLIPSOID_PLANET -> ImageResource(path = "ellipsoid_planet.jpg", drawable = Res.drawable.ellipsoid_planet)
        PlanetType.UNKNOWN, null -> ImageResource(path = "barren_planet.jpg", drawable = Res.drawable.barren_planet)
    }

internal fun List<StellarHost>.addPlanets(planets: List<Planet>): List<StellarHost> {
    val planetMap = planets.groupBy { it.stellarHostId }
    return apply { forEach { it.planets.addAll(elements = planetMap[it.id].orEmpty()) } }
}

internal const val SUN = "sol"
