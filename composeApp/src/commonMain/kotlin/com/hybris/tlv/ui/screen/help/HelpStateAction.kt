package com.hybris.tlv.ui.screen.help

internal sealed interface HelpAction {
    data object Navigation: HelpAction
    data object ControlPanel: HelpAction
    data object HostDefinition: HelpAction
    data object HostType: HelpAction
    data object PlanetDefinition: HelpAction
    data object PlanetType: HelpAction
    data object Mechanics: HelpAction
    data object Habitability: HelpAction
    data object Score: HelpAction
    data class VersionClick(val reset: Boolean): HelpAction
}

internal data class HelpState(
    val loading: Boolean = true,
    val currentContent: Content = Content.LEARN_MENU,
    val formula: String? = null,
    val showSnackbar: Boolean = false
)

internal enum class Content {
    LEARN_MENU,
    NAVIGATION,
    CONTROL_PANEL,
    HOST_DEFINITION,
    HOST_TYPE,
    PLANET_DEFINITION,
    PLANET_TYPE,
    HABITABILITY,
    SCORE
}
