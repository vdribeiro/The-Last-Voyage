package com.hybris.tlv.usecase.exoplanet.model

internal enum class PlanetType(val displayName: String) {
    TERRESTRIAL(displayName = "planet_type_"),
    // density > 3.0 g/cm³ AND radius < 2.5 Earth radii
    // density > 3.0 g/cm³ or calculatePlanetTelluricityScore() > 0.5.
    ICE_PLANET(displayName = "planet_type_"),
    // Qualifies as a Terrestrial Planet AND equilibriumTemperature < 200 K
    GAS_GIANT(displayName = "planet_type_"),
    // density < 2.0 g/cm³ AND radius > 4.0 Earth radii
    // density < 2.5 g/cm³ AND radius > 4.0
    ICE_GIANT(displayName = "planet_type_"),
    // Meets Gas Giant criteria AND equilibriumTemperature < 150 K
    // mass is between ~10 and 50 Earth masses AND radius is between ~3 and 6 Earth radii.
    SUB_EARTH(displayName = "planet_type_"),
    // Meets Terrestrial criteria AND mass < 0.5 Earth masses
    // radius < 1.0 AND mass < 1.0 AND qualifies as a Terrestrial Planet.
    SUPER_EARTH(displayName = "planet_type_"),
    // Meets Terrestrial criteria AND mass BETWEEN 1.0 AND 10.0 Earth masses
    // (radius > 1.0 OR mass > 1.0) AND radius < 1.75 AND qualifies as a Terrestrial Planet.
    MEGA_EARTH(displayName = "planet_type_"),
    // Meets Terrestrial criteria AND mass > 10.0 Earth masses
    // mass > 10 AND qualifies as a Super-Earth
    MINI_NEPTUNE(displayName = "planet_type_"),
    // radius BETWEEN 2.0 AND 4.0 Earth radii AND density < 2.0 g/cm³
    // radius is between 1.75 and 3.5
    HOT_JUPITER(displayName = "planet_type_"),
    // Meets Gas Giant criteria AND orbitalPeriod < 10 days
    // Qualifies as a Gas Giant AND (orbitalPeriod < 10 days OR orbitAxis < 0.1 AU)
    ULTRA_HOT_JUPITER(displayName = "planet_type_"),
    // Meets Hot Jupiter criteria AND equilibriumTemperature > 2000 K
    // Qualifies as a Hot Jupiter AND equilibriumTemperature > 2000 K
    SUPER_JUPITER(displayName = "planet_type_"),
    //mass > 350 Earth masses (Jupiter is ~318)
    HOT_NEPTUNE(displayName = "planet_type_"),
    // mass BETWEEN 10 AND 50 Earth masses AND orbitalPeriod < 10 days
    // Qualifies as an Ice Giant or Mini-Neptune AND (orbitalPeriod < 10 days OR orbitAxis < 0.1 AU)
    LAVA_PLANET(displayName = "planet_type_"),
    // Meets Terrestrial criteria AND equilibriumTemperature > 1500 K
    // Qualifies as a Terrestrial Planet AND equilibriumTemperature > 1200 K
    IRON_PLANET(displayName = "planet_type_"),
    // Meets Terrestrial criteria AND density > 8.0 g/cm³
    // density > 8.0 g/cm³
    CHTHONIAN_PLANET(displayName = "planet_type_"),
    // Inference: Look for a planet with an extremely high density for its size (density > 7.0 g/cm³) that is also very close to its star (orbitalPeriod < 2 days). The high density suggests a massive core, and the proximity suggests its atmosphere was boiled off.
    // density > 7.0 g/cm³ AND orbitAxis < 0.1 AU
    PUFFY_PLANET(displayName = "planet_type_"),
    // Meets Gas Giant criteria AND density < 0.5 g/cm³
    // density < 0.5 g/cm³
    ULTRA_SHORT_PERIOD_PLANET(displayName = "planet_type_"),
    // orbitalPeriod < 1.0 days
    // orbitalPeriod < 1.0
    EYEBALL_PLANET(displayName = "planet_type_"),
    // calculatePlanetTidalLockingScore() < 0.2
    PROTOPLANET(displayName = "planet_type_"),
    // stellarHost.age < 0.01 Gyr
    EARTH_LIKE_PLANET(displayName = "planet_type_"),
    // Qualifies as a Terrestrial Planet AND calculateHabitableZoneScore() > 0.5
    EARTH_ANALOG(displayName = "planet_type_"),
    // calculateHabitability() > 0.9
    SUPER_HABITABLE(displayName = "planet_type_"),
    // calculateHabitability() > 0.9 AND orbits a K-type star (stellarHost.spectralType starts with 'K')
    HABITABLE_CANDIDATE(displayName = "planet_type_"),
    // Inference: if it's in the "Habitable Zone" (equilibriumTemperature between ~250 K and ~350 K), is terrestrial, has a reasonable mass (0.5 - 2.0 Earth masses), and orbits a stable star (not too young or active).
    OCEAN_PLANET(displayName = "planet_type_"),
    // Inference: The density of water is ~1.0 g/cm³. A planet with a density BETWEEN 0.8 AND 1.5 g/cm³ and an equilibriumTemperature BETWEEN 273 K AND 373 K (0°C - 100°C) is a very strong candidate.
    DISRUPTED_PLANET(displayName = "planet_type_"),
    // calculateRocheScore() == 0.0
}
