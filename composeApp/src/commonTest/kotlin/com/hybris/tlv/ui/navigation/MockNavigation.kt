package com.hybris.tlv.ui.navigation

import kotlin.test.assertEquals

/**
 * Simulated navigation.
 */
internal class MockNavigation {

    /**
     * List with the simulated navigation backstack.
     */
    private val screens: MutableList<Screen> = mutableListOf()

    /**
     * Background loop that listens to the global [Navigate] channel.
     */
    suspend fun receiveCommands() {
        receiveCommand { command ->
            when (command) {
                is Navigate.To -> screens.addOrTruncate(element = command.screen)
                Navigate.Back -> screens.removeLastOrNull()
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
    fun navigate(screen: Screen): Boolean =
        sendCommand(command = Navigate.To(screen = screen))

    /**
     * Compares the navigation backstack with the given screen [list].
     */
    fun assertNavigation(list: List<Screen>) =
        assertEquals(expected = list.map { it::class }, actual = screens.map { it::class })

    /**
     * Clears the navigation backstack.
     */
    fun clear() = screens.clear()
}