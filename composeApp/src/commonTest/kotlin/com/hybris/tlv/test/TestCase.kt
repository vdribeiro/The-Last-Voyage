package com.hybris.tlv.test

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hybris.tlv.Dependency
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.MockLogger
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.database.createMockSqlDriver
import com.hybris.tlv.data.http.createMockHttpEngine
import com.hybris.tlv.domain.flag.FeatureFlags
import com.hybris.tlv.domain.flag.Flags
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.ui.App
import com.hybris.tlv.ui.lifecycle.lifecycleOwner
import com.hybris.tlv.ui.navigation.MockNavigation
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.screen.StoreFactory

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
        music = false
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
            audioPlayer = AudioPlayer()
        )
    }

    /**
     * Factory used to create Stores using the test-specific dependency.
     */
    protected suspend fun getStoreFactory(): StoreFactory = StoreFactory(
        config = dependency.get().config,
        useCases = dependency.get().useCases
    )

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
        Dispatchers.setMain(dispatcher = dispatcher)
    }

    /**
     * Resets the dispatcher for coroutines.
     */
    private fun resetDispatcher() {
        setDispatcher(dispatcher = Dispatchers.Unconfined)
        Dispatchers.resetMain()
    }

    /**
     * Executes a unit test.
     * Prepares the environment by resetting local data and clearing the navigation stack, then launches a job to process commands.
     */
    protected fun runUnitTest(block: suspend TestScope.() -> Unit) {
        FeatureFlags.set { testFlags }
        Telemetry.engine = MockLogger()
        runTest {
            val testDispatcher = UnconfinedTestDispatcher(scheduler = testScheduler)
            setDispatcher(dispatcher = testDispatcher)
            try {
                resetData()
                navigation.clear()
                backgroundScope.launch(context = testDispatcher) { navigation.receiveCommands() }
                block()
                testScheduler.advanceUntilIdle()
            } finally {
                resetDispatcher()
            }
        }
    }

    /**
     * Executes a UI test.
     * Prepares the environment by resetting local data and clearing the navigation stack, then launches a job to process commands.
     */
    protected fun runUITest(mockNavigation: Boolean = true, block: suspend ComposeUiTest.() -> Unit) {
        FeatureFlags.set { testFlags }
        Telemetry.engine = MockLogger()
        runComposeUiTest {
            val testDispatcher = UnconfinedTestDispatcher()
            setDispatcher(dispatcher = testDispatcher)
            val scope = if (mockNavigation) CoroutineScope(context = testDispatcher) else null
            try {
                resetData()
                navigation.clear()
                scope?.launch { navigation.receiveCommands() }
                block()
                waitForIdle()
            } finally {
                scope?.cancel()
                resetDispatcher()
            }
        }
    }

    /**
     * Render a Composable within the test harness.
     */
    protected fun ComposeUiTest.setUI(
        vararg values: ProvidedValue<*>,
        content: @Composable () -> Unit
    ) {
        setContent {
            CompositionLocalProvider(value = LocalLifecycleOwner provides lifecycleOwner) {
                App(*values) {
                    content()
                }
            }
        }
        waitForIdle()
    }

    /**
     * Syntactic sugar for store.stateFlow.value.
     */
    protected val <State, Action> Store<State, Action>.state: State get() = stateFlow.value

    /**
     * Verify the number of items in a collection.
     */
    protected fun SemanticsNodeInteraction.count(count: Int): SemanticsNodeInteraction =
        if (runCatching { onChildren().assertCountEquals(expectedSize = count) }.isSuccess) this else
            assert(matcher = SemanticsMatcher(description = "Has $count items") { node ->
                val collectionInfo = node.config.getOrNull(key = SemanticsProperties.CollectionInfo)
                collectionInfo != null && (collectionInfo.rowCount == count || collectionInfo.columnCount == count)
            })
}
