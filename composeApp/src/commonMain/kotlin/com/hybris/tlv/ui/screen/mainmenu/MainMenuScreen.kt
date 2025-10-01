package com.hybris.tlv.ui.screen.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.hybris.tlv.platform.Property
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.screen.mainmenu.content.HabitabilityContent
import com.hybris.tlv.ui.screen.mainmenu.content.HostDefinitionContent
import com.hybris.tlv.ui.screen.mainmenu.content.LearnContent
import com.hybris.tlv.ui.screen.mainmenu.content.MainMenuContent
import com.hybris.tlv.ui.screen.mainmenu.content.PlanetDefinitionContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.BottomBar
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MainMenuScreen(store: Store<MainMenuState, MainMenuAction>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent
    val isMenu = currentContent == Content.MAIN_MENU || currentContent == Content.LEARN_MENU

    Screen(
        modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        bottomBar = {
            if (isMenu) BottomBar(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_BOTTOM_BAR),
                onCreditsClick = { store.send(action = MainMenuAction.Credits) },
                developerCornerUri = storeState.developerCorner,
                supportUri = storeState.support
            )
        }
    ) {
        when (currentContent) {
            Content.MAIN_MENU -> MainMenuContent(store = store)
            Content.LEARN_MENU -> LearnContent(store = store)
            Content.HOST_DEFINITION -> HostDefinitionContent(store = store)
            Content.PLANET_DEFINITION -> PlanetDefinitionContent(store = store)
            Content.HABITABILITY -> HabitabilityContent(store = store)
        }
    }
}

