package com.hybris.tlv

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
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.command.Command
import com.hybris.tlv.command.receiveCommand
import com.hybris.tlv.command.sendCommand
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.dependency.Dependency
import com.hybris.tlv.http.TestEngine
import com.hybris.tlv.lifecycle.TestLifecycle
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
     * Access point to the use cases, derived from the test-specific [dependency].
     */
    protected val useCases: UseCases by lazy { dependency.useCases }
    /**
     * Factory used to create Stores using the test-specific [dependency].
     */
    protected val storeFactory = StoreFactory(dependency = dependency)

    /**
     * List with the simulated navigation backstack.
     */
    private val _screens: MutableList<Screen> = mutableListOf()
    protected val screens: List<Screen> get() = _screens

    /**
     * Background loop that listens to the global [Command] channel.
     */
    private suspend fun receiveCommands() {
        receiveCommand { command ->
            when (command) {
                is Command.Navigate -> _screens.addOrTruncate(element = command.screen)
                Command.Back -> _screens.removeLastOrNull()
                Command.ToggleAudio -> {}
            }
        }
    }

    /**
     * Helper to manage the navigation backstack list.
     * If the [element] does not exist, then it is appended to the list, otherwise all screens after it are cleared.
     */
    private fun MutableList<Screen>.addOrTruncate(element: Screen) {
        val index = indexOf(element = element)
        if (index == -1) add(element = element) else {
            if (index + 1 < size) subList(fromIndex = index + 1, toIndex = size).clear()
        }
    }

    /**
     * Simulates a navigation event.
     */
    protected fun navigate(screen: Screen) {
        sendCommand(command = Command.Navigate(screen = screen))
    }

    /**
     * Executes a unit test.
     * Prepares the environment by resetting local data and clearing the navigation stack, then launches a job to process commands.
     */
    protected fun runUnitTest(block: suspend TestScope.() -> Unit) {
        runTest {
            dependency.useCases.sync.reset()
            _screens.clear()
            backgroundScope.launch(context = UnconfinedTestDispatcher(scheduler = testScheduler)) { receiveCommands() }
            block()
        }
    }

    /**
     * Executes a UI test.
     * Prepares the environment by resetting local data and clearing the navigation stack, then launches a job to process commands.
     */
    protected fun runUITest(block: suspend ComposeUiTest.() -> Unit) {
        runComposeUiTest {
            val scope = CoroutineScope(context = UnconfinedTestDispatcher())
            try {
                dependency.useCases.sync.reset()
                _screens.clear()
                scope.launch { receiveCommands() }
                block()
            } finally {
                scope.cancel()
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
        val lifecycleOwner = withContext(context = Dispatchers.Main) { TestLifecycle() }
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
    protected fun <State, Action> Store<State, Action>.state(): State = stateFlow.value

    /**
     * Creates a [SemanticsMatcher] to verify the number of items in a collection.
     */
    protected fun hasCount(count: Int): SemanticsMatcher = SemanticsMatcher(description = "Has $count items") { node ->
        val collectionInfo = node.config.getOrNull(key = SemanticsProperties.CollectionInfo)
        collectionInfo != null && (collectionInfo.rowCount == count || collectionInfo.columnCount == count)
    }
}
