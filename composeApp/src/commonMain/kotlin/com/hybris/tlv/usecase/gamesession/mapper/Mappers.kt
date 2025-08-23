package com.hybris.tlv.usecase.gamesession.mapper

import com.hybris.tlv.database.GameSessionSchema
import com.hybris.tlv.datetime.now
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.mapper.toShip

internal fun GameSessionPrototype.toGameSession(id: String = generateUuid()): GameSession =
    GameSession(
        id = id,
        utc = now(),
        currentStellarHostId = null,
        visitedStellarHosts = emptySet(),
        launchedEvents = emptySet(),
        settledPlanetId = null,
        finalHabitability = null,
        score = null,
        ship = ship.toShip(id),
        formula = formula.copy(id = id)
    )

internal fun GameSession.toGameSessionSchema(): GameSessionSchema =
    GameSessionSchema(
        id = id,
        utc = utc,
        currentStellarHostId = currentStellarHostId,
        visitedStellarHosts = visitedStellarHosts,
        launchedEvents = launchedEvents,
        settledPlanetId = settledPlanetId,
        finalHabitability = finalHabitability,
        score = score,
    )
