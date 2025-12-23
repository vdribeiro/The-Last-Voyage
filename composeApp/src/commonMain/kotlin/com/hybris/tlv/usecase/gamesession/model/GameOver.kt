package com.hybris.tlv.usecase.gamesession.model

internal enum class GameOver(val displayName: String, val multiplier: Double) {
    // Ship is destroyed
    INTEGRITY_ZERO(displayName = "game_over_screen__integrity_zero", multiplier = 0.25),
    INTEGRITY_ZERO_YEARS_FEW(displayName = "game_over_screen__integrity_zero_years_few", multiplier = 0.25),
    INTEGRITY_ZERO_YEARS_SOME(displayName = "game_over_screen__integrity_zero_years_some", multiplier = 0.25),
    INTEGRITY_ZERO_YEARS_LOTS(displayName = "game_over_screen__integrity_zero_years_lots", multiplier = 0.25),
    INTEGRITY_ZERO_CRYOPODS_ZERO(displayName = "game_over_screen__integrity_zero_cryopods_zero", multiplier = 0.25),
    INTEGRITY_ZERO_CRYOPODS_ONE(displayName = "game_over_screen__integrity_zero_cryopods_one", multiplier = 0.25),
    INTEGRITY_ZERO_CRYOPODS_LOW(displayName = "game_over_screen__integrity_zero_cryopods_low", multiplier = 0.25),
    INTEGRITY_ZERO_CRYOPODS_ENOUGH(displayName = "game_over_screen__integrity_zero_cryopods_enough", multiplier = 0.25),
    INTEGRITY_ZERO_FUEL_LOW(displayName = "game_over_screen__integrity_zero_fuel_low", multiplier = 0.25),
    INTEGRITY_ZERO_FUEL_SOME(displayName = "game_over_screen__integrity_zero_fuel_some", multiplier = 0.25),
    INTEGRITY_ZERO_FUEL_PLENTY(displayName = "game_over_screen__integrity_zero_fuel_plenty", multiplier = 0.25),
    INTEGRITY_ZERO_YEARS_LOTS_CRYOPODS_BUSTLING(displayName = "game_over_screen__integrity_zero_years_lots_cryopods_bustling", multiplier = 0.25),

    // Ship ran out of fuel
    FUEL_ZERO(displayName = "game_over_screen__fuel_zero", multiplier = 0.25),
    FUEL_ZERO_YEARS_FEW(displayName = "game_over_screen__fuel_zero_years_few", multiplier = 0.25),
    FUEL_ZERO_YEARS_SOME(displayName = "game_over_screen__fuel_zero_years_some", multiplier = 0.25),
    FUEL_ZERO_YEARS_LOTS(displayName = "game_over_screen__fuel_zero_years_lots", multiplier = 0.25),
    FUEL_ZERO_MATERIALS_ZERO(displayName = "game_over_screen__fuel_zero_materials_zero", multiplier = 0.25),
    FUEL_ZERO_MATERIALS_LOW(displayName = "game_over_screen__fuel_zero_materials_low", multiplier = 0.25),
    FUEL_ZERO_MATERIALS_ENOUGH(displayName = "game_over_screen__fuel_zero_materials_enough", multiplier = 0.25),
    FUEL_ZERO_CRYOPODS_ZERO(displayName = "game_over_screen__fuel_zero_cryopods_zero", multiplier = 0.25),
    FUEL_ZERO_CRYOPODS_ONE(displayName = "game_over_screen__fuel_zero_cryopods_one", multiplier = 0.25),
    FUEL_ZERO_CRYOPODS_LOW(displayName = "game_over_screen__fuel_zero_cryopods_low", multiplier = 0.25),
    FUEL_ZERO_CRYOPODS_ENOUGH(displayName = "game_over_screen__fuel_zero_cryopods_enough", multiplier = 0.25),
    FUEL_ZERO_INTEGRITY_LOW(displayName = "game_over_screen__fuel_zero_integrity_low", multiplier = 0.25),
    FUEL_ZERO_INTEGRITY_ENOUGH(displayName = "game_over_screen__fuel_zero_integrity_enough", multiplier = 0.25),
    FUEL_ZERO_INTEGRITY_PRISTINE(displayName = "game_over_screen__fuel_zero_integrity_pristine", multiplier = 0.25),
    FUEL_ZERO_MATERIALS_PLENTY_CRYOPODS_BUSTLING(displayName = "game_over_screen__fuel_zero_materials_plenty_cryopods_bustling", multiplier = 0.25),
    FUEL_ZERO_INTEGRITY_ENOUGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING(displayName = "game_over_screen__fuel_zero_integrity_enough_materials_enough_cryopods_bustling", multiplier = 0.25),

    // Solar System Planets
    MERCURY(displayName = "game_over_screen__mercury", multiplier = 0.25),
    VENUS(displayName = "game_over_screen__venus", multiplier = 0.25),
    EARTH(displayName = "game_over_screen__earth", multiplier = 0.25),
    MARS(displayName = "game_over_screen__mars", multiplier = 0.25),
    JUPITER(displayName = "game_over_screen__jupiter", multiplier = 0.25),
    SATURN(displayName = "game_over_screen__saturn", multiplier = 0.25),
    URANUS(displayName = "game_over_screen__uranus", multiplier = 0.25),
    NEPTUNE(displayName = "game_over_screen__neptune", multiplier = 0.25),

