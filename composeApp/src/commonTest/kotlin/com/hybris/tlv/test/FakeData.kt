package com.hybris.tlv.test

import com.hybris.tlv.core.resource.JsonResource
import com.hybris.tlv.data.config.Configs
import com.hybris.tlv.data.serializer.loadFromJsonResource
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
        loadFromJsonResource<Translation>(json = JsonResource.Translations)
    }
    val catastrophes: LazyData<List<Catastrophe>> = LazyData {
        loadFromJsonResource<Catastrophe>(json = JsonResource.Catastrophes)
    }
    val engines: LazyData<List<Engine>> = LazyData {
        loadFromJsonResource<Engine>(json = JsonResource.Engines)
    }
    val events: LazyData<List<Event>> = LazyData {
        loadFromJsonResource<Event>(json = JsonResource.Events)
    }
    val achievements: LazyData<List<Achievement>> = LazyData {
        loadFromJsonResource<Achievement>(json = JsonResource.Achievements)
    }
    val credits: LazyData<List<Credit>> = LazyData {
        loadFromJsonResource<Credit>(json = JsonResource.Credits)
    }
    val stellarHosts: LazyData<List<StellarHost>> = LazyData {
        loadFromJsonResource<StellarHost>(json = JsonResource.StellarHosts)
    }
    val planets: LazyData<List<Planet>> = LazyData {
        loadFromJsonResource<Planet>(json = JsonResource.Planets)
    }
    val stellarHostsWithPlanets: LazyData<List<StellarHost>> = LazyData {
        stellarHosts.get().addPlanets(planets = planets.get())
    }
    val shipPrototype: ShipPrototype by lazy {
        ShipPrototype(
            assignedPoints = 10,
            sensorRange = 5,
            fuel = 100,
            materials = 90,
            cryopods = 150,
        )
    }
    val ship: LazyData<Ship> = LazyData {
        Ship(
            id = "1",
            engine = engines.get().random(),
            assignedPoints = 10,
            yearsTraveled = 100.0,
            sensorRange = 5,
            integrity = 80,
            fuel = 100,
            materials = 90,
            cryopods = 150,
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
