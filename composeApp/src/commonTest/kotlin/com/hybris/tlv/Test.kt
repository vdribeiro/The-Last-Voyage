package com.hybris.tlv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.dependency.Dependency
import com.hybris.tlv.http.TestEngines
import com.hybris.tlv.ui.navigation.Command
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.receiveCommand
import com.hybris.tlv.ui.navigation.sendCommand
import com.hybris.tlv.usecase.UseCases

internal val dependency: Dependency by lazy {
    Dependency(
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines.testEngine,
        audioPlayer = AudioPlayer(),
    )
}

internal fun reset() {
    runBlocking {
        dependency.useCases.sync.reset()
    }
}

internal val useCases: UseCases by lazy { dependency.useCases }

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTestApi::class)
abstract class TestCase {

    private val _screens: MutableList<Screen> = mutableListOf()
    internal val screens: List<Screen> get() = _screens

    private suspend fun receiveCommands() {
        receiveCommand { command ->
            when (command) {
                is Command.Navigate -> _screens.add(element = command.screen)
                Command.Back -> _screens.removeLastOrNull()
                Command.ToggleAudio -> {}
            }
        }
    }

    internal fun navigate(screen: Screen) {
        sendCommand(command = Command.Navigate(screen = screen))
    }

    protected fun runUnitTest(block: suspend TestScope.() -> Unit): TestResult = runTest {
        reset()
        _screens.clear()
        backgroundScope.launch(context = UnconfinedTestDispatcher(scheduler = testScheduler)) { receiveCommands() }
        setup()
        block()
    }

    protected fun runUITest(block: suspend ComposeUiTest.() -> Unit): TestResult = runComposeUiTest {
        val scope = CoroutineScope(context = UnconfinedTestDispatcher())
        try {
            reset()
            _screens.clear()
            scope.launch { receiveCommands() }
            setup()
            waitForIdle()
            block()
        } finally {
            scope.cancel()
        }
    }

    protected open fun setup() {}
}
