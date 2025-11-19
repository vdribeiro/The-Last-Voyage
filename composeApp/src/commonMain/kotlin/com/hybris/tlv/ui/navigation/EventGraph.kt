package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun NavGraphBuilder.eventGraph() {
    val navController = rememberNavController()
    composable<Screen.Event> {

        val store = viewModel {
            EventStore(createSavedStateHandle())
        }
        Text(text = "EVENT")

        LifecycleCoroutine(store) {
            store.effect.collect { effect ->
                when (effect) {

                }
            }
        }
    }
}

internal class EventStore(createSavedStateHandle: SavedStateHandle): ViewModel() {

    override fun onCleared() {
        Telemetry.info("STORE", "onCleared")
        super.onCleared()
    }
}

inline fun <reified T: Any> NavController.navigateSingleStack(route: T) {
    navigate(route) {
        popUpTo<T> {
            inclusive = true
        }
    }
}