package com.hybris.tlv.domain.usecase.space

internal interface ArchiveUseCases {

    /**
     * Get exoplanet data from the NASA archive.
     */
    suspend fun getArchive(): Boolean
}
