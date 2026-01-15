package com.hybris.tlv.usecase.space

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import com.hybris.tlv.database.PlanetSchema
import com.hybris.tlv.database.StellarHostSchema
import com.hybris.tlv.resource.A
import com.hybris.tlv.resource.AlkaliMetalCloudsGasGiant
import com.hybris.tlv.resource.AmmoniaCloudsGasGiant
import com.hybris.tlv.resource.B
import com.hybris.tlv.resource.BarrenPlanet
import com.hybris.tlv.resource.C
import com.hybris.tlv.resource.ChthonianPlanet
import com.hybris.tlv.resource.CloudlessGasGiant
import com.hybris.tlv.resource.ColdEyeballPlanet
import com.hybris.tlv.resource.CraterPlanet
import com.hybris.tlv.resource.D
import com.hybris.tlv.resource.DesertPlanet
import com.hybris.tlv.resource.DisruptedPlanet
import com.hybris.tlv.resource.EarthAnalogPlanet
import com.hybris.tlv.resource.EarthLikePlanet
import com.hybris.tlv.resource.EllipsoidPlanet
import com.hybris.tlv.resource.EyeballPlanet
import com.hybris.tlv.resource.F
import com.hybris.tlv.resource.G
import com.hybris.tlv.resource.GasGiant
import com.hybris.tlv.resource.HotEyeballPlanet
import com.hybris.tlv.resource.HotJupiter
import com.hybris.tlv.resource.HotNeptune
import com.hybris.tlv.resource.IceGiant
import com.hybris.tlv.resource.IcePlanet
import com.hybris.tlv.resource.ImageResource
import com.hybris.tlv.resource.IronPlanet
import com.hybris.tlv.resource.K
import com.hybris.tlv.resource.L
import com.hybris.tlv.resource.LavaPlanet
import com.hybris.tlv.resource.M
import com.hybris.tlv.resource.MegaEarth
import com.hybris.tlv.resource.MiniNeptune
import com.hybris.tlv.resource.O
import com.hybris.tlv.resource.OceanPlanet
import com.hybris.tlv.resource.Protoplanet
import com.hybris.tlv.resource.PuffyPlanet
import com.hybris.tlv.resource.S
import com.hybris.tlv.resource.SilicateCloudsGasGiant
import com.hybris.tlv.resource.SubEarth
import com.hybris.tlv.resource.SubsurfaceOceanPlanet
import com.hybris.tlv.resource.SuperEarth
import com.hybris.tlv.resource.SuperHabitablePlanet
import com.hybris.tlv.resource.SuperJupiter
import com.hybris.tlv.resource.SuperNeptune
import com.hybris.tlv.resource.SuperPuffPlanet
import com.hybris.tlv.resource.T
import com.hybris.tlv.resource.TerrestrialPlanet
import com.hybris.tlv.resource.UltraHotJupiter
import com.hybris.tlv.resource.UltraHotNeptune
import com.hybris.tlv.resource.UltraShortPeriodPlanet
import com.hybris.tlv.resource.Unknown
import com.hybris.tlv.resource.W
import com.hybris.tlv.resource.WaterCloudsGasGiant
import com.hybris.tlv.resource.Y
import com.hybris.tlv.usecase.space.formula.Constants.PARSEC
import com.hybris.tlv.usecase.space.formula.Constants.SUN_SURFACE_GRAVITY
import com.hybris.tlv.usecase.space.model.CartesianPoint
import com.hybris.tlv.usecase.space.model.ExoplanetJson
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.model.StellarHostJson

internal fun StellarHostJson.toStellarHost(): StellarHost =
    StellarHost(
        id = stellarHostName.toSnakeCase(),
        name = stellarHostName.toExpandedName(),
        systemName = stellarHostSystemName?.toExpandedName(),
        spectralType = stellarHostSpectralType
            ?.replace(regex = "\\s".toRegex(), replacement = "")
            ?.uppercase(),
        effectiveTemperature = stellarHostEffectiveTemperature,
        radius = stellarHostRadius,
        mass = stellarHostMass,
        metallicity = stellarHostMetallicity,
        luminosity = stellarHostLuminosity,
        gravity = stellarHostGravity?.stellarHostGravityToSunGravity(),
        age = stellarHostAge,
        density = stellarHostDensity,
        rotationalVelocity = stellarHostRotationalVelocity,
        rotationalPeriod = stellarHostRotationalPeriod,
        distance = stellarHostDistance?.parsecsToLightYears(),
        ra = stellarHostRa,
        dec = stellarHostDec
    )

