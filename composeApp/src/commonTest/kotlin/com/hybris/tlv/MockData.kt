package com.hybris.tlv

import com.hybris.tlv.config.Configs
import com.hybris.tlv.locale.now
import com.hybris.tlv.ui.screen.newgame.ShipState
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.model.Precondition
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.credit.model.CreditType
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.model.TravelOutcome
import com.hybris.tlv.usecase.translation.model.Translation

internal val learnings: List<Learning> by lazy {
    listOf(
        Learning(
            id = "stellar_host_distance",
            description = "stellar_host_distance_description",
            image = null,
            type = LearningType.HOST_PROPERTY
        ),
        Learning(
            id = "planet_orbital_period",
            description = "planet_orbital_period_description",
            image = null,
            type = LearningType.PLANET_PROPERTY
        ),
        Learning(
            id = "stellar_host_type_unknown",
            description = "stellar_host_type_unknown_description",
            image = "?",
            type = LearningType.HOST_TYPE
        ),
        Learning(
            id = "planet_type_unknown",
            description = "planet_type_unknown_description",
            image = "?",
            type = LearningType.PLANET_TYPE
        ),
        Learning(
            id = "habitability_hz",
            description = "habitability_hz_description",
            image = null,
            type = LearningType.FORMULA
        )
    )
}

internal val catastrophes: List<Catastrophe> by lazy {
    listOf(
        Catastrophe(
            id = "catastrophe__asteroid_impact",
            description = "catastrophe__asteroid_impact_description",
        ),
        Catastrophe(
            id = "catastrophe__nuclear_war",
            description = "catastrophe__nuclear_war_description",
        ),
    )
}

internal val engines: List<Engine> by lazy {
    listOf(
        Engine(
            id = "engine__alcubierre_drive",
            description = "engine__alcubierre_drive_description",
            velocity = 1.0,
        ),
        Engine(
            id = "engine__liquid_fuel_rocket",
            description = "engine__liquid_fuel_rocket_description",
            velocity = 0.000014677,
        ),
        Engine(
            id = "engine__solar_sail",
            description = "engine__solar_sail_description",
            velocity = 0.5,
        ),
        Engine(
            id = "engine__wormhole_generator",
            description = "engine__wormhole_generator_description",
            velocity = 299000000.0,
        )
    )
}

internal val stellarHosts: List<StellarHost> by lazy {
    listOf(
        StellarHost(
            id = "sol",
            name = "Sol",
            systemName = "Sol",
            spectralType = "G2V",
            effectiveTemperature = 5778.0,
            radius = 1.0,
            mass = 1.0,
            metallicity = 0.0,
            luminosity = 1.0,
            gravity = 1.0,
            age = 4.6,
            density = 1.410,
            rotationalVelocity = 2.0,
            rotationalPeriod = 25.05,
            distance = 0.0,
            ra = 0.0,
            dec = 0.0
        ),
        StellarHost(
            id = "proxima_centauri",
            name = "Proxima Centauri",
            systemName = "Alpha Centauri",
            spectralType = "M5.5V",
            effectiveTemperature = 2900.0,
            radius = 0.141,
            mass = 0.1221,
            metallicity = null,
            luminosity = -2.8,
            gravity = 5.3201025,
            age = null,
            density = 48.7626491,
            rotationalVelocity = null,
            rotationalPeriod = 90.0,
            distance = 4.2439092564,
            ra = 217.3934657,
            dec = -62.6761821
        ),
        StellarHost(
            id = "alpha_centauri_b",
            name = "Alpha Centauri B",
            systemName = "Alpha Centauri",
            spectralType = "K0V",
            effectiveTemperature = 5178.0,
            radius = 0.88,
            mass = 0.88,
            metallicity = 0.23,
            luminosity = -0.284,
            gravity = 1.2083702,
            age = 8.0,
            density = null,
            rotationalVelocity = 0.9,
            rotationalPeriod = 42.0,
            distance = 4.39492274596,
            ra = 219.9141283,
            dec = -60.8394714
        ),
        StellarHost(
            id = "alpha_centauri_a",
            name = "Alpha Centauri A",
            systemName = "Alpha Centauri",
            spectralType = "G2.0V",
            effectiveTemperature = 5801.0,
            radius = 1.25,
            mass = 1.06,
            metallicity = 0.21,
            luminosity = 0.207,
            gravity = 0.7281150,
            age = 7.84,
            density = null,
            rotationalVelocity = 2.3,
            rotationalPeriod = 29.0,
            distance = 4.39492274596,
            ra = 219.9204103,
            dec = -60.8351471
        ),
    )
}

