package com.hybris.tlv.ui.screen.achievement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.AchievementItem
import com.hybris.tlv.ui.theme.component.Screen

@Composable
internal fun AchievementScreen(store: Store<AchievementAction, AchievementState>) {
    val storeState by store.stateFlow.collectAsState()

    Screen(
        modifier = Modifier.testTag(tag = ACHIEVEMENT_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
    ) {
        LazyColumn(
            modifier = Modifier
                .testTag(tag = ACHIEVEMENT_SCREEN_LIST)
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            items(items = storeState.achievements, key = { it.id }) { achievement ->
                AchievementItem(
                    modifier = Modifier.testTag(tag = ACHIEVEMENT_SCREEN_LIST_ITEM),
                    name = achievement.name,
                    description = achievement.description
                )
            }
        }
    }
}
