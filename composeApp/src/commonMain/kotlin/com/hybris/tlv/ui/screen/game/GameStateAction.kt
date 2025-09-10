package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

internal sealed interface GameAction {
    data object NextTutorial: GameAction
    data class ChangeTab(val content: Content): GameAction
    data class Travel(val stellarHost: StellarHost): GameAction
    data class Settle(val planet: Planet): GameAction
}

internal data class GameState(
    val loading: Boolean? = null,
    val tutorial: Tutorial? = null,
    val currentContent: Content? = null,
    val gameSession: GameSession? = null,
    val currentStellarHost: StellarHost? = null,
    val nearStellarHosts: List<StellarHost>? = null,
)

internal enum class Content {
    SHIP,
    SYSTEM,
    TRAVEL,
}

internal enum class Tutorial {
    NO,
    YES,
    SHIP,
    TRAVEL,
    SYSTEM,
}
