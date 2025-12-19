package com.hybris.tlv.usecase.space

internal interface ArchiveUseCases {

    /**
     * Get exoplanet data from the NASA archive.
     */
    suspend fun getArchive(): Boolean
}