    // Habitability: Deadly
    HABITABILITY_DEADLY(displayName = "game_over_screen__habitability_deadly", multiplier = 0.25),
    HABITABILITY_DEADLY_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_deadly_cryopods_enough", multiplier = 0.25),
    HABITABILITY_DEADLY_INTEGRITY_LOW(displayName = "game_over_screen__habitability_deadly_integrity_low", multiplier = 0.25),
    HABITABILITY_DEADLY_INTEGRITY_MID_LOW_MATERIALS_ENOUGH(displayName = "game_over_screen__habitability_deadly_integrity_mid_low_materials_enough", multiplier = 0.25),

    // Habitability: Very Low
    HABITABILITY_VERY_LOW(displayName = "game_over_screen__habitability_very_low", multiplier = 0.5),
    HABITABILITY_VERY_LOW_CRYOPODS_ENOUGH_MATERIALS_ENOUGH(displayName = "game_over_screen__habitability_very_low_cryopods_enough_materials_enough", multiplier = 0.5),
    HABITABILITY_VERY_LOW_CRYOPODS_MID_MATERIALS_ENOUGH(displayName = "game_over_screen__habitability_very_low_cryopods_mid_materials_enough", multiplier = 0.5),
    HABITABILITY_VERY_LOW_INTEGRITY_LOW(displayName = "game_over_screen__habitability_very_low_integrity_low", multiplier = 0.5),

    // Habitability: Low
    HABITABILITY_LOW(displayName = "game_over_screen__habitability_low", multiplier = 0.75),
    HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_low_materials_enough_cryopods_enough", multiplier = 1.0),
    HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_INTEGRITY_PRISTINE(displayName = "game_over_screen__habitability_low_materials_enough_cryopods_enough_integrity_pristine", multiplier = 1.0),
    HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_FUEL_PLENTY(displayName = "game_over_screen__habitability_low_materials_enough_cryopods_enough_fuel_plenty", multiplier = 1.0),
    HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_LOW(displayName = "game_over_screen__habitability_low_materials_enough_cryopods_low", multiplier = 0.75),
    HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_low_materials_enough_cryopods_zero", multiplier = 0.75),
    HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_low_materials_low_cryopods_enough", multiplier = 0.75),
    HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_LOW(displayName = "game_over_screen__habitability_low_materials_low_cryopods_low", multiplier = 0.75),
    HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_low_materials_low_cryopods_zero", multiplier = 0.75),

    // Habitability: Medium
    HABITABILITY_MEDIUM(displayName = "game_over_screen__habitability_medium", multiplier = 1.25),
    HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_medium_materials_enough_cryopods_enough", multiplier = 2.0),
    HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS(displayName = "game_over_screen__habitability_medium_materials_enough_cryopods_enough_years_lots", multiplier = 2.0),
    HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_BUSTLING(displayName = "game_over_screen__habitability_medium_materials_enough_cryopods_bustling", multiplier = 2.0),
    HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_LOW(displayName = "game_over_screen__habitability_medium_materials_enough_cryopods_low", multiplier = 1.25),
    HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_medium_materials_enough_cryopods_zero", multiplier = 1.25),
    HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH(displayName = "game_over_screen__habitability_medium_materials_low_cryopods_enough_integrity_enough", multiplier = 1.75),
    HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_medium_materials_low_cryopods_enough", multiplier = 1.5),
    HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_LOW(displayName = "game_over_screen__habitability_medium_materials_low_cryopods_low", multiplier = 1.25),
    HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_medium_materials_low_cryopods_zero", multiplier = 1.25),

    // Habitability: High
    HABITABILITY_HIGH(displayName = "game_over_screen__habitability_high", multiplier = 2.25),
    HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_high_materials_enough_cryopods_enough", multiplier = 3.0),
    HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS(displayName = "game_over_screen__habitability_high_materials_enough_cryopods_enough_years_lots", multiplier = 3.0),
    HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING(displayName = "game_over_screen__habitability_high_materials_enough_cryopods_bustling", multiplier = 3.0),
    HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_LOW(displayName = "game_over_screen__habitability_high_materials_enough_cryopods_low", multiplier = 2.25),
    HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_high_materials_enough_cryopods_zero", multiplier = 2.25),
    HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH(displayName = "game_over_screen__habitability_high_materials_low_cryopods_enough_integrity_enough", multiplier = 2.75),
    HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH(displayName = "game_over_screen__habitability_high_materials_low_cryopods_enough", multiplier = 2.5),
    HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_LOW(displayName = "game_over_screen__habitability_high_materials_low_cryopods_low", multiplier = 2.25),
    HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ZERO(displayName = "game_over_screen__habitability_high_materials_low_cryopods_zero", multiplier = 2.25),

    // Default
    GAME_OVER(displayName = "game_over_screen__default_game_over", multiplier = 0.25);
}
