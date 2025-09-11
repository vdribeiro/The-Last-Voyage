package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hybris.tlv.Core
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.domain.Translation
import database.AppDatabase

@Composable
internal fun navigation(screen: Screen, state: Any?): NavigationManager = Core(
    sqlDriver = AndroidSqliteDriver(
        context = LocalContext.current,
        schema = AppDatabase.Schema,
    )
).navigation.apply { navigate(screen = screen, state = state) }

internal fun setTranslations(translations: List<Translation>) =
    TranslationCache.set(translations = translations)