internal fun ExoplanetJson.toStellarHost(systemName: String?): StellarHost =
    StellarHost(
        id = stellarHostName.toSnakeCase(),
        name = stellarHostName.toExpandedName(),
        systemName = systemName,
        spectralType = stellarHostSpectralType
            ?.replace(regex = "\\s".toRegex(), replacement = "")
            ?.uppercase(),
        effectiveTemperature = stellarHostEffectiveTemperature,
        radius = stellarHostRadius,
        mass = stellarHostMass,
        metallicity = stellarHostMetallicity,
        luminosity = stellarHostLuminosity,
        gravity = stellarHostGravity?.stellarHostGravityToSunGravity(),
        age = stellarHostAge,
        density = stellarHostDensity,
        rotationalVelocity = stellarHostRotationalVelocity,
        rotationalPeriod = stellarHostRotationalPeriod,
        distance = stellarHostDistance?.parsecsToLightYears(),
        ra = stellarHostRa,
        dec = stellarHostDec
    )

internal fun ExoplanetJson.toPlanet(): Planet =
    Planet(
        id = planetName.toSnakeCase(),
        name = planetName.toExpandedName(),
        stellarHostId = stellarHostName.toSnakeCase(),
        status = when (planetStatus?.lowercase()) {
            null, PlanetStatus.CONFIRMED.name.lowercase() -> PlanetStatus.CONFIRMED
            PlanetStatus.CANDIDATE.name.lowercase() -> PlanetStatus.CANDIDATE
            else -> PlanetStatus.FALSE
        },
        orbitalPeriod = planetOrbitalPeriod,
        orbitAxis = planetOrbitAxis,
        radius = planetRadius,
        mass = planetMass,
        density = planetDensity,
        eccentricity = planetEccentricity,
        insolationFlux = planetInsolationFlux,
        equilibriumTemperature = planetEquilibriumTemperature,
        occultationDepth = planetOccultationDepth,
        inclination = planetInclination,
        obliquity = planetObliquity ?: planetProjectedObliquity,
    )

internal fun StellarHost.toStellarHostSchema(): StellarHostSchema =
    StellarHostSchema(
        id = id,
        name = name,
        systemName = systemName,
        spectralType = spectralType,
        effectiveTemperature = effectiveTemperature,
        radius = radius,
        mass = mass,
        metallicity = metallicity,
        luminosity = luminosity,
        gravity = gravity,
        age = age,
        density = density,
        rotationalVelocity = rotationalVelocity,
        rotationalPeriod = rotationalPeriod,
        distance = distance,
        ra = ra,
        dec = dec
    )

internal fun StellarHostSchema.toStellarHost(): StellarHost =
    StellarHost(
        id = id,
        name = name,
        systemName = systemName,
        spectralType = spectralType,
        effectiveTemperature = effectiveTemperature,
        radius = radius,
        mass = mass,
        metallicity = metallicity,
        luminosity = luminosity,
        gravity = gravity,
        age = age,
        density = density,
        rotationalVelocity = rotationalVelocity,
        rotationalPeriod = rotationalPeriod,
        distance = distance,
        ra = ra,
        dec = dec
    )

internal fun Planet.toPlanetSchema(): PlanetSchema =
    PlanetSchema(
        id = id,
        name = name,
        stellarHostId = stellarHostId,
        status = status,
        orbitalPeriod = orbitalPeriod,
        orbitAxis = orbitAxis,
        radius = radius,
        mass = mass,
        density = density,
        eccentricity = eccentricity,
        insolationFlux = insolationFlux,
        equilibriumTemperature = equilibriumTemperature,
        occultationDepth = occultationDepth,
        inclination = inclination,
        obliquity = obliquity,
    )

internal fun PlanetSchema.toPlanet(): Planet =
    Planet(
        id = id,
        name = name,
        stellarHostId = stellarHostId,
        status = status,
        orbitalPeriod = orbitalPeriod,
        orbitAxis = orbitAxis,
        radius = radius,
        mass = mass,
        density = density,
        eccentricity = eccentricity,
        insolationFlux = insolationFlux,
        equilibriumTemperature = equilibriumTemperature,
        occultationDepth = occultationDepth,
        inclination = inclination,
        obliquity = obliquity,
    )

internal fun StellarHost.toCartesian(): CartesianPoint? {
    if (ra == null || dec == null || distance == null) return null
    val raRad = ra * PI / 180.0
    val decRad = dec * PI / 180.0
    return CartesianPoint(
        x = distance * cos(x = decRad) * cos(x = raRad),
        y = distance * cos(x = decRad) * sin(x = raRad),
        z = distance * sin(x = decRad)
    )
}