internal val planets: List<Planet> by lazy {
    listOf(
        Planet(
            id = "mercury",
            name = "Mercury",
            stellarHostId = "sol",
            status = PlanetStatus.CONFIRMED,
            orbitalPeriod = 88.0,
            orbitAxis = 0.387,
            radius = 0.383,
            mass = 0.055,
            density = 5.429,
            eccentricity = 0.206,
            insolationFlux = 6.674,
            equilibriumTemperature = 440.0,
            occultationDepth = 0.000012,
            inclination = 7.01,
            obliquity = 0.034,
        ),
        Planet(
            id = "venus",
            name = "Venus",
            stellarHostId = "sol",
            status = PlanetStatus.CONFIRMED,
            orbitalPeriod = 224.7,
            orbitAxis = 0.723,
            radius = 0.950,
            mass = 0.815,
            density = 5.243,
            eccentricity = 0.007,
            insolationFlux = 1.911,
            equilibriumTemperature = 228.0,
            occultationDepth = 0.000076,
            inclination = 3.39,
            obliquity = 177.4,
        ),
        Planet(
            id = "earth",
            name = "Earth",
            stellarHostId = "sol",
            status = PlanetStatus.CONFIRMED,
            orbitalPeriod = 365.2,
            orbitAxis = 1.000,
            radius = 1.0,
            mass = 1.0,
            density = 5.514,
            eccentricity = 0.017,
            insolationFlux = 1.000,
            equilibriumTemperature = 255.0,
            occultationDepth = 0.000084,
            inclination = 0.0,
            obliquity = 23.4,
        ),
        Planet(
            id = "mars",
            name = "Mars",
            stellarHostId = "sol",
            status = PlanetStatus.CONFIRMED,
            orbitalPeriod = 687.0,
            orbitAxis = 1.524,
            radius = 0.532,
            mass = 0.107,
            density = 3.934,
            eccentricity = 0.094,
            insolationFlux = 0.430,
            equilibriumTemperature = 210.0,
            occultationDepth = 0.000024,
            inclination = 1.85,
            obliquity = 25.2,
        ),
        Planet(
            id = "jupiter",
            name = "Jupiter",
            stellarHostId = "sol",
            status = PlanetStatus.CONFIRMED,
            orbitalPeriod = 4331.0,
            orbitAxis = 5.204,
            radius = 11.209,
            mass = 317.83,
            density = 1.326,
            eccentricity = 0.049,
            insolationFlux = 0.037,
            equilibriumTemperature = 110.0,
            occultationDepth = 0.010531,
            inclination = 1.31,
            obliquity = 3.1,
        ),
        Planet(
            id = "saturn",
            name = "Saturn",
            stellarHostId = "sol",
            status = PlanetStatus.CONFIRMED,
            orbitalPeriod = 10747.0,
            orbitAxis = 9.582,
            radius = 9.449,
            mass = 95.16,
            density = 0.687,
            eccentricity = 0.052,
            insolationFlux = 0.011,
            equilibriumTemperature = 81.0,
            occultationDepth = 0.007515,
            inclination = 2.49,
            obliquity = 26.7,
        ),
        Planet(
            id = "uranus",
            name = "Uranus",
            stellarHostId = "sol",
            status = PlanetStatus.CONFIRMED,
            orbitalPeriod = 30589.0,
            orbitAxis = 19.189,
            radius = 4.007,
            mass = 14.54,
            density = 1.270,
            eccentricity = 0.047,
            insolationFlux = 0.003,
            equilibriumTemperature = 58.0,
            occultationDepth = 0.001347,
            inclination = 0.77,
            obliquity = 97.8,
        ),
        Planet(
            id = "neptune",
            name = "Neptune",
            stellarHostId = "sol",
            status = PlanetStatus.CONFIRMED,
            orbitalPeriod = 59800.0,
            orbitAxis = 30.070,
            radius = 3.883,
            mass = 17.15,
            density = 1.638,
            eccentricity = 0.010,
            insolationFlux = 0.001,
            equilibriumTemperature = 46.0,
            occultationDepth = 0.001264,
            inclination = 1.77,
            obliquity = 28.3,
        ),
        Planet(
            id = "proxima_centauri_b",
            name = "Proxima Centauri b",
            stellarHostId = "proxima_centauri",
            status = PlanetStatus.CONFIRMED,
            orbitalPeriod = 11.1868,
            orbitAxis = 0.04856,
            radius = 1.03,
            mass = 1.07,
            density = 5.38,
            eccentricity = 0.02,
            insolationFlux = 0.65,
            equilibriumTemperature = 234.0,
            occultationDepth = null,
            inclination = null,
            obliquity = null,
        ),
        Planet(
            id = "proxima_centauri_c",
            name = "Proxima Centauri c",
            stellarHostId = "proxima_centauri",
            status = PlanetStatus.CANDIDATE,
            orbitalPeriod = 1900.0,
            orbitAxis = 1.48,
            radius = null,
            mass = 5.8,
            density = null,
            eccentricity = 0.0,
            insolationFlux = null,
            equilibriumTemperature = 39.0,
            occultationDepth = null,
            inclination = null,
            obliquity = null,
        ),
        Planet(
            id = "proxima_centauri_d",
            name = "Proxima Centauri d",
            stellarHostId = "proxima_centauri",
            status = PlanetStatus.CANDIDATE,
            orbitalPeriod = 5.122,
            orbitAxis = 0.02885,
            radius = null,
            mass = 0.26,
            density = null,
            eccentricity = 0.040,
            insolationFlux = null,
            equilibriumTemperature = null,
            occultationDepth = null,
            inclination = null,
            obliquity = null,
        ),
        Planet(
            id = "alpha_centauri_b_b",
            name = "Alpha Centauri B b",
            stellarHostId = "alpha_centauri_b",
            status = PlanetStatus.FALSE,
            orbitalPeriod = 3.2357,
            orbitAxis = null,
            radius = null,
            mass = 1.13,
            density = null,
            eccentricity = 0.0,
            insolationFlux = null,
            equilibriumTemperature = null,
            occultationDepth = null,
            inclination = null,
            obliquity = null,
        ),
    )
}

