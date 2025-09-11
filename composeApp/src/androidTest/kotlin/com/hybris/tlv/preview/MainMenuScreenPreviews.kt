package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import com.hybris.tlv.App
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.mainmenu.Content
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun MainMenuNull() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState()
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuAll() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            featureFeedback = true,
            featureSoon = true,
            featureLearn = true,
            featureScores = true,
            featureAchievements = true,
            featureStellarExplorer = true,
            featureNewGame = true,
            loading = false,
            currentContent = Content.MAIN_MENU,
            ongoingGameSession = false,
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuLoading() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            featureFeedback = true,
            featureSoon = true,
            featureLearn = true,
            featureScores = true,
            featureAchievements = true,
            featureStellarExplorer = true,
            featureNewGame = true,
            loading = true,
            currentContent = Content.MAIN_MENU,
            ongoingGameSession = true,
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuContinue() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            featureFeedback = true,
            featureSoon = true,
            featureLearn = true,
            featureScores = true,
            featureAchievements = true,
            featureStellarExplorer = true,
            featureNewGame = true,
            loading = false,
            currentContent = Content.MAIN_MENU,
            ongoingGameSession = true,
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuNoNewGame() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            featureFeedback = true,
            featureSoon = true,
            featureLearn = true,
            featureScores = true,
            featureAchievements = true,
            featureStellarExplorer = true,
            featureNewGame = false,
            loading = false,
            currentContent = Content.MAIN_MENU,
            ongoingGameSession = true,
            learningsMap = null
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuNoFeedbackAndSoon() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            featureFeedback = false,
            featureSoon = false,
            featureLearn = true,
            featureScores = true,
            featureAchievements = true,
            featureStellarExplorer = true,
            featureNewGame = true,
            loading = false,
            currentContent = Content.MAIN_MENU,
            ongoingGameSession = false,
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuNoScoresAndAchievements() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            featureFeedback = true,
            featureSoon = true,
            featureLearn = true,
            featureScores = false,
            featureAchievements = false,
            featureStellarExplorer = true,
            featureNewGame = true,
            loading = false,
            currentContent = Content.MAIN_MENU,
            ongoingGameSession = false,
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuNoLearnAndStellarExplorer() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            featureFeedback = true,
            featureSoon = true,
            featureLearn = false,
            featureScores = true,
            featureAchievements = true,
            featureStellarExplorer = false,
            featureNewGame = true,
            loading = false,
            currentContent = Content.MAIN_MENU,
            ongoingGameSession = false,
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuLearn() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            loading = false,
            currentContent = Content.LEARN_MENU,
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuHostDefinition() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            loading = false,
            currentContent = Content.HOST_DEFINITION,
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
            ).groupBy { it.type }
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuPlanetDefinition() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            loading = false,
            currentContent = Content.PLANET_DEFINITION,
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
            ).groupBy { it.type }
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun MainMenuHabitability() {
    val navigation = navigation(
        screen = Screen.MAIN_MENU,
        state = MainMenuState(
            loading = false,
            currentContent = Content.HABITABILITY,
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
            ).groupBy { it.type }
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}