@Preview
@Composable
private fun MainMenuLoading() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    AppTheme {
        MainMenuScreen(
            store = getStore(
                initialState = MainMenuState(
                    loading = true,
                    featureLearn = true,
                    featureScores = true,
                    featureAchievements = true,
                    featureStellarExplorer = true,
                    featureNewGame = true,
                    featureTutorial = true,
                    developerCorner = "Developer Corner",
                    support = "Support",
                    formula = "Formula",
                    currentContent = Content.MAIN_MENU,
                    ongoingGameSession = false,
                    learningsMap = emptyMap(),
                    newGameDialog = false
                )
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuAll() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = Property.APP_NAME
            ),
            Translation(
                key = "main_menu_screen__new_game",
                value = "New Game"
            ),
            Translation(
                key = "main_menu_screen__learn",
                value = "Learn"
            ),
            Translation(
                key = "main_menu_screen__scores",
                value = "Scores"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    AppTheme {
        MainMenuScreen(
            store = getStore(
                initialState = MainMenuState(
                    loading = false,
                    featureLearn = true,
                    featureScores = true,
                    featureAchievements = true,
                    featureStellarExplorer = true,
                    featureNewGame = true,
                    featureTutorial = true,
                    developerCorner = "Developer Corner",
                    support = "Support",
                    formula = "Formula",
                    currentContent = Content.MAIN_MENU,
                    ongoingGameSession = false,
                    learningsMap = emptyMap(),
                    newGameDialog = false
                )
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuContinue() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = Property.APP_NAME
            ),
            Translation(
                key = "main_menu_screen__new_game",
                value = "New Game"
            ),
            Translation(
                key = "main_menu_screen__continue",
                value = "Continue"
            ),
            Translation(
                key = "main_menu_screen__learn",
                value = "Learn"
            ),
            Translation(
                key = "main_menu_screen__scores",
                value = "Scores"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    AppTheme {
        MainMenuScreen(
            store = getStore(
                initialState = MainMenuState(
                    loading = false,
                    featureLearn = true,
                    featureScores = true,
                    featureAchievements = true,
                    featureStellarExplorer = true,
                    featureNewGame = true,
                    featureTutorial = true,
                    developerCorner = "Developer Corner",
                    support = "Support",
                    formula = "Formula",
                    currentContent = Content.MAIN_MENU,
                    ongoingGameSession = true,
                    learningsMap = emptyMap(),
                    newGameDialog = false
                )
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuNoFeatures() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = Property.APP_NAME
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    AppTheme {
        MainMenuScreen(
            store = getStore(
                initialState = MainMenuState(
                    loading = false,
                    featureLearn = false,
                    featureScores = false,
                    featureAchievements = false,
                    featureStellarExplorer = false,
                    featureNewGame = false,
                    featureTutorial = false,
                    developerCorner = "Developer Corner",
                    support = "Support",
                    formula = "Formula",
                    currentContent = Content.MAIN_MENU,
                    ongoingGameSession = false,
                    learningsMap = emptyMap(),
                    newGameDialog = false
                )
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuLearn() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = Property.APP_NAME
            ),
            Translation(
                key = "main_menu_screen__stellar_explorer",
                value = "Stellar Explorer"
            ),
            Translation(
                key = "main_menu_screen__host_definition",
                value = "Star Definition"
            ),
            Translation(
                key = "main_menu_screen__definition_example",
                value = "Example"
            ),
            Translation(
                key = "main_menu_screen__definition_properties",
                value = "Properties"
            ),
            Translation(
                key = "main_menu_screen__definition_types",
                value = "Types"
            ),
            Translation(
                key = "main_menu_screen__planet_definition",
                value = "Planet Definition"
            ),
            Translation(
                key = "main_menu_screen__habitability",
                value = "Habitability Formula"
            ),
            Translation(
                key = "main_menu_screen__mechanics",
                value = "Tutorial"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    AppTheme {
        MainMenuScreen(
            store = getStore(
                initialState = MainMenuState(
                    loading = false,
                    featureLearn = true,
                    featureScores = true,
                    featureAchievements = true,
                    featureStellarExplorer = true,
                    featureNewGame = true,
                    featureTutorial = true,
                    developerCorner = "Developer Corner",
                    support = "Support",
                    formula = "Formula",
                    currentContent = Content.LEARN_MENU,
                    ongoingGameSession = false,
                    learningsMap = emptyMap(),
                    newGameDialog = false
                )
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuHostDefinition() {
    AppTheme {
        MainMenuScreen(
            store = getStore(
                initialState = MainMenuState(
                    loading = false,
                    featureLearn = true,
                    featureScores = true,
                    featureAchievements = true,
                    featureStellarExplorer = true,
                    featureNewGame = true,
                    featureTutorial = true,
                    developerCorner = "Developer Corner",
                    support = "Support",
                    formula = "Formula",
                    currentContent = Content.HOST_DEFINITION,
                    ongoingGameSession = false,
                    learningsMap = listOf(
                        Learning(
                            id = "Luminosity",
                            description = "Shine on you crazy diamond",
                            image = null,
                            type = LearningType.HOST_PROPERTY
                        ),
                        Learning(
                            id = "G",
                            description = "Our Sun",
                            image = "G",
                            type = LearningType.HOST_TYPE
                        ),
                        Learning(
                            id = "W",
                            description = "Wolf-Rayet",
                            image = "W",
                            type = LearningType.HOST_TYPE
                        ),
                    ).groupBy { it.type },
                    newGameDialog = false
                )
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuPlanetDefinition() {
    AppTheme {
        MainMenuScreen(
            store = getStore(
                initialState = MainMenuState(
                    loading = false,
                    featureLearn = true,
                    featureScores = true,
                    featureAchievements = true,
                    featureStellarExplorer = true,
                    featureNewGame = true,
                    featureTutorial = true,
                    developerCorner = "Developer Corner",
                    support = "Support",
                    formula = "Formula",
                    currentContent = Content.PLANET_DEFINITION,
                    ongoingGameSession = false,
                    learningsMap = listOf(
                        Learning(
                            id = "Mass",
                            description = "Very Biggus",
                            image = null,
                            type = LearningType.PLANET_PROPERTY
                        ),
                        Learning(
                            id = "Mars",
                            description = "The Red Planet",
                            image = "EARTH_LIKE_PLANET",
                            type = LearningType.PLANET_TYPE
                        ),
                        Learning(
                            id = "Mini Neptune",
                            description = "Mini-Me",
                            image = "MINI_NEPTUNE",
                            type = LearningType.PLANET_TYPE
                        ),
                    ).groupBy { it.type },
                    newGameDialog = false
                )
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuHabitability() {
    AppTheme {
        MainMenuScreen(
            store = getStore(
                initialState = MainMenuState(
                    loading = false,
                    featureLearn = true,
                    featureScores = true,
                    featureAchievements = true,
                    featureStellarExplorer = true,
                    featureNewGame = true,
                    featureTutorial = true,
                    developerCorner = "Developer Corner",
                    support = "Support",
                    formula = "Formula",
                    currentContent = Content.HABITABILITY,
                    ongoingGameSession = false,
                    learningsMap = listOf(
                        Learning(
                            id = "Roche",
                            description = "The Roche limit",
                            image = null,
                            type = LearningType.FORMULA
                        ),
                        Learning(
                            id = "CHZ",
                            description = "The Circumstellar Habitable Zone (CHZ) is the region around a star where liquid water could exist on a planet's surface.\n" +
                                    "I use the Kopparapu model with a flat plateau of 1.0 across the entire conservative zone and then a smooth down slope through the optimistic zone, as a simple gradient peaked at the center unfairly penalizes planets like Earth, which is perfectly habitable but located near the inner edge of the Sun's conservative zone.\n" +
                                    "The host star's temperature is used to calculate the fluxes with the model's coefficients. If it is not available, the Kasting simple luminosity model is used instead but with a smaller weight.",
                            image = null,
                            type = LearningType.FORMULA
                        ),
                    ).groupBy { it.type },
                    newGameDialog = false
                )
            )
        )
    }
}
