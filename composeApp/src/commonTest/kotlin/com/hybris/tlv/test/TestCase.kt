package com.hybris.tlv.test

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.rememberNavigationEventDispatcherOwner
import com.hybris.tlv.Dependency
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.MockLogger
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.database.createMockSqlDriver
import com.hybris.tlv.data.http.createMockHttpEngine
import com.hybris.tlv.domain.flag.FeatureFlags
import com.hybris.tlv.domain.flag.Flags
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.ui.lifecycle.lifecycleOwner
import com.hybris.tlv.ui.navigation.MockNavigation
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.StoreFactory
import com.hybris.tlv.ui.theme.AppTheme

/**
 * Abstract class for defining test cases.
 * It provides a hermetic testing environment for Unit tests [runUnitTest] and UI tests [runUITest]
 * and handles the dependencies, coroutine scopes, and navigation simulation.
 */
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTestApi::class)
internal abstract class TestCase: PlatformTestCase() {

    /**
     * Feature flags for testing.
     */
    private val testFlags = Flags(
        devMode = true,
        reset = true,
        http = true,
        archive = true,
        music = false,
        engines = true
    )

    /**
     * Simulated navigation.
     */
    private val navigation: MockNavigation by lazy { MockNavigation() }

    /**
     * Dependency index for test cases with in memory Database and Mock Http Engine.
     */
    protected val dependency: LazyData<Dependency> = LazyData {
        Dependency(
            sqlDriver = createMockSqlDriver(),
            httpEngine = createMockHttpEngine(),
        )
    }

    /**
     * Factory used to create Stores using the test-specific dependency.
     */
    protected val storeFactory: LazyData<StoreFactory> = LazyData {
        StoreFactory(
            config = dependency.get().config,
            useCases = dependency.get().useCases
        )
    }

    /**
     * Simulates a navigation event.
     */
    protected fun navigate(screen: Screen): Boolean = navigation.navigate(screen = screen)

    /**
     * Compares the navigation backstack with the given screen [list].
     */
    protected fun assertNavigation(list: List<Screen>) = navigation.assertNavigation(list = list)

    /**
     * Resets all data.
     */
    protected suspend fun resetData() {
        dependency.get().useCases.sync.reset()
        TranslationCache.set(translations = emptyList())
    }

    /**
     * Sets the dispatcher for coroutines.
     */
    private fun setDispatcher(dispatcher: CoroutineDispatcher) {
        Dispatcher.Main = dispatcher
        Dispatcher.Default = dispatcher
        Dispatcher.IO = dispatcher
    }

    /**
     * Sets up the test environment.
     * Resets feature flags, sets the telemetry engine,
     * sets the coroutine dispatcher and main dispatcher with the given [dispatcher] (typically with a [UnconfinedTestDispatcher]),
     * resets local data, clears the navigation stack and finally starts the navigation simulation.
     */
    private suspend fun setup(scope: CoroutineScope?, dispatcher: CoroutineDispatcher) {
        FeatureFlags.set { testFlags }
        Telemetry.engine = MockLogger()
        setDispatcher(dispatcher = dispatcher)
        Dispatchers.setMain(dispatcher = dispatcher)
        resetData()
        navigation.clear()
        scope?.launch(context = dispatcher) { navigation.receiveCommands() }
    }

    /**
     * Resets the test environment.
     * Resets feature flags and telemetry engine,
     * sets coroutine dispatcher and main dispatcher with the original dispatchers for the general test environment,
     * resets local data, clears the navigation stack and finally stops the navigation simulation.
     */
    private suspend fun reset(scope: CoroutineScope?) {
        FeatureFlags.set { testFlags }
        Telemetry.engine = null
        setDispatcher(dispatcher = Dispatchers.Unconfined)
        Dispatchers.resetMain()
        resetData()
        navigation.clear()
        scope?.cancel()
    }

    /**
     * Executes a unit test.
     * Prepares the environment by calling [setup], then executes the given [block]. Finally clears with [reset].
     */
    protected fun runUnitTest(block: suspend TestScope.(TestDispatcher) -> Unit) {
        runTest {
            val testDispatcher = UnconfinedTestDispatcher(scheduler = testScheduler)
            val scope = backgroundScope
            setup(scope = scope, dispatcher = testDispatcher)
            try {
                block(testDispatcher)
                testScheduler.advanceUntilIdle()
            } finally {
                reset(scope = scope)
            }
        }
    }

    /**
     * Executes a UI test.
     * Prepares the environment by calling [setup], then executes the given [block]. Finally clears with [reset].
     */
    protected fun runUITest(mockNavigation: Boolean = true, block: suspend ComposeUiTest.(TestDispatcher) -> Unit) {
        runComposeUiTest {
            val testDispatcher = UnconfinedTestDispatcher()
            val scope = if (mockNavigation) CoroutineScope(context = testDispatcher) else null
            setup(scope = scope, dispatcher = testDispatcher)
            try {
                block(testDispatcher)
                waitForIdle()
            } finally {
                reset(scope = scope)
            }
        }
    }

    /**
     * Render a Composable within the test harness.
     */
    protected fun ComposeUiTest.setUI(
        compositionValues: List<ProvidedValue<*>> = emptyList(),
        content: @Composable () -> Unit
    ) {
        setContent {
            val compositionValues = listOf(
                LocalLifecycleOwner provides lifecycleOwner,
                LocalNavigationEventDispatcherOwner provides rememberNavigationEventDispatcherOwner()
            ) + compositionValues
            CompositionLocalProvider(*compositionValues.toTypedArray()) {
                AppTheme {
                    content()
                }
            }
        }
        waitForIdle()
    }
}
