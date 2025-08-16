package com.hybris.tlv.preview

//@Preview
//@Composable
//private fun NewGameStartScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.NEW_GAME,
//            state = NewGameState(
//                currentContent = NewGameContent.START,
//                selectedCatastrophe = catastrophes.random()
//            )
//        )
//    }
//}

//
//@Composable
//private fun Screen(
//    screen: Screen,
//    state: Any?
//) =
//    Mock(
//        driver = AndroidSqliteDriver(
//            context = LocalContext.current,
//            schema = AppDatabase.Schema
//        )
//    ).Screen(
//        screen = screen,
//        state = state
//    )
//
//@Preview
//@Composable
//private fun ErrorScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.ERROR,
//            state = ErrorState()
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun SplashScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.SPLASH,
//            state = SplashState()
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun MainMenuScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.MAIN_MENU,
//            state = MainMenuState(
//                ongoingGameSession = false
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun MainMenuContinueScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.MAIN_MENU,
//            state = MainMenuState(
//                ongoingGameSession = true
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun NewGameShipScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.NEW_GAME,
//            state = NewGameState(
//                currentContent = NewGameContent.SHIP,
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun NewGameAdvancedScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.NEW_GAME,
//            state = NewGameState(
//                currentContent = NewGameContent.ADVANCED
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun NewGameStartScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.NEW_GAME,
//            state = NewGameState(
//                currentContent = NewGameContent.START,
//                selectedCatastrophe = catastrophes.random()
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun GameTravelScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.GAME,
//            state = GameState(
//                gameSession = gameSession,
//                currentContent = GameContent.TRAVEL,
//                nearStellarHosts = stellarHosts,
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun GameSystemScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.GAME,
//            state = GameState(
//                gameSession = gameSession,
//                currentContent = GameContent.SYSTEM,
//                stellarHosts = stellarHosts,
//                currentStellarHost = stellarHosts.first().apply {
//                    planets.addAll(planets.filter { it.stellarHostId == id })
//                    travelOutcome = TravelOutcome(
//                        integrity = 5,
//                        fuel = 10
//                    )
//                },
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun GameShipScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.GAME,
//            state = GameState(
//                gameSession = gameSession,
//                currentContent = GameContent.SHIP,
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun EventScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.EVENT,
//            state = EventState(
//                event = events.random()
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun GameOverMessageScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.GAME_OVER,
//            state = GameOverState(
//                currentContent = GameOverContent.MESSAGE,
//                gameSession = gameSession,
//                gameOverMessage = "Game over man! Game over!"
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun GameOverScoreScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.GAME_OVER,
//            state = GameOverState(
//                currentContent = GameOverContent.SCORE,
//                gameSession = gameSession,
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun ExploreScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.EXPLORE,
//            state = ExploreState(
//                currentContent = ExploreContent.MENU,
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun ExploreMechanicsScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.EXPLORE,
//            state = ExploreState(
//                currentContent = ExploreContent.MECHANICS,
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun ExploreHabitabilityScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.EXPLORE,
//            state = ExploreState(
//                currentContent = ExploreContent.HABITABILITY,
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun StellarExplorerScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.STELLAR_EXPLORER,
//            state = StellarExplorerState(
//                currentContent = StellarExplorerContent.LIST_HOSTS,
//                stellarHosts = stellarHosts
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun StellarExplorerDetailScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.STELLAR_EXPLORER,
//            state = StellarExplorerState(
//                currentContent = StellarExplorerContent.DETAIL_HOSTS,
//                selectedStellarHost = stellarHosts.first().apply {
//                    planets.addAll(elements = planets.filter { it.stellarHostId == id })
//                }
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun ScoreScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.SCORES,
//            state = ScoreState(
//                scores = listOf(
//                    gameSession.copy(id = generateUuid(), score = 100.0),
//                    gameSession.copy(id = generateUuid(), score = 50.0),
//                    gameSession.copy(id = generateUuid(), score = 150.0),
//                    gameSession.copy(id = generateUuid(), score = 1000.0)
//                )
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun AchievementScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.ACHIEVEMENTS,
//            state = AchievementState(
//                achievements = achievements
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun CreditsScreenPreview() {
//    AppTheme {
//        Screen(
//            screen = Screen.ACHIEVEMENTS,
//            state = CreditsState(
//                credits = credits
//            )
//        )
//    }
//}
