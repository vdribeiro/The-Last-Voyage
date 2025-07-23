package com.hybris.tlv.ui.screen.achievement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.model.Achievement

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AchievementScreen(store: Store<AchievementAction, AchievementState>) {
    val storeState by store.stateFlow.collectAsState()

    BackHandler(enabled = true) { store.send(action = AchievementAction.Back) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 12.dp)
            ) {
                items(items = storeState.achievements, key = { it.id }) { achievement ->
                    AchievementItem(achievement = achievement)
                }
            }
        }
    }
}

@Composable
private fun AchievementItem(achievement: Achievement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(size = 12.dp)
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Image(
            //    painter = painterResource(resource = getDrawableResource(achievement.imageResource)),
            //    contentDescription = achievement.name,
            //    contentScale = ContentScale.Crop,
            //    modifier = Modifier
            //        .size(size = 64.dp)
            //        .clip(shape = CircleShape)
            //)
            Icon(
                imageVector = Icons.Outlined.AcUnit,
                contentDescription = achievement.name,
                modifier = Modifier.size(size = 40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.width(width = 16.dp))

            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = achievement.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(height = 4.dp))
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}