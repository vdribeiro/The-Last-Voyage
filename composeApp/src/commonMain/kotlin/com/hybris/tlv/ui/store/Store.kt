package com.hybris.tlv.ui.store

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.ui.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal abstract class Store<Action, State>(
    private val dispatcher: Dispatcher,
    private val navigation: Navigation,
    initialState: State
) {
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(value = initialState)
    val stateFlow: StateFlow<State> get() = _stateFlow
    private val pipeline = Mutex()
    private val jobs = mutableListOf<Job>()

    fun send(action: Action) = reducer(state = _stateFlow.value, action = action)

    protected abstract fun reducer(state: State, action: Action)

    protected fun updateState(body: (State) -> State) =
        dispatcher.main.launch { _stateFlow.update { body(_stateFlow.value) } }

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        dispatcher.io.launch(block = block).also { jobs.add(element = it) }

    protected fun launchInPipeline(block: suspend CoroutineScope.() -> Unit): Job =
        dispatcher.io.launch {
            pipeline.withLock { block() }
        }.also { jobs.add(element = it) }

    fun cancelJobs() = jobs.forEach { it.cancel() }

    protected fun navigate(screen: Navigation.Screen, state: Any? = null) {
        cancelJobs()
        navigation.navigate(screen = screen, state = state)
    }
}
