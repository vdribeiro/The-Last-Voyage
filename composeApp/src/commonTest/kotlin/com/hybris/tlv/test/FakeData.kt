package com.hybris.tlv.test

import com.hybris.tlv.data.config.Configs
import com.hybris.tlv.data.resource.JsonResource
import com.hybris.tlv.data.resource.loadResource
import com.hybris.tlv.domain.usecase.achievement.model.Achievement
import com.hybris.tlv.domain.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.domain.usecase.credit.model.Credit
import com.hybris.tlv.domain.usecase.event.model.Event
import com.hybris.tlv.domain.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.domain.usecase.ship.model.Engine
import com.hybris.tlv.domain.usecase.ship.model.Ship
import com.hybris.tlv.domain.usecase.ship.model.ShipPrototype
import com.hybris.tlv.domain.usecase.space.addPlanets
import com.hybris.tlv.domain.usecase.space.model.Formula
import com.hybris.tlv.domain.usecase.space.model.Planet
import com.hybris.tlv.domain.usecase.space.model.StellarHost
import com.hybris.tlv.domain.usecase.translation.TranslationGateway.Companion.loadAllTranslationsFromJsonResource
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.platform.Property

internal object FakeData {
    val configs: Configs by lazy {
        Configs(
            appVersion = Property.APP_VERSION_NUMBER,
            translationsVersion = Long.MAX_VALUE,
            catastrophesVersion = Long.MAX_VALUE,
            enginesVersion = Long.MAX_VALUE,
            stellarHostsVersion = Long.MAX_VALUE,
            planetsVersion = Long.MAX_VALUE,
            eventsVersion = Long.MAX_VALUE,
            achievementsVersion = Long.MAX_VALUE,
            creditsVersion = Long.MAX_VALUE,
            developerCorner = "https://github.com/vdribeiro/The-Last-Voyage",
            formula = "https://github.com/vdribeiro/The-Last-Voyage",
        )
    }
    val translations: LazyData<List<Translation>> = LazyData {
        loadAllTranslationsFromJsonResource()
    }
    val catastrophes: LazyData<List<Catastrophe>> = LazyData {
        loadResource<Catastrophe>(json = JsonResource.Catastrophes)
    }
    val catastrophesTranslations: LazyData<List<Translation>> = LazyData {
        loadResource<Translation>(json = JsonResource.CatastrophesTranslations)
    }
    val engines: LazyData<List<Engine>> = LazyData {
        loadResource<Engine>(json = JsonResource.Engines)
    }
    val enginesTranslations: LazyData<List<Translation>> = LazyData {
        loadResource<Translation>(json = JsonResource.EnginesTranslations)
    }
    val events: LazyData<List<Event>> = LazyData {
        loadResource<Event>(json = JsonResource.Events)
    }
    val eventsTranslations: LazyData<List<Translation>> = LazyData {
        loadResource<Translation>(json = JsonResource.EventsTranslations)
    }
    val achievements: LazyData<List<Achievement>> = LazyData {
        loadResource<Achievement>(json = JsonResource.Achievements)
    }
    val achievementsTranslations: LazyData<List<Translation>> = LazyData {
        loadResource<Translation>(json = JsonResource.AchievementsTranslations)
    }
    val credits: LazyData<List<Credit>> = LazyData {
        loadResource<Credit>(json = JsonResource.Credits)
    }
    val stellarHosts: LazyData<List<StellarHost>> = LazyData {
        loadResource<StellarHost>(json = JsonResource.StellarHosts)
    }
    val planets: LazyData<List<Planet>> = LazyData {
        loadResource<Planet>(json = JsonResource.Planets)
    }
    val stellarHostsWithPlanets: LazyData<List<StellarHost>> = LazyData {
        stellarHosts.get().addPlanets(planets = planets.get())
    }
    val shipPrototype: ShipPrototype by lazy {
        ShipPrototype(
            assignedPoints = 25,
            sensorRange = 4,
            fuel = 1000,
            materials = 500,
            cryopods = 500,
        )
    }
    val ship: LazyData<Ship> = LazyData {
        Ship(
            id = "1",
            engine = engines.get().random(),
            assignedPoints = 25,
            yearsTraveled = 100.0,
            sensorRange = 4,
            integrity = 80,
            fuel = 1000,
            materials = 500,
            cryopods = 500,
        )
    }
    val gameSessionPrototype: LazyData<GameSessionPrototype> = LazyData {
        GameSessionPrototype(
            ship = shipPrototype,
            engine = engines.get().random(),
            formula = Formula()
        )
    }
}
