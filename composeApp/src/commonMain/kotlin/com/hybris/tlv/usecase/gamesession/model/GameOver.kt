package com.hybris.tlv.usecase.gamesession.model

internal enum class GameOver(val displayName: String) {
    // Ship is destroyed
    INTEGRITY_ZERO(displayName = "game_over_screen__integrity_zero"),
    INTEGRITY_ZERO_YEARS_FEW(displayName = "game_over_screen__integrity_zero_years_few"),
    INTEGRITY_ZERO_YEARS_SOME(displayName = "game_over_screen__integrity_zero_years_some"),
    INTEGRITY_ZERO_YEARS_LOTS(displayName = "game_over_screen__integrity_zero_years_lots"),
    INTEGRITY_ZERO_CRYOPODS_ZERO(displayName = "game_over_screen__integrity_zero_cryopods_zero"),
    INTEGRITY_ZERO_CRYOPODS_ONE(displayName = "game_over_screen__integrity_zero_cryopods_one"),
    INTEGRITY_ZERO_CRYOPODS_LOW(displayName = "game_over_screen__integrity_zero_cryopods_low"),
    INTEGRITY_ZERO_CRYOPODS_ENOUGH(displayName = "game_over_screen__integrity_zero_cryopods_enough"),
    INTEGRITY_ZERO_FUEL_LOW(displayName = "game_over_screen__integrity_zero_fuel_low"),
    INTEGRITY_ZERO_FUEL_SOME(displayName = "game_over_screen__integrity_zero_fuel_some"),
    INTEGRITY_ZERO_FUEL_PLENTY(displayName = "game_over_screen__integrity_zero_fuel_plenty"),
    INTEGRITY_ZERO_YEARS_LOTS_CRYOPODS_BUSTLING(displayName = "game_over_screen__integrity_zero_years_lots_cryopods_bustling"),

    // Ship ran out of fuel
    FUEL_ZERO(displayName = "game_over_screen__fuel_zero"),
    FUEL_ZERO_YEARS_FEW(displayName = "game_over_screen__fuel_zero_years_few"),
    FUEL_ZERO_YEARS_SOME(displayName = "game_over_screen__fuel_zero_years_some"),
    FUEL_ZERO_YEARS_LOTS(displayName = "game_over_screen__fuel_zero_years_lots"),
    FUEL_ZERO_MATERIALS_ZERO(displayName = "game_over_screen__fuel_zero_materials_zero"),
    FUEL_ZERO_MATERIALS_LOW(displayName = "game_over_screen__fuel_zero_materials_low"),
    FUEL_ZERO_MATERIALS_ENOUGH(displayName = "game_over_screen__fuel_zero_materials_enough"),
    FUEL_ZERO_CRYOPODS_ZERO(displayName = "game_over_screen__fuel_zero_cryopods_zero"),
    FUEL_ZERO_CRYOPODS_ONE(displayName = "game_over_screen__fuel_zero_cryopods_one"),
    FUEL_ZERO_CRYOPODS_LOW(displayName = "game_over_screen__fuel_zero_cryopods_low"),
    FUEL_ZERO_CRYOPODS_ENOUGH(displayName = "game_over_screen__fuel_zero_cryopods_enough"),
    FUEL_ZERO_INTEGRITY_LOW(displayName = "game_over_screen__fuel_zero_integrity_low"),
    FUEL_ZERO_INTEGRITY_ENOUGH(displayName = "game_over_screen__fuel_zero_integrity_enough"),
    FUEL_ZERO_INTEGRITY_PRISTINE(displayName = "game_over_screen__fuel_zero_integrity_pristine"),
    FUEL_ZERO_MATERIALS_PLENTY_CRYOPODS_BUSTLING(displayName = "game_over_screen__fuel_zero_materials_plenty_cryopods_bustling"),
    FUEL_ZERO_INTEGRITY_ENOUGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING(displayName = "game_over_screen__fuel_zero_integrity_enough_materials_enough_cryopods_bustling"),

