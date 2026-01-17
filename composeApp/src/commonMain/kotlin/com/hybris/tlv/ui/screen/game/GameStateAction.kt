package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.domain.usecase.ship.model.Ship
import com.hybris.tlv.domain.usecase.space.model.Planet
import com.hybris.tlv.domain.usecase.space.model.StellarHost

internal sealed interface GameAction {
    data class ChangeTab(val content: Content): GameAction
    data class Travel(val stellarHost: StellarHost): GameAction
    data class Settle(val planet: Planet): GameAction
}

internal data class GameState(
    val loading: Boolean = true,
    val currentContent: Content = Content.SYSTEM,
    val ship: Ship? = null,
    val currentStellarHost: StellarHost? = null,
    val nearStellarHosts: List<StellarHost> = emptyList()
)

internal enum class Content {
    SHIP,
    SYSTEM,
    TRAVEL,
}
