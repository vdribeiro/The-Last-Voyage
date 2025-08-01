package com.hybris.tlv.usecase.exoplanet.model

internal enum class PlanetType(val displayName: String) {
    ALKALI_METAL_CLOUDS_GAS_GIANT("planet_type_alkali_metal_clouds_gas_giant"),
    AMMONIA_CLOUDS_GAS_GIANT("planet_type_ammonia_clouds_gas_giant"),
    AMMONIA_PLANET("planet_type_ammonia_planet"),
    BARREN_PLANET("planet_type_barren_planet"),
    BROWN_DWARF("planet_type_brown_dwarf"),
    CARBON_PLANET("planet_type_carbon_planet"),
    CHLORINE_PLANET("planet_type_chlorine_planet"),
    CHTHONIAN_PLANET("planet_type_chthonian_planet"),
    // Inference: Look for a planet with an extremely high density for its size (density > 7.0 g/cm³) that is also very close to its star (orbitalPeriod < 2 days). The high density suggests a massive core, and the proximity suggests its atmosphere was boiled off.
    // density > 7.0 g/cm³ AND orbitAxis < 0.1 AU
    CLOUDLESS_GAS_GIANT("planet_type_cloudless_gas_giant"),
    COLD_EYEBALL_PLANET("planet_type_cold_eyeball_planet"),
    CORELESS_PLANET("planet_type_coreless_planet"),
    CRATER_PLANET("planet_type_crater_planet"),
    DESERT_PLANET("planet_type_desert_planet"),
    DISRUPTED_PLANET("planet_type_disrupted_planet"),
    // calculateRocheScore() == 0.0
    DWARF_PLANET("planet_type_dwarf_planet"),
    EARTH_ANALOG_PLANET("planet_type_earth_analog_planet"),
    // calculateHabitability() > 0.9
    EARTH_LIKE_PLANET("planet_type_earth_like_planet"),
    // Qualifies as a Terrestrial Planet AND calculateHabitableZoneScore() > 0.5
    ECUMENOPOLIS_PLANET("planet_type_ecumenopolis_planet"),
    ELLIPSOID_PLANET("planet_type_ellipsoid_planet"),
    EXOPLANET("planet_type_exoplanet"),
    EYEBALL_PLANET("planet_type_eyeball_planet"),
    // calculatePlanetTidalLockingScore() < 0.2
    FLUORINE_PLANET("planet_type_fluorine_planet"),
    FOREST_PLANET("planet_type_forest_planet"),
    GAS_GIANT("planet_type_gas_giant"),
    // density < 2.0 g/cm³ AND radius > 4.0 Earth radii
    // density < 2.5 g/cm³ AND radius > 4.0
    GIANT_PLANET("planet_type_giant_planet"),
    HABITABLE_PLANET("planet_type_habitable_planet"),
    // Inference: if it's in the "Habitable Zone" (equilibriumTemperature between ~250 K and ~350 K), is terrestrial, has a reasonable mass (0.5 - 2.0 Earth masses), and orbits a stable star (not too young or active).
    HELIUM_PLANET("planet_type_helium_planet"),
    HOT_EYEBALL_PLANET("planet_type_hot_eyeball_planet"),
    HOT_JUPITER("planet_type_hot_jupiter"),
    // Meets Gas Giant criteria AND orbitalPeriod < 10 days
    // Qualifies as a Gas Giant AND (orbitalPeriod < 10 days OR orbitAxis < 0.1 AU)
    HOT_NEPTUNE("planet_type_hot_neptune"),
    // mass BETWEEN 10 AND 50 Earth masses AND orbitalPeriod < 10 days
    // Qualifies as an Ice Giant or Mini-Neptune AND (orbitalPeriod < 10 days OR orbitAxis < 0.1 AU)
    HYCEAN_PLANET("planet_type_hycean_planet"),
    ICE_GIANT("planet_type_ice_giant"),
    // Meets Gas Giant criteria AND equilibriumTemperature < 150 K
    // mass is between ~10 and 50 Earth masses AND radius is between ~3 and 6 Earth radii.
    ICE_PLANET("planet_type_ice_planet"),
    // Qualifies as a Terrestrial Planet AND equilibriumTemperature < 200 K
    IRON_PLANET("planet_type_iron_planet"),
    // Meets Terrestrial criteria AND density > 8.0 g/cm³
    // density > 8.0 g/cm³
    JUNGLE_PLANET("planet_type_jungle_planet"),
    LAVA_PLANET("planet_type_lava_planet"),
    // Meets Terrestrial criteria AND equilibriumTemperature > 1500 K
    // Qualifies as a Terrestrial Planet AND equilibriumTemperature > 1200 K
    MEGA_EARTH("planet_type_mega_earth"),
    // Meets Terrestrial criteria AND mass > 10.0 Earth masses
    // mass > 10 AND qualifies as a Super-Earth
    MESOPLANET("planet_type_mesoplanet"),
    METHANE_PLANET("planet_type_methane_planet"),
    MINI_JUPITER("planet_type_mini_jupiter"),
    MINI_NEPTUNE("planet_type_mini_neptune"),
    // radius BETWEEN 2.0 AND 4.0 Earth radii AND density < 2.0 g/cm³
    // radius is between 1.75 and 3.5
    MOUNTAIN_PLANET("planet_type_mountain_planet"),
    OCEAN_PLANET("planet_type_ocean_planet"),
    // Inference: The density of water is ~1.0 g/cm³. A planet with a density BETWEEN 0.8 AND 1.5 g/cm³ and an equilibriumTemperature BETWEEN 273 K AND 373 K (0°C - 100°C) is a very strong candidate.
    PHOSPHORUS_PLANET("planet_type_phosphorus_planet"),
    PLANETESIMAL("planet_type_planetesimal"),
    PROTOPLANET("planet_type_protoplanet"),
    // stellarHost.age < 0.01 Gyr
    PUFFY_PLANET("planet_type_puffy_planet"),
    // Meets Gas Giant criteria AND density < 0.5 g/cm³
    // density < 0.5 g/cm³
    RINGED_PLANET("planet_type_ringed_planet"),
    ROGUE_PLANET("planet_type_rogue_planet"),
    SILICATE_CLOUDS_GAS_GIANT("planet_type_silicate_clouds_gas_giant"),
    SILICATE_PLANET("planet_type_silicate_planet"),
    SUB_BROWN_DWARF("planet_type_sub_brown_dwarf"),
    SUB_EARTH("planet_type_sub_earth"),
    // Meets Terrestrial criteria AND mass < 0.5 Earth masses
    // radius < 1.0 AND mass < 1.0 AND qualifies as a Terrestrial Planet.
    SUB_NEPTUNE("planet_type_sub_neptune"),
    SUBSURFACE_OCEAN_PLANET("planet_type_subsurface_ocean_planet"),
    SULFUR_PLANET("planet_type_sulfur_planet"),
    SUPER_EARTH("planet_type_super_earth"),
    // Meets Terrestrial criteria AND mass BETWEEN 1.0 AND 10.0 Earth masses
    // (radius > 1.0 OR mass > 1.0) AND radius < 1.75 AND qualifies as a Terrestrial Planet.
    SUPER_JUPITER("planet_type_super_jupiter"),
    //mass > 350 Earth masses (Jupiter is ~318)
    SUPER_NEPTUNE("planet_type_super_neptune"),
    SUPER_PUFF_PLANET("planet_type_super_puff_planet"),
    SUPERHABITABLE_PLANET("planet_type_superhabitable_planet"),
    // calculateHabitability() > 0.9 AND orbits a K-type star (stellarHost.spectralType starts with 'K')
    SUPERMASSIVE_TERRESTRIAL_PLANET("planet_type_supermassive_terrestrial_planet"),
    SWAMP_PLANET("planet_type_swamp_planet"),
    TERRESTRIAL_PLANET("planet_type_terrestrial_planet"),
    // density > 3.0 g/cm³ AND radius < 2.5 Earth radii
    // density > 3.0 g/cm³ or calculatePlanetTelluricityScore() > 0.5.
    TOROIDAL_PLANET("planet_type_toroidal_planet"),
    ULTRA_COOL_DWARF("planet_type_ultra_cool_dwarf"),
    ULTRA_HOT_JUPITER("planet_type_ultra_hot_jupiter"),
    // Meets Hot Jupiter criteria AND equilibriumTemperature > 2000 K
    // Qualifies as a Hot Jupiter AND equilibriumTemperature > 2000 K
    ULTRA_HOT_NEPTUNE("planet_type_ultra_hot_neptune"),
    ULTRA_SHORT_PERIOD_PLANET("planet_type_ultra_short_period_planet"),
    // orbitalPeriod < 1.0 days
    // orbitalPeriod < 1.0
    WATER_CLOUDS_GAS_GIANT("planet_type_water_clouds_gas_giant")
}
