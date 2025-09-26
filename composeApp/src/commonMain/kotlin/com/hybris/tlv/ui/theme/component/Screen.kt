package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun Screen(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    onMusicClick: (() -> Unit)? = null,
    onFeedbackClick: (() -> Unit)? = null,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Sound button
                    onMusicClick?.let {
                        IconButton(onClick = it) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "Music"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(weight = 1f))
                    // Feedback button
                    onFeedbackClick?.let {
                        IconButton(onClick = it) {
                            Icon(
                                imageVector = Icons.Default.BugReport,
                                contentDescription = "Feedback"
                            )
                        }
                    }
                }
                topBar()
            }
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (loading) {
                true -> Loading()
                false -> content()
            }
        }
    }
}
