package com.hybris.tlv.usecase.exoplanet.model

enum class PlanetType(val displayName: String) {
    // Size/Mass Based
    SUB_EARTH(displayName = "planet_type_sub_earth"),
    SUPER_EARTH(displayName = "planet_type_super_earth"),
    MEGA_EARTH(displayName = "planet_type_mega_earth"),
    MINI_NEPTUNE(displayName = "planet_type_mini_neptune"),
    SUPER_NEPTUNE(displayName = "planet_type_super_neptune"),
    ICE_GIANT(displayName = "planet_type_ice_giant"),
    GAS_GIANT(displayName = "planet_type_gas_giant"),
    SUPER_JUPITER(displayName = "planet_type_super_jupiter"),

    // Composition Based
    TERRESTRIAL_PLANET(displayName = "planet_type_terrestrial_planet"),
    IRON_PLANET(displayName = "planet_type_iron_planet"),
    PUFFY_PLANET(displayName = "planet_type_puffy_planet"),
    SUPER_PUFF_PLANET(displayName = "planet_type_super_puff_planet"),
    OCEAN_PLANET(displayName = "planet_type_ocean_planet"),
    SUBSURFACE_OCEAN_PLANET(displayName = "planet_type_subsurface_ocean_planet"),

    // Temperature/Orbit Based
    LAVA_PLANET(displayName = "planet_type_lava_planet"),
    DESERT_PLANET(displayName = "planet_type_desert_planet"),
    ICE_PLANET(displayName = "planet_type_ice_planet"),
    HOT_JUPITER(displayName = "planet_type_hot_jupiter"),
    ULTRA_HOT_JUPITER(displayName = "planet_type_ultra_hot_jupiter"),
    HOT_NEPTUNE(displayName = "planet_type_hot_neptune"),
    ULTRA_HOT_NEPTUNE(displayName = "planet_type_ultra_hot_neptune"),
    ULTRA_SHORT_PERIOD_PLANET(displayName = "planet_type_ultra_short_period_planet"),
    EYEBALL_PLANET(displayName = "planet_type_eyeball_planet"),
    HOT_EYEBALL_PLANET(displayName = "planet_type_hot_eyeball_planet"),
    COLD_EYEBALL_PLANET(displayName = "planet_type_cold_eyeball_planet"),

    // Gas Giant Atmosphere Types (Sudarsky Classification)
    AMMONIA_CLOUDS_GAS_GIANT(displayName = "planet_type_ammonia_clouds_gas_giant"),
    WATER_CLOUDS_GAS_GIANT(displayName = "planet_type_water_clouds_gas_giant"),
    CLOUDLESS_GAS_GIANT(displayName = "planet_type_cloudless_gas_giant"),
    ALKALI_METAL_CLOUDS_GAS_GIANT(displayName = "planet_type_alkali_metal_clouds_gas_giant"),
    SILICATE_CLOUDS_GAS_GIANT(displayName = "planet_type_silicate_clouds_gas_giant"),

    // Habitability Based
    BARREN_PLANET(displayName = "planet_type_barren_planet"),
    EARTH_LIKE_PLANET(displayName = "planet_type_earth_like_planet"),
    EARTH_ANALOG_PLANET(displayName = "planet_type_earth_analog_planet"),
    SUPERHABITABLE_PLANET(displayName = "planet_type_superhabitable_planet"),

    // Formation/State Based
    PROTOPLANET(displayName = "planet_type_protoplanet"),
    DISRUPTED_PLANET(displayName = "planet_type_disrupted_planet"),
    CHTHONIAN_PLANET(displayName = "planet_type_chthonian_planet"),

    // Hypothetical
    CRATER_PLANET(displayName = "planet_type_crater_planet"),
    ELLIPSOID_PLANET(displayName = "planet_type_ellipsoid_planet"),
}
