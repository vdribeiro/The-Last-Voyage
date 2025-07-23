package com.hybris.tlv.usecase.translation

import com.hybris.tlv.usecase.translation.model.domain.Translation

private val errorScreenTranslations = listOf(
    Translation(
        languageIso = "en",
        key = "error_screen__title",
        value = "Oops! Something went wrong."
    ),
    Translation(
        languageIso = "en",
        key = "error_screen__description",
        value = "It seems we've run into an uncharted anomaly. Our engineering crew is already on it, " +
                "but a description from the you, the captain, would be invaluable."
    ),
    Translation(
        languageIso = "en",
        key = "error_screen__button",
        value = "Submit Feedback"
    ),
)

private val splashScreenTranslations = listOf(
    Translation(
        languageIso = "en",
        key = "splash_screen__loading",
        value = "Loading..."
    ),
)

private val mainMenuScreenTranslations = listOf(
    Translation(
        languageIso = "en",
        key = "main_menu_screen__new_game",
        value = "New Game"
    ),
    Translation(
        languageIso = "en",
        key = "main_menu_screen__continue",
        value = "Continue"
    ),
    Translation(
        languageIso = "en",
        key = "main_menu_screen__scores",
        value = "Scores"
    ),
    Translation(
        languageIso = "en",
        key = "main_menu_screen__achievements",
        value = "Achievements"
    ),
    Translation(
        languageIso = "en",
        key = "main_menu_screen__explore",
        value = "Explore"
    ),
    Translation(
        languageIso = "en",
        key = "main_menu_screen__credits",
        value = "Credits"
    )
)

private val newGameScreenTranslations = listOf(
    Translation(
        languageIso = "en",
        key = "new_game_screen__ship_points",
        value = "Points Available"
    ),
    Translation(
        languageIso = "en",
        key = "new_game_screen__difficulty_easy",
        value = "Easy"
    ),
    Translation(
        languageIso = "en",
        key = "new_game_screen__difficulty_normal",
        value = "Normal"
    ),
    Translation(
        languageIso = "en",
        key = "new_game_screen__difficulty_hard",
        value = "Hard"
    ),
    Translation(
        languageIso = "en",
        key = "new_game_screen__difficulty_custom",
        value = "Custom"
    ),
    Translation(
        languageIso = "en",
        key = "new_game_screen__continue",
        value = "Continue"
    ),
    Translation(
        languageIso = "en",
        key = "new_game_screen__advanced",
        value = "Advanced"
    ),
    Translation(
        languageIso = "en",
        key = "new_game_screen__start",
        value = "Start"
    ),
)

private val gameScreenTranslations = listOf(
    Translation(
        languageIso = "en",
        key = "game_screen__travel",
        value = "Travel"
    ),
    Translation(
        languageIso = "en",
        key = "game_screen__system",
        value = "System"
    ),
    Translation(
        languageIso = "en",
        key = "game_screen__ship",
        value = "Ship"
    ),
    Translation(
        languageIso = "en",
        key = "game_screen__ship",
        value = "Ship"
    ),
)

private val scoreScreenTranslations = listOf(
    Translation(
        languageIso = "en",
        key = "score_screen__title",
        value = "Scores"
    ),
)