private val greekAbbreviations = mapOf(
    "Alf" to "Alpha",
    "Bet" to "Beta",
    "Gam" to "Gamma",
    "Del" to "Delta",
    "Eps" to "Epsilon",
    "Zet" to "Zeta",
    "Eta" to "Eta",
    "The" to "Theta",
    "Iot" to "Iota",
    "Kap" to "Kappa",
    "Lam" to "Lambda",
    "Mu" to "Mu",
    "Nu" to "Nu",
    "Xi" to "Xi",
    "Omi" to "Omicron",
    "Pi" to "Pi",
    "Rho" to "Rho",
    "Sig" to "Sigma",
    "Tau" to "Tau",
    "Ups" to "Upsilon",
    "Phi" to "Phi",
    "Chi" to "Chi",
    "Psi" to "Psi",
    "Ome" to "Omega"
)
private val latinAbbreviations = mapOf(
    "And" to "Andromedae",
    "Ant" to "Antliae",
    "Aps" to "Apodis",
    "Aqr" to "Aquarii",
    "Aql" to "Aquilae",
    "Ara" to "Arae",
    "Ari" to "Arietis",
    "Aur" to "Aurigae",
    "Boo" to "Bootis",
    "Cae" to "Caeli",
    "Cam" to "Camelopardalis",
    "Cnc" to "Cancri",
    "CVn" to "Canum Venaticorum",
    "CMa" to "Canis Majoris",
    "CMi" to "Canis Minoris",
    "Cap" to "Capricorni",
    "Car" to "Carinae",
    "Cas" to "Cassiopeiae",
    "Cen" to "Centauri",
    "Cep" to "Cephei",
    "Cet" to "Ceti",
    "Cha" to "Chamaeleontis",
    "Cir" to "Circini",
    "Col" to "Columbae",
    "Com" to "Comae Berenices",
    "CrA" to "Coronae Australis",
    "CrB" to "Coronae Borealis",
    "Crv" to "Corvi",
    "Crt" to "Crateris",
    "Cru" to "Crucis",
    "Cyg" to "Cygni",
    "Del" to "Delphini",
    "Dor" to "Doradus",
    "Dra" to "Draconis",
    "Equ" to "Equulei",
    "Eri" to "Eridani",
    "For" to "Fornacis",
    "Gem" to "Geminorum",
    "Gru" to "Gruis",
    "Her" to "Herculis",
    "Hor" to "Horologii",
    "Hya" to "Hydrae",
    "Hyi" to "Hydri",
    "Ind" to "Indi",
    "Lac" to "Lacertae",
    "Leo" to "Leonis",
    "LMi" to "Leonis Minoris",
    "Lep" to "Leporis",
    "Lib" to "Librae",
    "Lup" to "Lupi",
    "Lyn" to "Lyncis",
    "Lyr" to "Lyrae",
    "Men" to "Mensae",
    "Mic" to "Microscopii",
    "Mon" to "Monocerotis",
    "Mus" to "Muscae",
    "Nor" to "Normae",
    "Oct" to "Octantis",
    "Oph" to "Ophiuchi",
    "Ori" to "Orionis",
    "Pav" to "Pavonis",
    "Peg" to "Pegasi",
    "Per" to "Persei",
    "Phe" to "Phoenicis",
    "Pic" to "Pictoris",
    "Psc" to "Piscium",
    "PsA" to "Piscis Austrini",
    "Pup" to "Puppis",
    "Pyx" to "Pyxidis",
    "Ret" to "Reticuli",
    "Sge" to "Sagittae",
    "Sgr" to "Sagittarii",
    "Sco" to "Scorpii",
    "Scl" to "Sculptoris",
    "Sct" to "Scuti",
    "Ser" to "Serpentis",
    "Sex" to "Sextantis",
    "Tau" to "Tauri",
    "Tel" to "Telescopii",
    "Tri" to "Trianguli",
    "TrA" to "Trianguli Australis",
    "Tuc" to "Tucanae",
    "UMa" to "Ursae Majoris",
    "UMi" to "Ursae Minoris",
    "Vel" to "Velorum",
    "Vir" to "Virginis",
    "Vol" to "Volantis",
    "Vul" to "Vulpeculae"
)
private val allAbbreviations = (greekAbbreviations + latinAbbreviations).mapKeys { it.key.lowercase() }
private val allAbbreviationsPattern =
    "\\b(${allAbbreviations.keys.joinToString(separator = "|")})\\b".toRegex(option = RegexOption.IGNORE_CASE)

