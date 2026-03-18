package com.hybris.tlv.ui.theme.component.container

import kotlinx.coroutines.delay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.navigation.LocalNavController
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.navigate
import com.hybris.tlv.ui.screen.Screen

@Composable
internal fun LoadingScreen() {
    val navController = LocalNavController.current
    var showFeedback: Boolean by remember { mutableStateOf(value = false) }
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = 5000)
        showFeedback = true
    }
    Screen(
        contentAlignment = Alignment.Center,
        loading = true,
        loadingDelayMillis = 0L,
        loadingBackground = true,
        onBackClick = null,
        onHelpClick = null,
        onMusicClick = null,
        onFeedbackClick = if (showFeedback) {
            { navController?.navigate(screen = Screen.Feedback(tag = null, message = null)) }
        } else null
    )
}

@Preview
@Composable
private fun LearnMenuPreview() = Preview {
    LoadingScreen()
}
