package com.hybris.tlv.usecase.credits

import com.hybris.tlv.Tester
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

internal class CreditsUseCasesTest: Tester() {

    @Test
    fun rewrite() = runBlocking {
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
