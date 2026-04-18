package com.hybris.tlv.core.telemetry

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.hybris.tlv.core.telemetry.Console.CONSOLE_SIZE
import com.hybris.tlv.core.telemetry.Console.dispatcher
import com.hybris.tlv.core.telemetry.Console.log

/**
 * A thread-safe, multiplatform in-memory log buffer.
 *
 * This object maintains a circular-style buffer of the most recent [CONSOLE_SIZE] log entries.
 * It uses a single-threaded dispatcher to synchronize access to the [ArrayDeque], ensuring consistency across concurrent coroutine calls without platform-specific locks.
 */
internal object Console {
    /**
     * The maximum number of log lines to retain in memory.
     */
    private const val CONSOLE_SIZE = 1000

    /**
     * A specialized dispatcher limited to a single thread to act as a synchronization lock.
     */
    private val dispatcher = Dispatchers.Default.limitedParallelism(1)

    /**
     * Internal scope for asynchronous logging operations, using a [SupervisorJob] to ensure that a failure in one log operation doesn't cancel the console entirely.
     */
    private val scope = CoroutineScope(context = dispatcher + SupervisorJob())

    /**
     * Non-thread-safe collection used to store log lines.
     * Access is restricted to the [dispatcher] for safety.
     */
    private val logs = ArrayDeque<String>(initialCapacity = CONSOLE_SIZE)

    /**
     * Asynchronously adds a log message to the buffer.
     *
     * The message is split by line breaks, and each non-empty line is appended. If the buffer exceeds [CONSOLE_SIZE], the oldest entries are discarded.
     *
     * @param log The raw log string to be added.
     * @return A [Job] representing the asynchronous background operation.
     */
    fun log(log: String): Job = scope.launch {
        log.splitToSequence("\n").forEach { line ->
            if (line.isNotEmpty()) {
                if (logs.size >= CONSOLE_SIZE) logs.removeFirstOrNull()
                logs.addLast(element = line)
            }
        }
    }

    /**
     * Returns a point-in-time snapshot of the current logs.
     *
     * This function switches to the internal synchronization context to safely copy the [ArrayDeque] into an immutable [List].
     *
     * @return A list of log lines, ordered from oldest to newest.
     */
    suspend fun getSnapshot(): List<String> =
        withContext(context = dispatcher) { logs.toList() }
}
