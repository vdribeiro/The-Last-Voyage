package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

internal sealed interface GameAction {
    data class ChangeTab(val content: Content): GameAction
    data class Travel(val stellarHost: StellarHost): GameAction
    data class Settle(val planet: Planet): GameAction
}

internal data class GameState(
    val loading: Boolean,
    val currentContent: Content,
    val gameSession: GameSession?,
    val ship: Ship?,
    val currentStellarHost: StellarHost?,
    val nearStellarHosts: List<StellarHost>
)

internal enum class Content {
    SHIP,
    SYSTEM,
    TRAVEL,
}
