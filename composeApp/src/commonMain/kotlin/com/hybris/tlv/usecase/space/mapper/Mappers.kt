package com.hybris.tlv.usecase.space.mapper

import com.hybris.tlv.database.PlanetSchema
import com.hybris.tlv.database.StellarHostSchema
import com.hybris.tlv.http.getDouble
import com.hybris.tlv.http.getString
import com.hybris.tlv.usecase.space.formula.Constants.PARSEC
import com.hybris.tlv.usecase.space.formula.Constants.SUN_SURFACE_GRAVITY
import com.hybris.tlv.usecase.space.model.CartesianPoint
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_DENSITY
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_ECCENTRICITY
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_EQUILIBRIUM_TEMPERATURE
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_HOST_ID
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_ID
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_INCLINATION
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_INSOLATION_FLUX
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_MASS
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_NAME
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_OBLIQUITY
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_ORBITAL_PERIOD
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_ORBIT_AXIS
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_RADIUS
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.PLANET_STATUS
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_AGE
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_DEC
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_DENSITY
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_DISTANCE
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_GRAVITY
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_HOST_ID
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_HOST_NAME
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_LUMINOSITY
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_MASS
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_METALLICITY
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_RA
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_RADIUS
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_ROTATIONAL_PERIOD
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_ROTATIONAL_VELOCITY
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_SPECTRAL_TYPE
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_SYSTEM_NAME
import com.hybris.tlv.usecase.space.remote.SpaceApi.Companion.STELLAR_HOST_TEMPERATURE
import com.hybris.tlv.usecase.space.remote.json.ExoplanetJson
import com.hybris.tlv.usecase.space.remote.json.ExoplanetJson.Companion.PLANET_OCCULTATION_DEPTH
import com.hybris.tlv.usecase.space.remote.json.StellarHostJson
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
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
import thelastvoyage.composeapp.generated.resources.P
import thelastvoyage.composeapp.generated.resources.Q
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

private fun Double.stellarHostGravityToSunGravity(): Double = 10.0.pow(x = this - SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)
private fun Double.sunGravityToStellarHostGravity(): Double = (log10(x = this) + SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)
private fun Double.parsecsToLightYears(): Double = this * PARSEC
private fun Double.lightYearsToParsecs(): Double = this / PARSEC

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

internal fun Double.roundTo(decimalPlaces: Int): Double {
    val factor = 10.0.pow(n = decimalPlaces)
    return round(x = this * factor) / factor
}

internal fun Double?.sanitize(): Double? = when {
    this == null -> null
    isNaN() || isInfinite() || this == Double.NEGATIVE_INFINITY || this == Double.POSITIVE_INFINITY || this == Double.NaN -> null
    else -> this
}