internal val hostsWithPlanets: List<StellarHost> by lazy {
    val planetsMap = planets.groupBy { it.stellarHostId }
    stellarHosts.apply {
        forEach { it.planets.addAll(elements = planetsMap[it.id].orEmpty()) }
    }.sortedWith(comparator = compareBy<StellarHost, Double?>(comparator = nullsLast()) { it.distance }.thenBy { it.id })
}

internal val events: List<Event> by lazy {
    listOf(
        Event(
            id = "event__engine_misfire",
            description = "event__engine_misfire_description",
            parentId = null,
            outcome = TravelOutcome(
                materials = -10,
                fuel = -3,
            ),
        ),
        Event(
            id = "event__solar_flare",
            description = "event__solar_flare_description",
            parentId = null,
            outcome = TravelOutcome(
                materials = -5,
                fuel = -3,
            ),
        ),
        Event(
            id = "event__a_close_pass",
            description = "event__a_close_pass_description",
            parentId = null,
            outcome = null
        ),
        Event(
            id = "event__a_close_pass_for_science",
            description = "event__a_close_pass_for_science_description",
            parentId = "a_close_pass",
            outcome = TravelOutcome(
                cryopods = -15,
                fuel = +10
            )
        ),
        Event(
            id = "event__a_close_pass_ignore",
            description = "event__a_close_pass_ignore_description",
            parentId = "a_close_pass",
            outcome = null
        )
    )
}

internal val shipPrototype: ShipPrototype by lazy {
    ShipPrototype(
        assignedPoints = 10,
        sensorRange = 5,
        fuel = 100,
        materials = 90,
        cryopods = 150,
    )
}

internal val ship: Ship by lazy {
    Ship(
        id = "1",
        assignedPoints = 10,
        yearsTraveled = 100.0,
        sensorRange = 5,
        integrity = 80,
        fuel = 100,
        materials = 90,
        cryopods = 150,
    )
}

internal val shipState: ShipState by lazy {
    ShipState(
        sensorRange = ShipState.Point(max = 10, min = 1, interval = 1, initialValue = 3),
        materials = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        fuel = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        cryopods = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
    )
}

internal val gameSessionPrototype: GameSessionPrototype by lazy {
    GameSessionPrototype(
        ship = shipPrototype,
        formula = formula
    )
}

internal val gameSession: GameSession by lazy {
    GameSession(
        id = "1",
        utc = now(),
        currentStellarHostId = stellarHosts.first().id,
        visitedStellarHosts = emptySet(),
        launchedEvents = emptySet(),
        settledPlanetId = null,
        finalHabitability = null,
        score = null,
        ship = ship,
        formula = formula
    )
}

