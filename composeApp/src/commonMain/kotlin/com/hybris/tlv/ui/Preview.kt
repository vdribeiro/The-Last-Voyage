package com.hybris.tlv.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTranslationState
import com.hybris.tlv.ui.theme.getTranslationState

/**
 * A wrapper composable for [androidx.compose.ui.tooling.preview.Preview]s.
 */
@Composable
internal fun Preview(content: @Composable () -> Unit) {
    val translationMap = getTranslationState()
    val providers = remember(key1 = translationMap) {
        buildList {
            add(element = LocalTranslationState provides translationMap)
        }.toTypedArray()
    }

    CompositionLocalProvider(values = providers) {
        AppTheme {
            content()
        }
    }
}
