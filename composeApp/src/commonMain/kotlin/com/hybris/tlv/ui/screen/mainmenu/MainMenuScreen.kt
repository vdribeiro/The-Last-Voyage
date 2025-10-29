package com.hybris.tlv.ui.screen.mainmenu

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.hybris.tlv.platform.isDesktop
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.bottombar.MainNavigation
import com.hybris.tlv.ui.theme.component.bottombar.Snackbar
import com.hybris.tlv.ui.theme.component.card.PlanetCard
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.dialog.Dialog
import com.hybris.tlv.ui.theme.component.image.AppLogo
import com.hybris.tlv.ui.theme.component.list.LazyColumnWithScrollBar
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.space.formula.spectralTypeToImage
import com.hybris.tlv.usecase.space.formula.toImage
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.model.Score
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation

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
            if (isMenu) MainNavigation(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_BOTTOM_BAR),
                onCreditsClick = { store.send(action = MainMenuAction.Credits) },
                developerCornerUri = storeState.developerCorner,
                supportUri = storeState.support
            )
        },
        snackbarHost = {
            if (isDesktop) Snackbar(messages = emptyList())
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

@Composable
private fun MainMenuContent(store: Store<MainMenuState, MainMenuAction>) {
    val storeState by store.stateFlow.collectAsState()
    val translationVersion by TranslationCache.updateFlow.collectAsState()
    val appNameTranslation = remember(key1 = translationVersion) { getTranslation(key = "app_name") }
    val tutorialTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__new_game_tutorial") }
    val newGameTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__new_game") }
    val continueTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__continue") }
    val learnTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__learn") }
    val scoresTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__scores") }
    val achievementsTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__achievements") }

    val typography = LocalTypography.current

    if (storeState.newGameDialog) {
        Dialog(
            title = tutorialTranslation,
            onConfirm = { store.send(action = MainMenuAction.YesNewGameDialog) },
            onDismiss = { store.send(action = MainMenuAction.NoNewGameDialog) },
            onDismissRequest = { store.send(action = MainMenuAction.HideNewGameDialog) },
        )
    }

    LazyColumn(
        modifier = Modifier
            .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            AppLogo(
                modifier = Modifier.padding(bottom = 16.dp),
                showBackground = false,
                text = appNameTranslation
            )
        }
        if (storeState.featureNewGame) {
            item {
                Text(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_NEW_GAME)
                        .clickable { store.send(action = MainMenuAction.NewGame) },
                    text = newGameTranslation,
                    style = typography.headlineMedium,
                )
            }
            if (storeState.ongoingGameSession) {
                item {
                    Text(
                        modifier = Modifier
                            .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_CONTINUE)
                            .clickable { store.send(action = MainMenuAction.Next) },
                        text = continueTranslation,
                        style = typography.headlineMedium,
                    )
                }
            }
        }
        if (storeState.featureLearn) {
            item {
                Text(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN)
                        .clickable { store.send(action = MainMenuAction.Learn) },
                    text = learnTranslation,
                    style = typography.headlineMedium,
                )
            }
        }
        if (storeState.featureScores) {
            item {
                Text(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SCORES)
                        .clickable { store.send(action = MainMenuAction.Scores) },
                    text = scoresTranslation,
                    style = typography.headlineMedium,
                )
            }
        }
        if (storeState.featureAchievements) {
            item {
                Text(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_ACHIEVEMENTS)
                        .clickable { store.send(action = MainMenuAction.Achievements) },
                    text = achievementsTranslation,
                    style = typography.headlineMedium,
                )
            }
        }
    }
}

@Composable
private fun LearnContent(store: Store<MainMenuState, MainMenuAction>) {
    val storeState by store.stateFlow.collectAsState()
    val translationVersion by TranslationCache.updateFlow.collectAsState()
    val appNameTranslation = remember(key1 = translationVersion) { getTranslation(key = "app_name") }
    val stellarExplorerTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__stellar_explorer") }
    val hostDefinitionTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__host_definition") }
    val planetDefinitionTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__planet_definition") }
    val habitabilityTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__habitability") }
    val mechanicsTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__mechanics") }

    val typography = LocalTypography.current

    LazyColumn(
        modifier = Modifier
            .testTag(tag = MAIN_MENU_SCREEN_LEARN_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            AppLogo(
                modifier = Modifier.padding(bottom = 16.dp),
                showBackground = false,
                text = appNameTranslation
            )
        }
        if (storeState.featureStellarExplorer) {
            item {
                Text(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_LEARN_CONTENT_STELLAR_EXPLORER)
                        .clickable { store.send(action = MainMenuAction.StellarExplorer) },
                    text = stellarExplorerTranslation,
                    style = typography.headlineMedium,
                )
            }
        }
        item {
            Text(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_LEARN_CONTENT_HOST_DEFINITION)
                    .clickable { store.send(action = MainMenuAction.HostDefinition) },
                text = hostDefinitionTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_LEARN_CONTENT_PLANET_DEFINITION)
                    .clickable { store.send(action = MainMenuAction.PlanetDefinition) },
                text = planetDefinitionTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_LEARN_CONTENT_HABITABILITY)
                    .clickable { store.send(action = MainMenuAction.Habitability) },
                text = habitabilityTranslation,
                style = typography.headlineMedium,
            )
        }
        if (storeState.featureTutorial) {
            item {
                Text(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_LEARN_CONTENT_MECHANICS)
                        .clickable { store.send(action = MainMenuAction.Mechanics) },
                    text = mechanicsTranslation,
                    style = typography.headlineMedium,
                )
            }
        }
    }
}