private fun String.toExpandedName() = allAbbreviationsPattern.replace(input = replace(oldValue = "_", newValue = " ")) { matchResult ->
    val key = matchResult.value.lowercase()
    allAbbreviations[key] ?: matchResult.value
}

private fun String.toSnakeCase(): String =
    lowercase().replace(oldValue = " ", newValue = "_").replace(oldValue = "-", newValue = "_")

internal fun Double.roundTo(decimalPlaces: Int): Double {
    val factor = 10.0.pow(n = decimalPlaces)
    return round(x = this * factor) / factor
}

internal fun Double?.sanitize(): Double? = when {
    this == null -> null
    isNaN() || isInfinite() || this == Double.NEGATIVE_INFINITY || this == Double.POSITIVE_INFINITY -> null
    else -> this
}

private fun Double.stellarHostGravityToSunGravity(): Double = 10.0.pow(x = this - SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)

internal fun Double.sunGravityToStellarHostGravity(): Double = (log10(x = this) + SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)

private fun Double.parsecsToLightYears(): Double = this * PARSEC

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

internal fun List<StellarHost>.mergeStellarHosts(): List<StellarHost> =
    groupBy { it.id }.mapNotNull { (id, group) ->
        StellarHost(
            id = id,
            name = group.map { it.name }.firstOrNull().orEmpty(),
            systemName = group.mapNotNull { it.systemName }.ifEmpty { null }?.firstOrNull(),
            spectralType = group.mapNotNull { it.spectralType }.ifEmpty { null }?.firstOrNull(),
            effectiveTemperature = group.mapNotNull { it.effectiveTemperature }.ifEmpty { null }?.average(),
            radius = group.mapNotNull { it.radius }.ifEmpty { null }?.average(),
            mass = group.mapNotNull { it.mass }.ifEmpty { null }?.average(),
            metallicity = group.mapNotNull { it.metallicity }.ifEmpty { null }?.average(),
            luminosity = group.mapNotNull { it.luminosity }.ifEmpty { null }?.average(),
            gravity = group.mapNotNull { it.gravity }.ifEmpty { null }?.average(),
            age = group.mapNotNull { it.age }.ifEmpty { null }?.average(),
            density = group.mapNotNull { it.density }.ifEmpty { null }?.average(),
            rotationalVelocity = group.mapNotNull { it.rotationalVelocity }.ifEmpty { null }?.average(),
            rotationalPeriod = group.mapNotNull { it.rotationalPeriod }.ifEmpty { null }?.average(),
            distance = group.mapNotNull { it.distance }.ifEmpty { null }?.average(),
            ra = group.mapNotNull { it.ra }.ifEmpty { null }?.average(),
            dec = group.mapNotNull { it.dec }.ifEmpty { null }?.average()
        )
    }

internal fun List<Planet>.mergePlanets(): List<Planet> =
    groupBy { it.id }.mapNotNull { (id, group) ->
        Planet(
            id = id,
            name = group.map { it.name }.firstOrNull().orEmpty(),
            stellarHostId = group.firstNotNullOf { it.stellarHostId },
            status = (group.find { it.status == PlanetStatus.CONFIRMED }
                ?: group.find { it.status == PlanetStatus.CANDIDATE })?.status
                ?: PlanetStatus.FALSE,
            orbitalPeriod = group.mapNotNull { it.orbitalPeriod }.ifEmpty { null }?.average(),
            orbitAxis = group.mapNotNull { it.orbitAxis }.ifEmpty { null }?.average(),
            radius = group.mapNotNull { it.radius }.ifEmpty { null }?.average(),
            mass = group.mapNotNull { it.mass }.ifEmpty { null }?.average(),
            density = group.mapNotNull { it.density }.ifEmpty { null }?.average(),
            eccentricity = group.mapNotNull { it.eccentricity }.ifEmpty { null }?.average(),
            insolationFlux = group.mapNotNull { it.insolationFlux }.ifEmpty { null }?.average(),
            equilibriumTemperature = group.mapNotNull { it.equilibriumTemperature }.ifEmpty { null }?.average(),
            occultationDepth = group.mapNotNull { it.occultationDepth }.ifEmpty { null }?.average(),
            inclination = group.mapNotNull { it.inclination }.ifEmpty { null }?.average(),
            obliquity = group.mapNotNull { it.obliquity }.ifEmpty { null }?.average(),
        )
    }

internal fun List<StellarHost>.addPlanets(planets: List<Planet>): List<StellarHost> {
    val planetMap = planets.groupBy { it.stellarHostId }
    return apply { forEach { it.planets.addAll(elements = planetMap[it.id].orEmpty()) } }
}

internal const val SUN = "sol"
