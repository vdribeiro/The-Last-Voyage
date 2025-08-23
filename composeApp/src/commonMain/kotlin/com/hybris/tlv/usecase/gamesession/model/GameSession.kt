package com.hybris.tlv.usecase.gamesession.model

import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Formula

internal data class GameSession(
    val id: String,
    val utc: String,
    val ship: Ship,
    val currentStellarHostId: String?,
    val visitedStellarHosts: Set<String>,
    val launchedEvents: Set<String>,
    val settledPlanetId: String?,
    val finalHabitability: Double?,
    val score: Double?,
    val formula: Formula
)