@Composable
private fun HostDefinitionContent(store: Store<MainMenuState, MainMenuAction>) {
    val storeState by store.stateFlow.collectAsState()
    val stellarHostProperties = storeState.learningsMap[LearningType.HOST_PROPERTY].orEmpty()
    val stellarHosts = storeState.learningsMap[LearningType.HOST_TYPE].orEmpty()
    val stellarHost = remember {
        StellarHost(
            id = "Valar",
            name = "Valar",
            systemName = "Arda",
            spectralType = "G3V",
            effectiveTemperature = 5678.0,
            radius = 1.0,
            mass = 7.0,
            metallicity = 3.0,
            luminosity = 9.0,
            gravity = 2.0,
            age = 1.2,
            density = 2.1,
            rotationalVelocity = 10.0,
            rotationalPeriod = 50.0,
            distance = 9000.0,
            ra = 901.2,
            dec = 345.6,
        ).apply {
            planets.add(
                element = Planet(
                    id = "ME",
                    name = "ME",
                    stellarHostId = "Valar",
                    status = PlanetStatus.FALSE,
                    orbitalPeriod = null,
                    orbitAxis = null,
                    radius = null,
                    mass = null,
                    density = null,
                    eccentricity = null,
                    insolationFlux = null,
                    equilibriumTemperature = null,
                    occultationDepth = null,
                    inclination = null,
                    obliquity = null,
                )
            )
        }
    }
    val translationVersion by TranslationCache.updateFlow.collectAsState()
    val exampleTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__definition_example") }
    val propertiesTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__definition_properties") }
    val typesTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__definition_types") }

    val typography = LocalTypography.current

    LazyColumnWithScrollBar(
        modifier = Modifier
            .testTag(tag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        item {
            Text(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_EXAMPLE),
                text = exampleTranslation,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        item {
            StellarHostCard(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_EXAMPLE_STELLAR_HOST),
                name = stellarHost.name,
                systemName = stellarHost.systemName,
                planetCount = stellarHost.planets.size,
                spectralType = stellarHost.spectralType,
                spectralImage = stellarHost.spectralType.spectralTypeToImage(),
                effectiveTemperature = stellarHost.effectiveTemperature,
                radius = stellarHost.radius,
                mass = stellarHost.mass,
                metallicity = stellarHost.metallicity,
                luminosity = stellarHost.luminosity,
                gravity = stellarHost.gravity,
                age = stellarHost.age,
                density = stellarHost.density,
                rotationalVelocity = stellarHost.rotationalVelocity,
                rotationalPeriod = stellarHost.rotationalPeriod,
                distance = stellarHost.distance,
                ra = stellarHost.ra,
                dec = stellarHost.dec,
            )
        }
        item {
            Text(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_PROPERTIES),
                text = propertiesTranslation,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(items = stellarHostProperties, key = { it.id }) { property ->
            PropertyCard(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_PROPERTIES_SIMPLE),
                name = getTranslation(key = property.id),
                description = getTranslation(key = property.description),
            )
        }
        item {
            Text(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_TYPES),
                text = typesTranslation,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(items = stellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_TYPES_STELLAR_HOST),
                name = getTranslation(key = stellarHost.id),
                description = stellarHost.description,
                spectralImage = stellarHost.image.spectralTypeToImage(),
            )
        }
    }
}

