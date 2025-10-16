package com.hybris.tlv.usecase.space.formula

import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import org.jetbrains.compose.resources.DrawableResource
import com.hybris.tlv.usecase.space.formula.Constants.PARSEC
import com.hybris.tlv.usecase.space.formula.Constants.SUN_SURFACE_GRAVITY
import com.hybris.tlv.usecase.space.model.PlanetType
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
    isNaN() || isInfinite() || this == Double.NEGATIVE_INFINITY || this == Double.POSITIVE_INFINITY || this == Double.NaN -> null
    else -> this
}

internal fun Double.stellarHostGravityToSunGravity(): Double = 10.0.pow(x = this - SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)

internal fun Double.sunGravityToStellarHostGravity(): Double = (log10(x = this) + SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)

internal fun Double.parsecsToLightYears(): Double = this * PARSEC

internal fun Double.lightYearsToParsecs(): Double = this / PARSEC

internal fun String?.spectralTypeToDrawable(): DrawableResource =
    when (this?.firstOrNull()?.uppercase()) {
        "O" -> Res.drawable.O
        "B" -> Res.drawable.B
        "A" -> Res.drawable.A
        "F" -> Res.drawable.F
        "G" -> Res.drawable.G
        "K" -> Res.drawable.K
        "M" -> Res.drawable.M
        "W" -> Res.drawable.W
        "L" -> Res.drawable.L
        "T" -> Res.drawable.T
        "Y" -> Res.drawable.Y
        "C" -> Res.drawable.C
        "S" -> Res.drawable.S
        "D" -> Res.drawable.D
        else -> Res.drawable.unknown
    }

internal fun PlanetType?.toDrawable(): DrawableResource =
    when (this) {
        PlanetType.SUB_EARTH -> Res.drawable.sub_earth
        PlanetType.SUPER_EARTH -> Res.drawable.super_earth
        PlanetType.MEGA_EARTH -> Res.drawable.mega_earth
        PlanetType.MINI_NEPTUNE -> Res.drawable.mini_neptune
        PlanetType.SUPER_NEPTUNE -> Res.drawable.super_neptune
        PlanetType.ICE_GIANT -> Res.drawable.ice_giant
        PlanetType.GAS_GIANT -> Res.drawable.gas_giant
        PlanetType.SUPER_JUPITER -> Res.drawable.super_jupiter
        PlanetType.TERRESTRIAL_PLANET -> Res.drawable.terrestrial_planet
        PlanetType.IRON_PLANET -> Res.drawable.iron_planet
        PlanetType.PUFFY_PLANET -> Res.drawable.puffy_planet
        PlanetType.SUPER_PUFF_PLANET -> Res.drawable.super_puff_planet
        PlanetType.OCEAN_PLANET -> Res.drawable.ocean_planet
        PlanetType.SUBSURFACE_OCEAN_PLANET -> Res.drawable.subsurface_ocean_planet
        PlanetType.LAVA_PLANET -> Res.drawable.lava_planet
        PlanetType.DESERT_PLANET -> Res.drawable.desert_planet
        PlanetType.ICE_PLANET -> Res.drawable.ice_planet
        PlanetType.HOT_JUPITER -> Res.drawable.hot_jupiter
        PlanetType.ULTRA_HOT_JUPITER -> Res.drawable.ultra_hot_jupiter
        PlanetType.HOT_NEPTUNE -> Res.drawable.hot_neptune
        PlanetType.ULTRA_HOT_NEPTUNE -> Res.drawable.ultra_hot_neptune
        PlanetType.ULTRA_SHORT_PERIOD_PLANET -> Res.drawable.ultra_short_period_planet
        PlanetType.EYEBALL_PLANET -> Res.drawable.eyeball_planet
        PlanetType.HOT_EYEBALL_PLANET -> Res.drawable.hot_eyebal_planet
        PlanetType.COLD_EYEBALL_PLANET -> Res.drawable.cold_eyeball_planet
        PlanetType.AMMONIA_CLOUDS_GAS_GIANT -> Res.drawable.ammonia_clouds_gas_giant
        PlanetType.WATER_CLOUDS_GAS_GIANT -> Res.drawable.water_clouds_gas_giant
        PlanetType.CLOUDLESS_GAS_GIANT -> Res.drawable.cloudless_gas_giant
        PlanetType.ALKALI_METAL_CLOUDS_GAS_GIANT -> Res.drawable.alkali_metal_clouds_gas_giant
        PlanetType.SILICATE_CLOUDS_GAS_GIANT -> Res.drawable.silicate_clouds_gas_giant
        PlanetType.BARREN_PLANET -> Res.drawable.barren_planet
        PlanetType.EARTH_LIKE_PLANET -> Res.drawable.earth_like_planet
        PlanetType.EARTH_ANALOG_PLANET -> Res.drawable.earth_analog_planet
        PlanetType.SUPERHABITABLE_PLANET -> Res.drawable.super_habitable_planet
        PlanetType.PROTOPLANET -> Res.drawable.protoplanet
        PlanetType.DISRUPTED_PLANET -> Res.drawable.disrupted_planet
        PlanetType.CHTHONIAN_PLANET -> Res.drawable.chthonian_planet
        PlanetType.CRATER_PLANET -> Res.drawable.crater_planet
        PlanetType.ELLIPSOID_PLANET -> Res.drawable.ellipsoid_planet
        PlanetType.UNKNOWN, null -> Res.drawable.barren_planet
    }
