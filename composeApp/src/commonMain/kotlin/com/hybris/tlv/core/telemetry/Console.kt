package com.hybris.tlv.core.telemetry

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.hybris.tlv.core.telemetry.Console.CONSOLE_SIZE

/**
 * Console object that provides access to the last [CONSOLE_SIZE] logs.
 */
internal object Console {
    private const val CONSOLE_SIZE = 1000
    private val logs = ArrayDeque<String>(initialCapacity = CONSOLE_SIZE)
    private val dispatcher = Dispatchers.Default.limitedParallelism(1)
    private val scope = CoroutineScope(context = dispatcher + SupervisorJob())

    fun log(log: String): Job = scope.launch {
        log.splitToSequence("\n").forEach { line ->
            if (line.isNotEmpty()) {
                if (logs.size >= CONSOLE_SIZE) logs.removeFirstOrNull()
                logs.addLast(element = line)
            }
        }
    }

    /**
     * Get a snapshot of the current logs.
     */
    suspend fun getSnapshot(): List<String> =
        withContext(context = dispatcher) { logs.toList() }
}