internal val gameSessionFinished: GameSession by lazy {
    GameSession(
        id = "2",
        utc = now(),
        currentStellarHostId = stellarHosts.random().id,
        visitedStellarHosts = stellarHosts.shuffled().take(n = 2).map { it.id }.toSet(),
        launchedEvents = events.shuffled().take(n = 2).map { it.id }.toSet(),
        settledPlanetId = "earth",
        finalHabitability = 90.0,
        score = 9000.0,
        ship = ship,
        formula = formula
    )
}

internal val formula: Formula by lazy {
    Formula(id = "1")
}

internal val achievements: List<Achievement> by lazy {
    listOf(
        Achievement(
            id = "earth",
            name = "Earth",
            description = "Settle on Earth",
            preconditions = Precondition(
                settledPlanetId = "earth"
            ),
            status = false
        )
    )
}

internal val credits: List<Credit> by lazy {
    listOf(
        Credit(
            id = "engsoneca",
            link = "https://ko-fi.com/engsoneca",
            type = CreditType.CREATOR,
        ),
        Credit(
            id = "NASA Exoplanet Archive DOIs 10.26133/NEA13 and 10.26133/NEA40",
            link = "https://exoplanetarchive.ipac.caltech.edu/",
            type = CreditType.SOURCE,
        ),
        Credit(
            id = "OpenGameArt",
            link = "https://opengameart.org/",
            type = CreditType.MUSIC,
        ),
        Credit(
            id = "You",
            link = null,
            type = CreditType.SUPPORTER,
        ),
        Credit(
            id = "Yourself",
            link = null,
            type = CreditType.SUPPORTER,
        ),
        Credit(
            id = "Irene",
            link = null,
            type = CreditType.SUPPORTER,
        ),
        Credit(
            id = "Jim",
            link = null,
            type = CreditType.SUPPORTER,
        ),
    )
}

internal val configs: List<Configs> by lazy {
    listOf(
        Configs(
            translationsVersion = 1,
            learningsVersion = 1,
            catastrophesVersion = 1,
            enginesVersion = 1,
            stellarHostsVersion = 1,
            planetsVersion = 1,
            eventsVersion = 1,
            achievementsVersion = 1,
            creditsVersion = 1,
        )
    )
}

