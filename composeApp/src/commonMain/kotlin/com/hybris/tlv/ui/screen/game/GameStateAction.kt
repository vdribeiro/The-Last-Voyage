package com.hybris.tlv.ui.screen.game

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hybris.tlv.domain.ship.Ship
import com.hybris.tlv.domain.usecase.space.model.Planet
import com.hybris.tlv.domain.usecase.space.model.StellarHost

internal sealed interface GameAction {
    data object Back: GameAction
    data class ChangeTab(val content: Content): GameAction
    data class Travel(val stellarHost: StellarHost): GameAction
    data class Settle(val planet: Planet): GameAction
}

internal data class GameState(
    val loading: Boolean = true,
    val currentContent: Content = Content.SYSTEM,
    val ship: Ship? = null,
    val currentStellarHost: StellarHost? = null,
    val currentStellarHostPlanets: ImmutableList<Planet> = persistentListOf(),
    val nearStellarHosts: ImmutableList<StellarHost> = persistentListOf()
)

internal enum class Content {
    SHIP,
    SYSTEM,
    TRAVEL,
}
