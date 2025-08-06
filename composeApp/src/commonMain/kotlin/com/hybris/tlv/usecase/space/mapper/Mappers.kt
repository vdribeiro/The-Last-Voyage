package com.hybris.tlv.usecase.space.mapper

import com.hybris.tlv.database.PlanetSchema
import com.hybris.tlv.database.StellarHostSchema
import com.hybris.tlv.http.getDouble
import com.hybris.tlv.http.getString
import com.hybris.tlv.usecase.space.model.CartesianPoint
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
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

// Sun's approximate surface gravity in cm/s^2
private const val SUN_SURFACE_GRAVITY = 4.4378
// A Parsec in light-years
private const val PARSEC = 3.26156

private fun Double.stellarHostGravityToSunGravity(): Double = 10.0.pow(x = this - SUN_SURFACE_GRAVITY)
private fun Double.sunGravityToStellarHostGravity(): Double = log10(x = this) + SUN_SURFACE_GRAVITY
private fun Double.parsecsToLightYears(): Double = this * PARSEC
private fun Double.lightYearsToParsecs(): Double = this / PARSEC

internal fun StellarHostJson.toStellarHost(): StellarHost =
    StellarHost(
        id = stellarHostName.toSnakeCase(),
        systemName = stellarHostSystemName.toExpandedName(),
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
        systemName = stellarHostName.toExpandedName(), // Should be fetched from Stellar Hosts
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
        put(key = STELLAR_HOST_SYSTEM_NAME, value = systemName)
        put(key = STELLAR_HOST_HOST_NAME, value = name)
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
        systemName = getString(key = STELLAR_HOST_HOST_NAME)!!,
        name = getString(key = STELLAR_HOST_HOST_NAME)!!,
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
        systemName = systemName,
        name = name,
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
        systemName = systemName,
        name = name,
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
