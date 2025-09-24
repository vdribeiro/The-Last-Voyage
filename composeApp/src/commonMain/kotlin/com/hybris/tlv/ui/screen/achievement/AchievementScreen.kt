package com.hybris.tlv.ui.screen.achievement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.AchievementItem
import com.hybris.tlv.ui.theme.component.DebouncedLinearProgressIndicator
import com.hybris.tlv.ui.theme.thenIf

@Composable
internal fun AchievementScreen(store: Store<AchievementAction, AchievementState>) {
    val storeState by store.stateFlow.collectAsState()

    Scaffold(
        modifier = Modifier.thenIf(
            tag = ACHIEVEMENT_SCREEN,
            maxWidth = Dp.Infinity,
            maxHeight = Dp.Infinity
        ),
    ) { innerPadding ->
        Box(modifier = Modifier.thenIf(padding = innerPadding)) {
            when (storeState.loading) {
                true -> DebouncedLinearProgressIndicator(
                    modifier = Modifier.thenIf(
                        tag = ACHIEVEMENT_SCREEN_PROGRESS_INDICATOR,
                        maxWidth = Dp.Infinity
                    )
                )

                false -> LazyColumn(
                    modifier = Modifier.thenIf(
                            tag = ACHIEVEMENT_SCREEN_LIST,
                            maxWidth = Dp.Infinity,
                            maxHeight = Dp.Infinity,
                            padding = PaddingValues(all = 16.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {
                    items(items = storeState.achievements, key = { it.id }) { achievement ->
                        AchievementItem(
                            modifier = Modifier.thenIf(tag = ACHIEVEMENT_SCREEN_LIST_ITEM),
                            name = achievement.name,
                            description = achievement.description
                        )
                    }
                }
            }
        }
    }
}
