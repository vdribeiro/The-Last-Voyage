package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import com.hybris.tlv.getStore
import com.hybris.tlv.translations
import com.hybris.tlv.ui.screen.mainmenu.Content
import com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.translation.TranslationCache
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun MainMenuLoading() {
    TranslationCache.set(translations = translations)
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
    TranslationCache.set(translations = translations)
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
    TranslationCache.set(translations = translations)
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
private fun MainMenuNewGameDialog() {
    TranslationCache.set(translations = translations)
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
                    newGameDialog = true
                )
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuNoFeatures() {
    TranslationCache.set(translations = translations)
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
    TranslationCache.set(translations = translations)
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
    TranslationCache.set(translations = translations)
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
    TranslationCache.set(translations = translations)
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
    TranslationCache.set(translations = translations)
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
