package com.hybris.tlv.usecase.achievement.remote

import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.mock
import com.hybris.tlv.mock.achievements
import com.hybris.tlv.mock.errorMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class AchievementRemoteTest {

    @Test
    fun `get achievements`() = runBlocking {
        assertEquals(expected = Result.Success(list = achievements), actual = mock.achievementApi.getAchievements())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.achievementApi.getAchievements() is Result.Error)
    }
}
