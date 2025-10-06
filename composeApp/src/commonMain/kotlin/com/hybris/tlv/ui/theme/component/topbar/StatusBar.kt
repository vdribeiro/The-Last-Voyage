package com.hybris.tlv.ui.theme.component.topbar

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.alpha
import com.hybris.tlv.ui.theme.component.container.Surface
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StatusBar(
    modifier: Modifier = Modifier,
    hullEnabled: Boolean = true,
    fuelEnabled: Boolean = true,
    materialsEnabled: Boolean = true,
    cryopodsEnabled: Boolean = true,
    hull: String? = null,
    fuel: String? = null,
    materials: String? = null,
    cryopods: String? = null
) {
    Surface(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusBarItem(
                enabled = hullEnabled,
                icon = Icons.Outlined.Shield,
                value = hull,
                contentDescription = "Hull Integrity"
            )
            StatusBarItem(
                enabled = fuelEnabled,
                icon = Icons.Outlined.LocalGasStation,
                value = fuel,
                contentDescription = "Fuel"
            )
            StatusBarItem(
                enabled = materialsEnabled,
                icon = Icons.Outlined.Construction,
                value = materials,
                contentDescription = "Materials"
            )
            StatusBarItem(
                enabled = cryopodsEnabled,
                icon = Icons.Outlined.BedroomParent,
                value = cryopods,
                contentDescription = "Cryopods"
            )
        }
    }
}

@Composable
private fun StatusBarItem(
    enabled: Boolean,
    icon: ImageVector,
    value: String?,
    contentDescription: String
) {
    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    Row(
        modifier = Modifier.alpha(alpha = alpha(enabled = enabled)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {
        Icon(
            modifier = Modifier.size(size = 20.dp),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = colorScheme.primary
        )
        value?.let {
            Text(
                text = it,
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
private fun StatusBarPreview() = AppTheme {
    StatusBar()
}
