package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.achievements
import com.hybris.tlv.getStore
import com.hybris.tlv.translations
import com.hybris.tlv.ui.screen.achievement.AchievementScreen
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun AchievementLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        AchievementScreen(
            store = getStore(
                initialState = AchievementState(
                    loading = true,
                    achievements = emptyList()
                )
            )
        )
    }
}

@Preview
@Composable
private fun AchievementList() {
    TranslationCache.set(translations = translations)
    AppTheme {
        AchievementScreen(
            store = getStore(
                initialState = AchievementState(
                    loading = true,
                    achievements = achievements
                )
            )
        )
    }
}
