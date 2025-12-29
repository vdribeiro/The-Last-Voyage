package com.hybris.tlv

import kotlin.test.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
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
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.command.Command
import com.hybris.tlv.command.receiveCommand
import com.hybris.tlv.command.sendCommand
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.dependency.Dependency
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.TestEngine
import com.hybris.tlv.lifecycle.lifecycleOwner
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.screen.Store
import com.hybris.tlv.screen.StoreFactory
import com.hybris.tlv.theme.AppTheme
import com.hybris.tlv.usecase.UseCases

/**
 * Abstract class for defining test cases.
 * It provides a hermetic testing environment for Unit tests [runUnitTest] and UI tests [runUITest]
 * and handles the dependencies, coroutine scopes, and navigation simulation.
 */
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTestApi::class)
internal abstract class TestCase {

    /**
     * Dependency index for test cases with in memory Database, Mock Http Engine and silent audio.
     */
    private val dependency: Dependency by lazy {
        Dependency(
            sqlDriver = createSqlDriver(inMemory = true),
            httpEngine = TestEngine.mock,
            audioPlayer = AudioPlayer(),
        )
    }
    /**
     * Access point to config, derived from the test-specific [dependency].
     */
    protected val config: ConfigManager by lazy { dependency.config }
    /**
     * Access point to the use cases, derived from the test-specific [dependency].
     */
    protected val useCases: UseCases by lazy { dependency.useCases }
    /**
     * Factory used to create Stores using the test-specific [dependency].
     */
    protected val storeFactory = StoreFactory(dependency = dependency)

    /**
     * Feature flags for testing.
     */
    private val testFlag = Flag(
        reset = true,
        http = true,
        archive = true,
        music = false
    )

    /**
     * List with the simulated navigation backstack.
     */
    private val screens: MutableList<Screen> = mutableListOf()

    /**
     * Background loop that listens to the global [Command] channel.
     */
    private suspend fun receiveCommands() {
        receiveCommand { command ->
            when (command) {
                is Command.Navigate -> screens.addOrTruncate(element = command.screen)
                Command.Back -> screens.removeLastOrNull()
                Command.ToggleAudio -> {}
            }
        }
    }

    /**
     * Helper to manage the navigation backstack list.
     * If the [element] does not exist, then it is appended to the list, otherwise all screens after it are cleared.
     */
    private fun MutableList<Screen>.addOrTruncate(element: Screen) {
        val index = indexOfFirst { it::class == element::class }
        if (index == -1) add(element = element) else {
            if (index + 1 < size) subList(fromIndex = index + 1, toIndex = size).clear()
        }
    }

    /**
     * Simulates a navigation event.
     */
    protected fun navigate(screen: Screen): Boolean =
        sendCommand(command = Command.Navigate(screen = screen))

    /**
     * Compares the navigation backstack with the given screen [list].
     */
    protected fun assertNavigation(list: List<Screen>) =
        assertEquals(expected = list.map { it::class }, actual = screens.map { it::class })

    /**
     * Resets all local data.
     */
    protected suspend fun reset() = dependency.useCases.sync.reset()

    /**
     * Sets feature flags.
     */
    protected fun setFlag(flag: (Flag) -> Flag = { it }) {
        TLV.flag = flag(testFlag)
    }

    /**
     * Executes a unit test.
     * Prepares the environment by resetting local data and clearing the navigation stack, then launches a job to process commands.
     */
    protected fun runUnitTest(mockNavigation: Boolean = true, block: suspend TestScope.() -> Unit) {
        runTest {
            val testDispatcher = UnconfinedTestDispatcher(scheduler = testScheduler)
            Dispatcher.setTestDispatcher(dispatcher = testDispatcher)
            try {
                setFlag()
                reset()
                screens.clear()
                if (mockNavigation) backgroundScope.launch(context = testDispatcher) { receiveCommands() }
                block()
                testScheduler.advanceUntilIdle()
            } finally {
                Dispatcher.reset()
            }
        }
    }

    /**
     * Executes a UI test.
     * Prepares the environment by resetting local data and clearing the navigation stack, then launches a job to process commands.
     */
    protected fun runUITest(mockNavigation: Boolean = true, block: suspend ComposeUiTest.() -> Unit) {
        runComposeUiTest {
            val testDispatcher = UnconfinedTestDispatcher()
            Dispatcher.setTestDispatcher(dispatcher = testDispatcher)
            val scope = CoroutineScope(context = testDispatcher)
            try {
                setFlag()
                reset()
                screens.clear()
                if (mockNavigation) scope.launch { receiveCommands() }
                block()
                waitForIdle()
            } finally {
                scope.cancel()
                Dispatcher.reset()
            }
        }
    }

    /**
     * Render a Composable within the test harness.
     * This wraps the provided [content] with a [LocalLifecycleOwner] and [AppTheme] for correct styling.
     */
    protected suspend fun ComposeUiTest.setScreen(
        vararg values: ProvidedValue<*>,
        content: @Composable () -> Unit
    ) {
        val lifecycleOwner = withContext(context = Dispatchers.Main) { lifecycleOwner }
        setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner, *values) {
                AppTheme {
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