private val exploreScreenTranslations = listOf(
    Translation(
        languageIso = "en",
        key = "explore_screen__stellar_explorer",
        value = "Stellar Explorer"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics",
        value = "Game Mechanics"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics_goal_title",
        value = "The Goal"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics_goal_description",
        value = "After a random extinction level catastrophe, your mission is to find a new home for humanity. " +
                "You take the role of the ship's computer and travel from star system to star system in search of a habitable planet. " +
                "The journey is long and perilous, and your resources are finite."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics_attributes_title",
        value = "Ship Attributes"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics_attributes_description",
        value = "At the start of your journey, you must allocate points to your ship's key systems:\n\n" +
                "• Sensor Range: Determines how many star systems you can choose from for your next destination.\n\n" +
                "• Fuel: Your starting fuel. Each travel consumes fuel equal to the distance traveled.\n\n" +
                "• Materials: Can be used to repair the ship in some events but most of all are essential for colonizing a new world.\n\n" +
                "• Cryopods: The number of colonists you carry. You must have enough to meet a planet's settlement requirements."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics_travel_title",
        value = "Travel"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics_travel_description",
        value = "Each travel between systems costs fuel and takes time. The ship travels at 0.1c, so a 10 light-year journey will add 100 years " +
                "of total travelled time. Longer journeys can be rewarding but are also more dangerous.\n" +
                "With each new destination, you will also encounter a new random event that can help or hinder your progress, " +
                "consuming or rewarding resources."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics_game_over_title",
        value = "Game Over"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics_game_over_description",
        value = "The game ends when your ship's integrity or fuel reaches 0, or you settle in a new world. " +
                "Settlement success depends on the planet's habitability and whether you have enough materials and cryopods.\n\n" +
                "• Uninhabitable Worlds (below 40%): Instant failure.\n\n" +
                "• Harsh Worlds (41-60%): Require significant resources (300 materials + 150 cryopods).\n\n" +
                "• Tolerable Worlds (61-80%): Require moderate resources (100 materials + 100 cryopods).\n\n" +
                "• Paradise Worlds (81-100%): Are the easiest to colonize (50 materials + 50 cryopods).\n\n" +
                "There are special cases where you can defy the odds with insufficient materials but enough population, if you have plenty of " +
                "fuel to use the engine as a makeshift energy source, or ship integrity to cannibalize the ship for materials."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics_score_title",
        value = "Score"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__mechanics_score_description",
        value = "Your final score is calculated based on your success, the quality of the planet, ship stats, the length of your journey " +
                "and difficulty based on initial points spent.\n\n" +
                "• Score = Base Score * Habitability Multiplier * Success Multiplier * Challenge Multiplier\n\n" +
                "• Base Score = (cryopods * 100) + (materials * 2) + (fuel * 1) + (yearsTraveled * 5)\n\n" +
                "• Habitability Multiplier = 0.25 for Deadly, 0.50 for Very Low, 1.0 for Low, 1.2 for Medium, 1.5 for High\n\n" +
                "• Success Multiplier = 0.25 if the ship disintegrates, 0.50 if you survive in a primitive state, " +
                "0.75 if you survive by defying the odds, 1.0 if you successfully settle in a new world\n\n" +
                "• Challenge Multiplier = 1.0 + (15 - Assigned Points) + 0.05"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability",
        value = "Habitability Formula"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_title",
        value = "Summary"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_description",
        value = "The assessment of a planet's potential to host life is a complex field, and as a result, several different models and indices are " +
                "usually used to focus on different aspects of habitability. A truly universal formula is elusive because we only have one example " +
                "of a planet that can sustain life: Earth.\nIn this game, the habitability score used is a weighted average of dozens of planetary " +
                "and stellar characteristics based on real data and real models.\nThe individual scores range from 0.0 to 1.0. Keep in mind that " +
                "data availability is not constant which is another pain to handle. This approach tries to derive missing data when possible, " +
                "otherwise it handles null scores gracefully by simply omitting them from the final average.\n" +
                "So, to try and calculate a fair score, I used the following hypothesis."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_hz_title",
        value = "Is the planet in the right place for liquid water?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_hz_description",
        value = "The Circumstellar Habitable Zone (CHZ) is the the region around a star where liquid water could exist on a planet's surface.\n" +
                "I used the Kopparapu model with a flat plateau of 1.0 across the entire conservative zone and then a smooth down slope through " +
                "the optimistic zone, as a simple gradient peaked at the center unfairly penalizes planets like Earth, which is perfectly " +
                "habitable but located near the inner edge of the Sun's conservative zone.\nThe host star's temperature is used to calculate the " +
                "fluxes with the model's coefficients. If it is not available, the Kasting simple luminosity model is used instead."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_density_radius_title",
        value = "Is the planet the right size and density to be rocky?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_density_radius_description",
        value = "A small planet is unlikely to have enough mass to sustain the geological activity and atmospheric pressure needed for surface " +
                "liquid water, whilst a large planet is very likely to be a mini-Neptune, possessing a thick, crushing gas envelope that makes " +
                "them uninhabitable on the surface.\nThe radius must be within 0.5 to 1.5 Earth radii to be considered rocky.\n" +
                "On the other hand, rocky planets are typically above 3g/cm^3 (Earth's density is ~5.51 g/cm^3), " +
                "so the telluricity is also factored in."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_mass_title",
        value = "Can the planet hold an atmosphere and drive geology?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_mass_description",
        value = "Mass is needed for gravity to hold onto a substantial atmosphere over billions of years, protecting it from being stripped away " +
                "by stellar winds, and to retain enough internal heat to power long-term geological activity like plate tectonics, which is vital " +
                "for cycling chemicals and nutrients. Mars is a classic example of a body that lost most of its early atmosphere.\n" +
                "Conversely, a planet with a very strong gravity will likely hold onto a very thick hydrogen and helium atmosphere from its " +
                "formation, turning it into a gas-dominated mini-Neptune with no solid surface.\nThe mass must be within 0.1 to 5.0 Earth masses " +
                "to be a viable candidate."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_eccentricity_title",
        value = "Does the planet have a stable, circular orbit for stable temperatures?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_eccentricity_description",
        value = "The planet's eccentricity with a score of 1.0 is a perfect circular orbit. The score decreases as the orbit becomes more " +
                "elliptical. High eccentricity can lead to extreme temperature variations, making a planet less habitable."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_temperature_title",
        value = "Does the planet have a reasonable baseline temperature?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_temperature_description",
        value = "The planet's temperature must fall within a range that can support liquid water with a plausible atmosphere. " +
                "Two ranges are used: the ideal from 230 to 280 Kelvin without penalization, and the optimistic from 180 to 330 Kelvin with score" +
                "penalization.\nWhen the planet's equilibrium temperature is unavailable, the temperature is calculated by the measured day-side " +
                "temperature in Kelvin from occultation depth (the depth of the secondary eclipse) assuming the occultation was measured in the " +
                "infrared where the planet's thermal emission is dominant."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_obliquity_title",
        value = "Does the planet have stable seasons?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_obliquity_description",
        value = "I use the planet's axial tilt (obliquity) to derive seasonal stability. " +
                "A moderate tilt like Earth's is considered ideal for stable seasons."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_protection_title",
        value = "Can the planet shield itself?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_protection_description",
        value = "A planet's ability to protect itself is primarily via a magnetic field which is very hard to detect. So mass and density are " +
                "used as a proxy for a large, molten iron core capable of generating a magnetosphere. " +
                "Higher mass helps maintain a molten core and higher density suggests a large iron core."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_tidal_locking_title",
        value = "Is the planet free from extreme temperature lock?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_tidal_locking_description",
        value = "The planet's orbital period is used to determine the risk of tidal locking. The star's spectral type is also factored in if " +
                "available as the risk is much higher for smaller stars as their habitable zones are closer."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_esi_title",
        value = "What is the planet's Earth Similarity Index?\n"

    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_planet_esi_description",
        value = "The Earth Similarity Index (ESI) is a scale used to quantify how similar a planet is to Earth based on physical properties like " +
                "radius, density, escape velocity and surface temperature. Escape velocity can be calculated with mass and radius but the surface " +
                "temperature might be hard to get. In this scenario, the equilibrium temperature or temperature calculated through occultation depth " +
                "or insolation flux can be used as a proxy for surface temperature.\nA high ESI indicates an Earth-sized, rocky world with a similar " +
                "temperature. It is however, a 'bonus' factor as the previous calculations cover much of this metric. It also has smaller weight " +
                "because this model is highly sensitive to temperature and does not consider other factors like the host star's activity " +
                "or atmospheric composition."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_spectral_type_title",
        value = "Is the star stable and does it have a long, stable lifetime?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_spectral_type_description",
        value = "The primary determinant of a star's lifetime and stability is spectral type and mass. If the star is too massive, it has a " +
                "short lifespan, conversely a lighter star is very long-lived, but has also other issues.\nThe system used for spectral type " +
                "classification it the Harvard spectral type with Morgan-Keenan luminosity class, (e.g. G2III):\n" +
                "• G ideal, like the Sun\n" +
                "• K long-lived and stable with less UV radiation\n" +
                "• F good candidates, but with more UV radiation than the Sun\n" +
                "• M very long-lived, but prone to flaring\n" +
                "• A short-lived and with significant UV radiation\n" +
                "• B very short-lived and with high UV radiation\n" +
                "• O extremely short-lived and with high UV radiation\n" +
                "• L, T and Y are brown dwarfs with an unstable HZ\n" +
                "• D are white dwarfs like remnant cores with harsh radiation\n" +
                "• C and S carbon or S-type stars which are evolved, unstable giants\n" +
                "• W, Q and P are catastrophically hostile or unstable stars\n" +
                "I then apply a small penalty for lower luminosity and subtypes farther away from 5 for the most habitable classes as stars in " +
                "the middle of the range are often considered more stable than those at the extremes."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_age_title",
        value = "Is the star old enough for life, but not too old?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_age_description",
        value = "The ideal age for a star to host a habitable planet is generally considered to be between 4 and 6 billion years old.\n" +
                "A star that is too young is often highly active and can strip a planet of its atmosphere and bathe its surface in harmful radiation. " +
                "On the other hand, a star that is too old is also unsuitable. As a star exhausts the hydrogen fuel in its core, it begins to evolve " +
                "into a red giant. This process causes it to expand and increase in luminosity, which would boil away the oceans."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_activity_title",
        value = "Is the star prone to violent flares?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_activity_description",
        value = "Besides age, a star's rotational velocity and period is also a good indicator of its activity. Fast rotation implies high " +
                "stellar activity (flares) and youth, which is less habitable."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_gravity_title",
        value = "Is it a compact main-sequence star or a giant?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_gravity_description",
        value = "A star with high gravity is indicative of a stable main-sequence star, while low gravity indicates an unstable giant."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_metallicity_title",
        value = "Does it have the right materials to form rocky planets?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_metallicity_description",
        value = "A star's metallicity is a key indicator of the raw materials that were available when the star and its planets were forming.\n" +
                "A star rich in metals means the primordial cloud it formed from was also rich in the necessary building blocks for rocky planets, " +
                "such as silicon, oxygen, magnesium, and iron. This makes it much more likely that the system hosts terrestrial, potentially " +
                "habitable worlds.\nWhereas, a star with very low metallicity formed from a more pristine cloud that lacked these heavy elements, " +
                "and is therefore very unlikely that such a system would have had enough raw material to form rocky planets of any significant size."
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_effective_temperature_title",
        value = "Is its temperature ideal?"
    ),
    Translation(
        languageIso = "en",
        key = "explore_screen__habitability_star_effective_temperature_description",
        value = "This score is based on the stellar effective temperature and it peaks at the Sun's temperature of 5780K " +
                "and decreases for hotter or cooler stars."
    ),
)

private val creditsScreenTranslations = listOf(
    Translation(
        languageIso = "en",
        key = "credits_screen__creators",
        value = "Creator"
    ),
    Translation(
        languageIso = "en",
        key = "credits_screen__supporters",
        value = "Supporters"
    ),
    Translation(
        languageIso = "en",
        key = "credits_screen__sources",
        value = "Data Sources"
    ),
    Translation(
        languageIso = "en",
        key = "credits_screen__music",
        value = "Music"
    ),
)

internal val translations = listOf(
    Translation(
        languageIso = "en",
        key = "app_name",
        value = "The Last Voyage"
    )
) +
        stellarTranslations +
        catastropheTranslations +
        shipTranslations +
        engineTranslations +
        eventTranslations +
        gameOverTranslations +
        habitabilityTranslations +
        errorScreenTranslations +
        splashScreenTranslations +
        mainMenuScreenTranslations +
        newGameScreenTranslations +
        gameScreenTranslations +
        scoreScreenTranslations +
        exploreScreenTranslations +
        creditsScreenTranslations
