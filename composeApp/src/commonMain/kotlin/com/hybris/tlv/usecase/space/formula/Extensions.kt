package com.hybris.tlv.usecase.space.formula

import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import com.hybris.tlv.usecase.space.formula.Constants.PARSEC
import com.hybris.tlv.usecase.space.formula.Constants.SUN_SURFACE_GRAVITY
import com.hybris.tlv.usecase.space.model.PlanetType

internal fun Double.roundTo(decimalPlaces: Int): Double {
    val factor = 10.0.pow(n = decimalPlaces)
    return round(x = this * factor) / factor
}

internal fun Double?.sanitize(): Double? = when {
    this == null -> null
    isNaN() || isInfinite() || this == Double.NEGATIVE_INFINITY || this == Double.POSITIVE_INFINITY || this == Double.NaN -> null
    else -> this
}

internal fun Double.stellarHostGravityToSunGravity(): Double = 10.0.pow(x = this - SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)

internal fun Double.sunGravityToStellarHostGravity(): Double = (log10(x = this) + SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)

internal fun Double.parsecsToLightYears(): Double = this * PARSEC

internal fun Double.lightYearsToParsecs(): Double = this / PARSEC

internal fun String?.spectralTypeToImage(): String =
    when (this?.firstOrNull()?.uppercase()) {
        "O" -> "O.jpg"
        "B" -> "B.jpg"
        "A" -> "A.jpg"
        "F" -> "F.jpg"
        "G" -> "G.jpg"
        "K" -> "K.jpg"
        "M" -> "M.jpg"
        "W" -> "W.jpg"
        "L" -> "L.jpg"
        "T" -> "T.jpg"
        "Y" -> "Y.jpg"
        "C" -> "C.jpg"
        "S" -> "S.jpg"
        "D" -> "D.jpg"
        else -> "unknown.jpg"
    }

internal fun PlanetType?.toImage(): String =
    when (this) {
        PlanetType.SUB_EARTH -> "sub_earth.jpg"
        PlanetType.SUPER_EARTH -> "super_earth.jpg"
        PlanetType.MEGA_EARTH -> "mega_earth.jpg"
        PlanetType.MINI_NEPTUNE -> "mini_neptune.jpg"
        PlanetType.SUPER_NEPTUNE -> "super_neptune.jpg"
        PlanetType.ICE_GIANT -> "ice_giant.jpg"
        PlanetType.GAS_GIANT -> "gas_giant.jpg"
        PlanetType.SUPER_JUPITER -> "super_jupiter.jpg"
        PlanetType.TERRESTRIAL_PLANET -> "terrestrial_planet.jpg"
        PlanetType.IRON_PLANET -> "iron_planet.jpg"
        PlanetType.PUFFY_PLANET -> "puffy_planet.jpg"
        PlanetType.SUPER_PUFF_PLANET -> "super_puff_planet.jpg"
        PlanetType.OCEAN_PLANET -> "ocean_planet.jpg"
        PlanetType.SUBSURFACE_OCEAN_PLANET -> "subsurface_ocean_planet.jpg"
        PlanetType.LAVA_PLANET -> "lava_planet.jpg"
        PlanetType.DESERT_PLANET -> "desert_planet.jpg"
        PlanetType.ICE_PLANET -> "ice_planet.jpg"
        PlanetType.HOT_JUPITER -> "hot_jupiter.jpg"
        PlanetType.ULTRA_HOT_JUPITER -> "ultra_hot_jupiter.jpg"
        PlanetType.HOT_NEPTUNE -> "hot_neptune.jpg"
        PlanetType.ULTRA_HOT_NEPTUNE -> "ultra_hot_neptune.jpg"
        PlanetType.ULTRA_SHORT_PERIOD_PLANET -> "ultra_short_period_planet.jpg"
        PlanetType.EYEBALL_PLANET -> "eyeball_planet.jpg"
        PlanetType.HOT_EYEBALL_PLANET -> "hot_eyebal_planet.jpg"
        PlanetType.COLD_EYEBALL_PLANET -> "cold_eyeball_planet.jpg"
        PlanetType.AMMONIA_CLOUDS_GAS_GIANT -> "ammonia_clouds_gas_giant.jpg"
        PlanetType.WATER_CLOUDS_GAS_GIANT -> "water_clouds_gas_giant.jpg"
        PlanetType.CLOUDLESS_GAS_GIANT -> "cloudless_gas_giant.jpg"
        PlanetType.ALKALI_METAL_CLOUDS_GAS_GIANT -> "alkali_metal_clouds_gas_giant.jpg"
        PlanetType.SILICATE_CLOUDS_GAS_GIANT -> "silicate_clouds_gas_giant.jpg"
        PlanetType.BARREN_PLANET -> "barren_planet.jpg"
        PlanetType.EARTH_LIKE_PLANET -> "earth_like_planet.jpg"
        PlanetType.EARTH_ANALOG_PLANET -> "earth_analog_planet.jpg"
        PlanetType.SUPERHABITABLE_PLANET -> "super_habitable_planet.jpg"
        PlanetType.PROTOPLANET -> "protoplanet.jpg"
        PlanetType.DISRUPTED_PLANET -> "disrupted_planet.jpg"
        PlanetType.CHTHONIAN_PLANET -> "chthonian_planet.jpg"
        PlanetType.CRATER_PLANET -> "crater_planet.jpg"
        PlanetType.ELLIPSOID_PLANET -> "ellipsoid_planet.jpg"
        PlanetType.UNKNOWN, null -> "barren_planet.jpg"
    }
