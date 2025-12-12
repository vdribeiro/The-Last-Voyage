package com.hybris.tlv.usecase.space

import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import com.hybris.tlv.image.A
import com.hybris.tlv.image.AlkaliMetalCloudsGasGiant
import com.hybris.tlv.image.AmmoniaCloudsGasGiant
import com.hybris.tlv.image.B
import com.hybris.tlv.image.BarrenPlanet
import com.hybris.tlv.image.C
import com.hybris.tlv.image.ChthonianPlanet
import com.hybris.tlv.image.CloudlessGasGiant
import com.hybris.tlv.image.ColdEyeballPlanet
import com.hybris.tlv.image.CraterPlanet
import com.hybris.tlv.image.D
import com.hybris.tlv.image.DesertPlanet
import com.hybris.tlv.image.DisruptedPlanet
import com.hybris.tlv.image.EarthAnalogPlanet
import com.hybris.tlv.image.EarthLikePlanet
import com.hybris.tlv.image.EllipsoidPlanet
import com.hybris.tlv.image.EyeballPlanet
import com.hybris.tlv.image.F
import com.hybris.tlv.image.G
import com.hybris.tlv.image.GasGiant
import com.hybris.tlv.image.HotEyeballPlanet
import com.hybris.tlv.image.HotJupiter
import com.hybris.tlv.image.HotNeptune
import com.hybris.tlv.image.IceGiant
import com.hybris.tlv.image.IcePlanet
import com.hybris.tlv.image.IronPlanet
import com.hybris.tlv.image.K
import com.hybris.tlv.image.L
import com.hybris.tlv.image.LavaPlanet
import com.hybris.tlv.image.M
import com.hybris.tlv.image.MegaEarth
import com.hybris.tlv.image.MiniNeptune
import com.hybris.tlv.image.O
import com.hybris.tlv.image.OceanPlanet
import com.hybris.tlv.image.Protoplanet
import com.hybris.tlv.image.PuffyPlanet
import com.hybris.tlv.image.S
import com.hybris.tlv.image.SilicateCloudsGasGiant
import com.hybris.tlv.image.SubEarth
import com.hybris.tlv.image.SubsurfaceOceanPlanet
import com.hybris.tlv.image.SuperEarth
import com.hybris.tlv.image.SuperHabitablePlanet
import com.hybris.tlv.image.SuperJupiter
import com.hybris.tlv.image.SuperNeptune
import com.hybris.tlv.image.SuperPuffPlanet
import com.hybris.tlv.image.T
import com.hybris.tlv.image.TerrestrialPlanet
import com.hybris.tlv.image.UltraHotJupiter
import com.hybris.tlv.image.UltraHotNeptune
import com.hybris.tlv.image.UltraShortPeriodPlanet
import com.hybris.tlv.image.Unknown
import com.hybris.tlv.image.W
import com.hybris.tlv.image.WaterCloudsGasGiant
import com.hybris.tlv.image.Y
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.usecase.space.formula.Constants.PARSEC
import com.hybris.tlv.usecase.space.formula.Constants.SUN_SURFACE_GRAVITY
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.model.StellarHost

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
        "O" -> O
        "B" -> B
        "A" -> A
        "F" -> F
        "G" -> G
        "K" -> K
        "M" -> M
        "W" -> W
        "L" -> L
        "T" -> T
        "Y" -> Y
        "C" -> C
        "S" -> S
        "D" -> D
        else -> Unknown
    }

internal fun PlanetType?.toImage(): ImageResource =
    when (this) {
        PlanetType.SUB_EARTH -> SubEarth
        PlanetType.SUPER_EARTH -> SuperEarth
        PlanetType.MEGA_EARTH -> MegaEarth
        PlanetType.MINI_NEPTUNE -> MiniNeptune
        PlanetType.SUPER_NEPTUNE -> SuperNeptune
        PlanetType.ICE_GIANT -> IceGiant
        PlanetType.GAS_GIANT -> GasGiant
        PlanetType.SUPER_JUPITER -> SuperJupiter
        PlanetType.TERRESTRIAL_PLANET -> TerrestrialPlanet
        PlanetType.IRON_PLANET -> IronPlanet
        PlanetType.PUFFY_PLANET -> PuffyPlanet
        PlanetType.SUPER_PUFF_PLANET -> SuperPuffPlanet
        PlanetType.OCEAN_PLANET -> OceanPlanet
        PlanetType.SUBSURFACE_OCEAN_PLANET -> SubsurfaceOceanPlanet
        PlanetType.LAVA_PLANET -> LavaPlanet
        PlanetType.DESERT_PLANET -> DesertPlanet
        PlanetType.ICE_PLANET -> IcePlanet
        PlanetType.HOT_JUPITER -> HotJupiter
        PlanetType.ULTRA_HOT_JUPITER -> UltraHotJupiter
        PlanetType.HOT_NEPTUNE -> HotNeptune
        PlanetType.ULTRA_HOT_NEPTUNE -> UltraHotNeptune
        PlanetType.ULTRA_SHORT_PERIOD_PLANET -> UltraShortPeriodPlanet
        PlanetType.EYEBALL_PLANET -> EyeballPlanet
        PlanetType.HOT_EYEBALL_PLANET -> HotEyeballPlanet
        PlanetType.COLD_EYEBALL_PLANET -> ColdEyeballPlanet
        PlanetType.AMMONIA_CLOUDS_GAS_GIANT -> AmmoniaCloudsGasGiant
        PlanetType.WATER_CLOUDS_GAS_GIANT -> WaterCloudsGasGiant
        PlanetType.CLOUDLESS_GAS_GIANT -> CloudlessGasGiant
        PlanetType.ALKALI_METAL_CLOUDS_GAS_GIANT -> AlkaliMetalCloudsGasGiant
        PlanetType.SILICATE_CLOUDS_GAS_GIANT -> SilicateCloudsGasGiant
        PlanetType.BARREN_PLANET -> BarrenPlanet
        PlanetType.EARTH_LIKE_PLANET -> EarthLikePlanet
        PlanetType.EARTH_ANALOG_PLANET -> EarthAnalogPlanet
        PlanetType.SUPERHABITABLE_PLANET -> SuperHabitablePlanet
        PlanetType.PROTOPLANET -> Protoplanet
        PlanetType.DISRUPTED_PLANET -> DisruptedPlanet
        PlanetType.CHTHONIAN_PLANET -> ChthonianPlanet
        PlanetType.CRATER_PLANET -> CraterPlanet
        PlanetType.ELLIPSOID_PLANET -> EllipsoidPlanet
        PlanetType.UNKNOWN, null -> BarrenPlanet
    }

internal fun List<StellarHost>.addPlanets(planets: List<Planet>): List<StellarHost> {
    val planetMap = planets.groupBy { it.stellarHostId }
    return apply { forEach { it.planets.addAll(elements = planetMap[it.id].orEmpty()) } }
}

internal const val SUN = "sol"
