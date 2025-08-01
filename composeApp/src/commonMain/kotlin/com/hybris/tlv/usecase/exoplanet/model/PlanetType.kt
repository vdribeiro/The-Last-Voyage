package com.hybris.tlv.usecase.exoplanet.model

enum class PlanetType(val displayName: String) {
    // General Types
    EXOPLANET("planet_type_exoplanet"),
    GIANT_PLANET("planet_type_giant_planet"),
    TERRESTRIAL_PLANET("planet_type_terrestrial_planet"),

    // Size/Mass Based
    SUB_EARTH("planet_type_sub_earth"),
    SUPER_EARTH("planet_type_super_earth"),
    MEGA_EARTH("planet_type_mega_earth"),
    SUPERMASSIVE_TERRESTRIAL_PLANET("planet_type_supermassive_terrestrial_planet"),
    MINI_NEPTUNE("planet_type_mini_neptune"),
    SUB_NEPTUNE("planet_type_sub_neptune"),
    SUPER_NEPTUNE("planet_type_super_neptune"),
    ICE_GIANT("planet_type_ice_giant"),
    GAS_GIANT("planet_type_gas_giant"),
    MINI_JUPITER("planet_type_mini_jupiter"),
    SUPER_JUPITER("planet_type_super_jupiter"),
    DWARF_PLANET("planet_type_dwarf_planet"),

    // Composition Based
    SILICATE_PLANET("planet_type_silicate_planet"),
    IRON_PLANET("planet_type_iron_planet"),
    CARBON_PLANET("planet_type_carbon_planet"),
    CORELESS_PLANET("planet_type_coreless_planet"),
    PUFFY_PLANET("planet_type_puffy_planet"),
    SUPER_PUFF_PLANET("planet_type_super_puff_planet"),
    OCEAN_PLANET("planet_type_ocean_planet"),
    SUBSURFACE_OCEAN_PLANET("planet_type_subsurface_ocean_planet"),
    HELIUM_PLANET("planet_type_helium_planet"),
    HYCEAN_PLANET("planet_type_hycean_planet"),
    AMMONIA_PLANET("planet_type_ammonia_planet"),
    METHANE_PLANET("planet_type_methane_planet"),
    CHLORINE_PLANET("planet_type_chlorine_planet"),
    SULFUR_PLANET("planet_type_sulfur_planet"),
    PHOSPHORUS_PLANET("planet_type_phosphorus_planet"),
    FLUORINE_PLANET("planet_type_fluorine_planet"),

    // Temperature/Orbit Based
    LAVA_PLANET("planet_type_lava_planet"),
    DESERT_PLANET("planet_type_desert_planet"),
    ICE_PLANET("planet_type_ice_planet"),
    HOT_JUPITER("planet_type_hot_jupiter"),
    ULTRA_HOT_JUPITER("planet_type_ultra_hot_jupiter"),
    HOT_NEPTUNE("planet_type_hot_neptune"),
    ULTRA_HOT_NEPTUNE("planet_type_ultra_hot_neptune"),
    ULTRA_SHORT_PERIOD_PLANET("planet_type_ultra_short_period_planet"),
    EYEBALL_PLANET("planet_type_eyeball_planet"),
    HOT_EYEBALL_PLANET("planet_type_hot_eyeball_planet"),
    COLD_EYEBALL_PLANET("planet_type_cold_eyeball_planet"),

    // Gas Giant Atmosphere Types (Sudarsky Classification)
    AMMONIA_CLOUDS_GAS_GIANT("planet_type_ammonia_clouds_gas_giant"),
    WATER_CLOUDS_GAS_GIANT("planet_type_water_clouds_gas_giant"),
    CLOUDLESS_GAS_GIANT("planet_type_cloudless_gas_giant"),
    ALKALI_METAL_CLOUDS_GAS_GIANT("planet_type_alkali_metal_clouds_gas_giant"),
    SILICATE_CLOUDS_GAS_GIANT("planet_type_silicate_clouds_gas_giant"),

    // Habitability Based
    BARREN_PLANET("planet_type_barren_planet"),
    HABITABLE_PLANET("planet_type_habitable_planet"),
    EARTH_LIKE_PLANET("planet_type_earth_like_planet"),
    EARTH_ANALOG_PLANET("planet_type_earth_analog_planet"),
    SUPERHABITABLE_PLANET("planet_type_superhabitable_planet"),

    // Formation/State Based
    PROTOPLANET("planet_type_protoplanet"),
    PLANETESIMAL("planet_type_planetesimal"),
    DISRUPTED_PLANET("planet_type_disrupted_planet"),
    CHTHONIAN_PLANET("planet_type_chthonian_planet"),
    ROGUE_PLANET("planet_type_rogue_planet"),

    // Hypothetical
    CRATER_PLANET("planet_type_crater_planet"),
    RINGED_PLANET("planet_type_ringed_planet"),
    FOREST_PLANET("planet_type_forest_planet"),
    JUNGLE_PLANET("planet_type_jungle_planet"),
    SWAMP_PLANET("planet_type_swamp_planet"),
    MOUNTAIN_PLANET("planet_type_mountain_planet"),
    ELLIPSOID_PLANET("planet_type_ellipsoid_planet"),
    TOROIDAL_PLANET("planet_type_toroidal_planet"),
    ECUMENOPOLIS_PLANET("planet_type_ecumenopolis_planet"),

    // Stellar/Sub-Stellar Objects (Not true planets)
    BROWN_DWARF("planet_type_brown_dwarf"),
    SUB_BROWN_DWARF("planet_type_sub_brown_dwarf"),
    ULTRA_COOL_DWARF("planet_type_ultra_cool_dwarf")
}