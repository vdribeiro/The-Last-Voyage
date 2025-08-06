package com.hybris.tlv.usecase.achievement.local

import com.hybris.tlv.usecase.achievement.model.Achievement

internal interface AchievementLocal {

    /**
     * Returns true if there are no [Achievement]s in the database, false otherwise.
     */
    fun isAchievementEmpty(): Boolean

    /**
     * Rewrites the [Achievement] table with the given [achievements].
     */
    fun rewriteAchievements(achievements: List<Achievement>)

    /**
     * Get [Achievement]s from the database.
     */
    fun getAchievements(): List<Achievement>
}
