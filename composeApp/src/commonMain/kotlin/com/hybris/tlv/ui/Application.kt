package com.hybris.tlv.ui

import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import com.hybris.tlv.serializer.TRANSLATIONS_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.TranslationProvider
import com.hybris.tlv.usecase.translation.TranslationCache

@Composable
internal fun Application(content: @Composable () -> Unit) {
    val isPreview = LocalInspectionMode.current
    if (isPreview) TranslationCache.set(translations = runBlocking { loadFromJsonResource(path = TRANSLATIONS_JSON) })
    TranslationProvider { AppTheme(content = content) }
}
