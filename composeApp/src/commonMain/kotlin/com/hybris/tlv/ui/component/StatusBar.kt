package com.hybris.tlv.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BedroomParent
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun StatusBar(
    modifier: Modifier = Modifier,
    hull: String,
    fuel: String,
    materials: String,
    cryopods: String
) {
    Surface(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusBarItem(
                icon = Icons.Outlined.Shield,
                value = hull,
                contentDescription = "Hull Integrity"
            )
            StatusBarItem(
                icon = Icons.Outlined.LocalGasStation,
                value = fuel,
                contentDescription = "Fuel"
            )
            StatusBarItem(
                icon = Icons.Outlined.Construction,
                value = materials,
                contentDescription = "Materials"
            )
            StatusBarItem(
                icon = Icons.Outlined.BedroomParent,
                value = cryopods,
                contentDescription = "Cryopods"
            )
        }
    }
}

@Composable
private fun StatusBarItem(
    icon: ImageVector,
    value: String,
    contentDescription: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(size = 20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