internal val translations: List<Translation> by lazy {
    listOf(
        Translation(
            languageIso = "en",
            key = "app_name",
            value = "The Last Voyage"
        ),
        Translation(
            key = "website",
            value = "Website"
        ),
        Translation(
            key = "main_menu_screen__new_game",
            value = "New Game"
        ),
        Translation(
            key = "main_menu_screen__continue",
            value = "Continue"
        ),
        Translation(
            key = "main_menu_screen__learn",
            value = "Learn"
        ),
        Translation(
            key = "main_menu_screen__soon",
            value = "Coming Soon..."
        ),
        Translation(
            key = "main_menu_screen__scores",
            value = "Scores"
        ),
        Translation(
            key = "main_menu_screen__achievements",
            value = "Achievements"
        ),
        Translation(
            key = "main_menu_screen__credits",
            value = "Credits"
        ),
        Translation(
            key = "main_menu_screen__stellar_explorer",
            value = "Stellar Explorer"
        ),
        Translation(
            key = "main_menu_screen__host_definition",
            value = "Star Definition"
        ),
        Translation(
            key = "main_menu_screen__definition_example",
            value = "Example"
        ),
        Translation(
            key = "main_menu_screen__definition_properties",
            value = "Properties"
        ),
        Translation(
            key = "main_menu_screen__definition_types",
            value = "Types"
        ),
        Translation(
            key = "main_menu_screen__planet_definition",
            value = "Planet Definition"
        ),
        Translation(
            key = "main_menu_screen__mechanics",
            value = "Tutorial"
        ),
        Translation(
            key = "main_menu_screen__habitability",
            value = "Habitability Formula"
        ),
        Translation(
            key = "stellar_explorer_screen__host_list",
            value = "Stellar Hosts"
        ),
        Translation(
            key = "stellar_explorer_screen__planet_list",
            value = "Planets"
        ),
        Translation(
            languageIso = "en",
            key = "catastrophe__asteroid_impact",
            value = "Asteroid Impact"
        ),
        Translation(
            languageIso = "en",
            key = "catastrophe__asteroid_impact_description",
            value = "A massive asteroid collides with Earth, causing widespread destruction, tsunamis, earthquakes, and atmospheric changes. The impact wipes out most life on the planet.\n\nOne ship escapes and begins: The Last Voyage."
        ),
        Translation(
            languageIso = "en",
            key = "catastrophe__nuclear_war",
            value = "Nuclear War"
        ),
        Translation(
            languageIso = "en",
            key = "catastrophe__nuclear_war_description",
            value = "A global conflict escalates into a full-scale nuclear exchange. Cities are destroyed, radiation spreads, and the aftermath leads to a nuclear winter, plunging the world into darkness and devastation.\n\nOne ship escapes and begins: The Last Voyage."
        ),
        Translation(
            languageIso = "en",
            key = "engine__alcubierre_drive",
            value = "Alcubierre Drive"
        ),
        Translation(
            languageIso = "en",
            key = "engine__alcubierre_drive_description",
            value = "A warp drive that can travel faster than light by warping space-time."
        ),
        Translation(
            languageIso = "en",
            key = "engine__liquid_fuel_rocket",
            value = "Liquid-fuel Rocket"
        ),
        Translation(
            languageIso = "en",
            key = "engine__liquid_fuel_rocket_description",
            value = "Uses liquid fuel and oxidizer."
        ),
        Translation(
            key = "game_screen__travel",
            value = "Travel"
        ),
        Translation(
            key = "game_screen__system",
            value = "System"
        ),
        Translation(
            key = "game_screen__ship",
            value = "Ship"
        ),
        Translation(
            languageIso = "en",
            key = "event__engine_misfire",
            value = "Engine Misfire"
        ),
        Translation(
            languageIso = "en",
            key = "event__engine_misfire_description",
            value = "Your engine clogs unexpectedly. You lose time performing emergency maintenance and consume some materials."
        ),
        Translation(
            languageIso = "en",
            key = "event__solar_flare",
            value = "Solar Flare"
        ),
        Translation(
            languageIso = "en",
            key = "event__solar_flare_description",
            value = "You pass a star during an intense solar flare. Your ship's sensors are temporarily scrambled and your arrival is delayed."
        ),
        Translation(
            languageIso = "en",
            key = "event__a_close_pass",
            value = "A Close Pass"
        ),
        Translation(
            languageIso = "en",
            key = "event__a_close_pass_description",
            value = "The ship's trajectory will pass relatively close to a highly active pulsar. Its intense magnetic fields and radiation beams are dangerous. However, a closer pass would allow the ship to gather priceless scientific data on neutron stars and could potentially calibrate the navigation system with extreme precision."
        ),
        Translation(
            languageIso = "en",
            key = "event__a_close_pass_for_science",
            value = "Make a Close Pass for Science"
        ),
        Translation(
            languageIso = "en",
            key = "event__a_close_pass_for_science_description",
            value = "The data is a massive success, providing a permanent boost to navigational accuracy. But the ship is lashed by magnetic fields, causing a power surge that shorts out several cryopods."
        ),
        Translation(
            languageIso = "en",
            key = "event__a_close_pass_ignore",
            value = "Maintain a Safe Distance"
        ),
        Translation(
            languageIso = "en",
            key = "event__a_close_pass_ignore_description",
            value = "The risk of system damage is too great. The ship gives the pulsar a wide berth, and the unique scientific opportunity is lost."
        ),
        Translation(
            key = "game_over_screen__game_over",
            value = "Game Over"
        ),
        Translation(
            key = "game_over_screen__score",
            value = "Score"
        ),
        Translation(
            key = "game_over_screen__end",
            value = "End"
        ),
        Translation(
            key = "error_screen__title",
            value = "Oops! Something went wrong."
        ),
        Translation(
            key = "error_screen__title_alt",
            value = "Feedback"
        ),
        Translation(
            key = "error_screen__description",
            value = "It seems we've run into an uncharted anomaly. Our engineering crew is already on it, but a description from you would be invaluable."
        ),
        Translation(
            key = "error_screen__description_alt",
            value = "Your insights are valuable, whether you have an idea or have found something that isn't working right."
        ),
        Translation(
            key = "error_screen__button",
            value = "Submit Feedback"
        ),
        Translation(
            key = "error_screen__thanks",
            value = "Thank you for your feedback!"
        ),
        Translation(
            key = "credit_screen__creators",
            value = "Creator"
        ),
        Translation(
            key = "credit_screen__supporters",
            value = "Supporters"
        ),
        Translation(
            key = "credit_screen__sources",
            value = "Data Sources"
        ),
        Translation(
            key = "credit_screen__music",
            value = "Music"
        ),
    )
}