@Composable
private fun PlanetDefinitionContent(store: Store<MainMenuState, MainMenuAction>) {
    val storeState by store.stateFlow.collectAsState()
    val planetProperties = storeState.learningsMap[LearningType.PLANET_PROPERTY].orEmpty()
    val planets = storeState.learningsMap[LearningType.PLANET_TYPE].orEmpty()
    val planet = remember {
        Planet(
            id = "Edoras",
            name = "Edoras",
            stellarHostId = "Valar",
            status = PlanetStatus.CANDIDATE,
            orbitalPeriod = 123.0,
            orbitAxis = 1.2,
            radius = 5.1,
            mass = 2.3,
            density = 3.2,
            eccentricity = 0.5,
            insolationFlux = 2.1,
            equilibriumTemperature = 666.9,
            occultationDepth = 0.01,
            inclination = 1.8,
            obliquity = 50.0,
        ).apply {
            score = Score(
                habitabilityScore = 1.0,
                confidenceScore = 1.0,
                planetType = PlanetType.SUPERHABITABLE_PLANET,
                rocheScore = null,
                habitableZoneKopparapuScore = null,
                habitableZoneKastingScore = null,
                planetRadiusScore = null,
                planetMassScore = null,
                planetTelluricityScore = null,
                planetEccentricityScore = null,
                planetTemperatureScore = null,
                planetObliquityScore = null,
                planetEsiScore = null,
                stellarSpectralTypeScore = null,
                stellarMassScore = null,
                stellarAgeScore = null,
                stellarActivityScore = null,
                stellarRotationalPeriodScore = null,
                stellarGravityScore = null,
                stellarMetallicityScore = null,
                stellarEffectiveTemperatureScore = null,
                planetProtectionScore = null,
                planetTidalLockingScore = null
            )
        }
    }
    val translationVersion by TranslationCache.updateFlow.collectAsState()
    val exampleTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__definition_example") }
    val propertiesTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__definition_properties") }
    val typesTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__definition_types") }

    val typography = LocalTypography.current

    LazyColumnWithScrollBar(
        modifier = Modifier
            .testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        item {
            Text(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_EXAMPLE),
                text = exampleTranslation,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        item {
            PlanetCard(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_EXAMPLE_PLANET),
                name = planet.name,
                status = planet.status.displayName,
                orbitalPeriod = planet.orbitalPeriod,
                orbitAxis = planet.orbitAxis,
                radius = planet.radius,
                mass = planet.mass,
                density = planet.density,
                eccentricity = planet.eccentricity,
                insolationFlux = planet.insolationFlux,
                equilibriumTemperature = planet.equilibriumTemperature,
                occultationDepth = planet.occultationDepth,
                inclination = planet.inclination,
                obliquity = planet.obliquity,
                type = planet.score?.planetType?.displayName,
                image = planet.score?.planetType.toImage()
            )
        }
        item {
            Text(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_PROPERTIES),
                text = propertiesTranslation,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(items = planetProperties, key = { it.id }) { property ->
            PropertyCard(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_PROPERTIES_SIMPLE),
                name = getTranslation(key = property.id),
                description = getTranslation(key = property.description),
            )
        }
        item {
            Text(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_TYPES),
                text = typesTranslation,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(items = planets, key = { it.id }) { planet ->
            PlanetCard(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_TYPES_PLANET),
                name = getTranslation(key = planet.id),
                description = planet.description,
                image = PlanetType.fromValue(value = planet.image.orEmpty()).toImage()
            )
        }
    }
}

@Composable
private fun HabitabilityContent(store: Store<MainMenuState, MainMenuAction>) {
    val storeState by store.stateFlow.collectAsState()
    val formula = storeState.learningsMap[LearningType.FORMULA].orEmpty()
    val uriHandler = LocalUriHandler.current
    val translationVersion by TranslationCache.updateFlow.collectAsState()
    val formulaTranslation = remember(key1 = translationVersion) { getTranslation(key = "formula") }

    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    LazyColumnWithScrollBar(
        modifier = Modifier
            .testTag(tag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = formula, key = { it.id }) { property ->
            PropertyCard(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_SIMPLE),
                name = getTranslation(key = property.id),
                description = getTranslation(key = property.description),
            )
        }
        item {
            Text(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_FORMULA)
                    .clickable { uriHandler.openUri(uri = storeState.formula) },
                text = formulaTranslation,
                style = typography.headlineSmall.copy(
                    color = colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
            )
        }
    }
}

@Preview
@Composable
private fun MainMenuLoadingPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
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

@Preview
@Composable
private fun MainMenuAllPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = "The Last Voyage"
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

@Preview
@Composable
private fun MainMenuContinuePreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = "The Last Voyage"
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

@Preview
@Composable
private fun MainMenuNoFeaturesPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = "The Last Voyage"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
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

@Preview
@Composable
private fun MainMenuLearnPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = "The Last Voyage"
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

@Preview
@Composable
private fun MainMenuHostDefinitionPreview() = AppTheme {
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

@Preview
@Composable
private fun MainMenuPlanetDefinitionPreview() = AppTheme {
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

@Preview
@Composable
private fun MainMenuHabitabilityPreview() = AppTheme {
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