internal fun StellarHostJson.toStellarHost(): StellarHost =
    StellarHost(
        id = stellarHostName.toSnakeCase(),
        systemName = stellarHostSystemName?.toExpandedName(),
        name = stellarHostName.toExpandedName(),
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

internal fun ExoplanetJson.toStellarHost(): StellarHost =
    StellarHost(
        id = stellarHostName.toSnakeCase(),
        systemName = null, // Should be fetched from Stellar Hosts
        name = stellarHostName.toExpandedName(),
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

internal fun StellarHost.toStellarHostJson(): StellarHostJson =
    StellarHostJson(
        stellarHostSystemName = systemName,
        stellarHostName = name,
        stellarHostSpectralType = spectralType,
        stellarHostEffectiveTemperature = effectiveTemperature,
        stellarHostRadius = radius,
        stellarHostMass = mass,
        stellarHostMetallicity = metallicity,
        stellarHostLuminosity = luminosity,
        stellarHostGravity = gravity?.sunGravityToStellarHostGravity(),
        stellarHostAge = age,
        stellarHostDensity = density,
        stellarHostRotationalVelocity = rotationalVelocity,
        stellarHostRotationalPeriod = rotationalPeriod,
        stellarHostDistance = distance?.lightYearsToParsecs(),
        stellarHostRa = ra,
        stellarHostDec = dec
    )

internal fun Planet.toExoplanetJson(stellarHost: StellarHost): ExoplanetJson? =
    ExoplanetJson(
        stellarHostName = stellarHost.name,
        stellarHostSpectralType = stellarHost.spectralType,
        stellarHostEffectiveTemperature = stellarHost.effectiveTemperature,
        stellarHostRadius = stellarHost.radius,
        stellarHostMass = stellarHost.mass,
        stellarHostMetallicity = stellarHost.metallicity,
        stellarHostLuminosity = stellarHost.luminosity,
        stellarHostGravity = stellarHost.gravity?.sunGravityToStellarHostGravity(),
        stellarHostAge = stellarHost.age,
        stellarHostDensity = stellarHost.density,
        stellarHostRotationalVelocity = stellarHost.rotationalVelocity,
        stellarHostRotationalPeriod = stellarHost.rotationalPeriod,
        stellarHostDistance = stellarHost.distance?.lightYearsToParsecs(),
        stellarHostRa = stellarHost.ra,
        stellarHostDec = stellarHost.dec,
        planetName = name,
        planetStatus = status.name,
        planetOrbitalPeriod = orbitalPeriod,
        planetOrbitAxis = orbitAxis,
        planetRadius = radius,
        planetMass = mass,
        planetDensity = density,
        planetEccentricity = eccentricity,
        planetInsolationFlux = insolationFlux,
        planetEquilibriumTemperature = equilibriumTemperature,
        planetOccultationDepth = occultationDepth,
        planetInclination = inclination,
        planetObliquity = obliquity,
        planetProjectedObliquity = obliquity
    )

internal fun List<StellarHost>.mergeStellarHosts(): List<StellarHost> =
    groupBy { it.id }.mapNotNull { (id, group) ->
        StellarHost(
            id = id,
            systemName = group.map { it.systemName }.firstOrNull().orEmpty(),
            name = group.map { it.name }.firstOrNull().orEmpty(),
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

internal fun StellarHost.toStellarHostMap(): Map<String, Any> =
    buildMap {
        put(key = STELLAR_HOST_HOST_ID, value = id)
        put(key = STELLAR_HOST_HOST_NAME, value = name)
        systemName?.let { put(key = STELLAR_HOST_SYSTEM_NAME, value = it) }
        spectralType?.let { put(key = STELLAR_HOST_SPECTRAL_TYPE, value = it) }
        effectiveTemperature?.let { put(key = STELLAR_HOST_TEMPERATURE, value = it) }
        radius?.let { put(key = STELLAR_HOST_RADIUS, value = it) }
        mass?.let { put(key = STELLAR_HOST_MASS, value = it) }
        metallicity?.let { put(key = STELLAR_HOST_METALLICITY, value = it) }
        luminosity?.let { put(key = STELLAR_HOST_LUMINOSITY, value = it) }
        gravity?.let { put(key = STELLAR_HOST_GRAVITY, value = it) }
        age?.let { put(key = STELLAR_HOST_AGE, value = it) }
        density?.let { put(key = STELLAR_HOST_DENSITY, value = it) }
        rotationalVelocity?.let { put(key = STELLAR_HOST_ROTATIONAL_VELOCITY, value = it) }
        rotationalPeriod?.let { put(key = STELLAR_HOST_ROTATIONAL_PERIOD, value = it) }
        distance?.let { put(key = STELLAR_HOST_DISTANCE, value = it) }
        ra?.let { put(key = STELLAR_HOST_RA, value = it) }
        dec?.let { put(key = STELLAR_HOST_DEC, value = it) }
    }

internal fun Planet.toPlanetMap(): Map<String, Any> =
    buildMap {
        put(key = PLANET_ID, value = id)
        put(key = PLANET_NAME, value = name)
        put(key = PLANET_HOST_ID, value = stellarHostId)
        put(key = PLANET_STATUS, value = status.name)
        orbitalPeriod?.let { put(key = PLANET_ORBITAL_PERIOD, value = it) }
        orbitAxis?.let { put(key = PLANET_ORBIT_AXIS, value = it) }
        radius?.let { put(key = PLANET_RADIUS, value = it) }
        mass?.let { put(key = PLANET_MASS, value = it) }
        density?.let { put(key = PLANET_DENSITY, value = it) }
        eccentricity?.let { put(key = PLANET_ECCENTRICITY, value = it) }
        insolationFlux?.let { put(key = PLANET_INSOLATION_FLUX, value = it) }
        equilibriumTemperature?.let { put(key = PLANET_EQUILIBRIUM_TEMPERATURE, value = it) }
        occultationDepth?.let { put(key = PLANET_OCCULTATION_DEPTH, value = it) }
        inclination?.let { put(key = PLANET_INCLINATION, value = it) }
        obliquity?.let { put(key = PLANET_OBLIQUITY, value = it) }
    }

internal fun Map<String, Any>.toStellarHost(): StellarHost =
    StellarHost(
        id = getString(key = STELLAR_HOST_HOST_ID)!!,
        name = getString(key = STELLAR_HOST_HOST_NAME)!!,
        systemName = getString(key = STELLAR_HOST_HOST_NAME)!!,
        spectralType = getString(key = STELLAR_HOST_SPECTRAL_TYPE),
        effectiveTemperature = getDouble(key = STELLAR_HOST_TEMPERATURE),
        radius = getDouble(key = STELLAR_HOST_RADIUS),
        mass = getDouble(key = STELLAR_HOST_MASS),
        metallicity = getDouble(key = STELLAR_HOST_METALLICITY),
        luminosity = getDouble(key = STELLAR_HOST_LUMINOSITY),
        gravity = getDouble(key = STELLAR_HOST_GRAVITY),
        age = getDouble(key = STELLAR_HOST_AGE),
        density = getDouble(key = STELLAR_HOST_DENSITY),
        rotationalVelocity = getDouble(key = STELLAR_HOST_ROTATIONAL_VELOCITY),
        rotationalPeriod = getDouble(key = STELLAR_HOST_ROTATIONAL_PERIOD),
        distance = getDouble(key = STELLAR_HOST_DISTANCE),
        ra = getDouble(key = STELLAR_HOST_RA),
        dec = getDouble(key = STELLAR_HOST_DEC)
    )

internal fun Map<String, Any>.toPlanet(): Planet =
    Planet(
        id = getString(key = PLANET_ID)!!,
        name = getString(key = PLANET_NAME)!!,
        stellarHostId = getString(key = PLANET_HOST_ID)!!,
        status = getString(key = PLANET_STATUS)?.let { PlanetStatus.valueOf(value = it) } ?: PlanetStatus.FALSE,
        orbitalPeriod = getDouble(key = PLANET_ORBITAL_PERIOD),
        orbitAxis = getDouble(key = PLANET_ORBIT_AXIS),
        radius = getDouble(key = PLANET_RADIUS),
        mass = getDouble(key = PLANET_MASS),
        density = getDouble(key = PLANET_DENSITY),
        eccentricity = getDouble(key = PLANET_ECCENTRICITY),
        insolationFlux = getDouble(key = PLANET_INSOLATION_FLUX),
        equilibriumTemperature = getDouble(key = PLANET_EQUILIBRIUM_TEMPERATURE),
        occultationDepth = getDouble(key = PLANET_OCCULTATION_DEPTH),
        inclination = getDouble(key = PLANET_INCLINATION),
        obliquity = getDouble(key = PLANET_OBLIQUITY),
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
        "Q" -> Res.drawable.Q
        "P" -> Res.drawable.P
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
        null -> Res.drawable.barren_planet
    }
