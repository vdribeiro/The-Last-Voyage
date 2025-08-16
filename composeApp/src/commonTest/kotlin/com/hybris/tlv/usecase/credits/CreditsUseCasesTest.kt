package com.hybris.tlv.usecase.credits

import com.hybris.tlv.mock.Mock
import kotlin.test.Test
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class CreditsUseCasesTest {

    private val mock = Mock()

    @Test
    fun rewrite() = runBlocking {
        mock.useCases.space.rewrite().last()
        Unit
    }

    ///**
    // * Rewrites the local and remote [Credits] data.
    // */
    //suspend fun rewrite(): Flow<SyncResult>
    //
    ///**
    // * Syncs the remote [Credits] data to local.
    // */
    //suspend fun syncCredits(): Flow<SyncResult>
    //
    ///**
    // * Prepopulate local [Credits].
    // */
    //suspend fun prepopulateCredits()
    //
    ///**
    // * Get [Credits] from the database.
    // */
    //suspend fun getCredits(): List<Credits>
}