    // Solar System Planets
    MERCURY(displayName = "game_over_screen__mercury"),
    VENUS(displayName = "game_over_screen__venus"),
    EARTH(displayName = "game_over_screen__earth"),
    MARS(displayName = "game_over_screen__mars"),
    JUPITER(displayName = "game_over_screen__jupiter"),
    SATURN(displayName = "game_over_screen__saturn"),
    URANUS(displayName = "game_over_screen__uranus"),
    NEPTUNE(displayName = "game_over_screen__neptune"),

    // Habitability: Deadly
    HABITABILITY_DEADLY(displayName = "game_over_screen__habitability_deadly"),
    HABITABILITY_DEADLY_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_deadly_cryopods_enough"),
    HABITABILITY_DEADLY_INTEGRITY_LOW(displayName = "game_over_screen__habitability_deadly_integrity_low"),
    HABITABILITY_DEADLY_INTEGRITY_MID_LOW_MATERIALS_ENOUGH(displayName = "game_over_screen__habitability_deadly_integrity_mid_low_materials_enough"),

    // Habitability: Very Low
    HABITABILITY_VERY_LOW(displayName = "game_over_screen__habitability_very_low"),
    HABITABILITY_VERY_LOW_CRYOPODS_ENOUGH_MATERIALS_ENOUGH(displayName = "game_over_screen__habitability_very_low_cryopods_enough_materials_enough"),
    HABITABILITY_VERY_LOW_CRYOPODS_MID_MATERIALS_ENOUGH(displayName = "game_over_screen__habitability_very_low_cryopods_mid_materials_enough"),
    HABITABILITY_VERY_LOW_INTEGRITY_LOW(displayName = "game_over_screen__habitability_very_low_integrity_low"),

    // Habitability: Low
    HABITABILITY_LOW(displayName = "game_over_screen__habitability_low"),
    HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_low_materials_enough_cryopods_enough"),
    HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_INTEGRITY_PRISTINE(displayName = "game_over_screen__habitability_low_materials_enough_cryopods_enough_integrity_pristine"),
    HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_FUEL_PLENTY(displayName = "game_over_screen__habitability_low_materials_enough_cryopods_enough_fuel_plenty"),
    HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_LOW(displayName = "game_over_screen__habitability_low_materials_enough_cryopods_low"),
    HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_low_materials_enough_cryopods_zero"),
    HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_low_materials_low_cryopods_enough"),
    HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_LOW(displayName = "game_over_screen__habitability_low_materials_low_cryopods_low"),
    HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_low_materials_low_cryopods_zero"),

    // Habitability: Medium
    HABITABILITY_MEDIUM(displayName = "game_over_screen__habitability_medium"),
    HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_medium_materials_enough_cryopods_enough"),
    HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS(displayName = "game_over_screen__habitability_medium_materials_enough_cryopods_enough_years_lots"),
    HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_BUSTLING(displayName = "game_over_screen__habitability_medium_materials_enough_cryopods_bustling"),
    HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_LOW(displayName = "game_over_screen__habitability_medium_materials_enough_cryopods_low"),
    HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_medium_materials_enough_cryopods_zero"),
    HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH(displayName = "game_over_screen__habitability_medium_materials_low_cryopods_enough_integrity_enough"),
    HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_medium_materials_low_cryopods_enough"),
    HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_LOW(displayName = "game_over_screen__habitability_medium_materials_low_cryopods_low"),
    HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_medium_materials_low_cryopods_zero"),

    // Habitability: High
    HABITABILITY_HIGH(displayName = "game_over_screen__habitability_high"),
    HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_high_materials_enough_cryopods_enough"),
    HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS(displayName = "game_over_screen__habitability_high_materials_enough_cryopods_enough_years_lots"),
    HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING(displayName = "game_over_screen__habitability_high_materials_enough_cryopods_bustling"),
    HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_LOW(displayName = "game_over_screen__habitability_high_materials_enough_cryopods_low"),
    HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_high_materials_enough_cryopods_zero"),
    HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH(displayName = "game_over_screen__habitability_high_materials_low_cryopods_enough_integrity_enough"),
    HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_high_materials_low_cryopods_enough"),
    HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_LOW(displayName = "game_over_screen__habitability_high_materials_low_cryopods_low"),
    HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_high_materials_low_cryopods_zero"),

    // Default
    GAME_OVER(displayName = "game_over_screen__default_game_over");
}
