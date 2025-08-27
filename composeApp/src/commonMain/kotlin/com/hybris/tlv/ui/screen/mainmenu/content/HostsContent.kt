package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.StellarHostCard
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.space.mapper.spectralTypeToDrawable
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun HostsContent(store: Store<MainMenuAction, MainMenuState>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = stellarHosts, key = { it.name }) { stellarHost ->
            StellarHostCard(
                name = stellarHost.name,
                description = stellarHost.description,
                spectralTypeDrawable = stellarHost.spectralType.spectralTypeToDrawable(),
            )
        }
    }
}

private data class Host(
    val name: String,
    val description: String,
    val spectralType: String,
)

private val stellarHosts by lazy {
    listOf(
        Host(
            name = getTranslation(key = "stellar_host_type_o"),
            description = getTranslation(key = "stellar_host_type_o_description"),
            spectralType = "O"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_b"),
            description = getTranslation(key = "stellar_host_type_b_description"),
            spectralType = "B"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_a"),
            description = getTranslation(key = "stellar_host_type_a_description"),
            spectralType = "A"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_f"),
            description = getTranslation(key = "stellar_host_type_f_description"),
            spectralType = "F"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_g"),
            description = getTranslation(key = "stellar_host_type_g_description"),
            spectralType = "G"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_k"),
            description = getTranslation(key = "stellar_host_type_k_description"),
            spectralType = "K"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_m"),
            description = getTranslation(key = "stellar_host_type_m_description"),
            spectralType = "M"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_w"),
            description = getTranslation(key = "stellar_host_type_w_description"),
            spectralType = "W"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_q"),
            description = getTranslation(key = "stellar_host_type_q_description"),
            spectralType = "Q"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_p"),
            description = getTranslation(key = "stellar_host_type_p_description"),
            spectralType = "P"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_l"),
            description = getTranslation(key = "stellar_host_type_l_description"),
            spectralType = "L"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_t"),
            description = getTranslation(key = "stellar_host_type_t_description"),
            spectralType = "T"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_y"),
            description = getTranslation(key = "stellar_host_type_y_description"),
            spectralType = "Y"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_c"),
            description = getTranslation(key = "stellar_host_type_c_description"),
            spectralType = "C"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_s"),
            description = getTranslation(key = "stellar_host_type_s_description"),
            spectralType = "S"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_d"),
            description = getTranslation(key = "stellar_host_type_d_description"),
            spectralType = "D"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_unknown"),
            description = getTranslation(key = "stellar_host_type_unknown_description"),
            spectralType = "?"
        ),
    )
}
