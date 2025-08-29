package com.hybris.tlv.usecase.achievement.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.achievements
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class AchievementRemoteTest {

    @Test
    fun `get achievements`() = runTest {
        assertEquals(expected = Result.Success(list = achievements), actual = mock.achievementApi.getAchievements())
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.achievementApi.getAchievements() is Result.Error)
    }
}
