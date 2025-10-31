package com.hybris.tlv.ui.screen.help

import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType

internal sealed interface HelpAction {
    data object HostDefinition: HelpAction
    data object PlanetDefinition: HelpAction
    data object Mechanics: HelpAction
    data object Habitability: HelpAction
}

internal data class HelpState(
    val loading: Boolean = true,
    val currentContent: Content = Content.LEARN_MENU,
    val featureTutorial: Boolean = false,
    val learningsMap: Map<LearningType, List<Learning>> = emptyMap(),
    val formula: String = "",
)

internal enum class Content {
    LEARN_MENU,
    HOST_DEFINITION,
    PLANET_DEFINITION,
    HABITABILITY,
}
