package com.hybris.tlv.domain.usecase.space

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.hybris.tlv.data.storage.FilePath
import com.hybris.tlv.data.storage.deleteJsonFile
import com.hybris.tlv.data.storage.loadJsonFile
import com.hybris.tlv.domain.usecase.space.model.Planet
import com.hybris.tlv.domain.usecase.space.model.StellarHost
import com.hybris.tlv.test.TestCase

internal class ArchiveUseCasesTest: TestCase() {

    @Test
    fun getArchive() = runUnitTest {
        deleteJsonFile(json = FilePath.ArchiveStellarHosts)
        deleteJsonFile(json = FilePath.ArchivePlanets)

        assertNull(actual = loadJsonFile(json = FilePath.ArchiveStellarHosts))
        assertNull(actual = loadJsonFile(json = FilePath.ArchivePlanets))

        assertTrue(actual = dependency.get().useCases.archive.getArchive())
        assertNotNull(actual = loadJsonFile<List<StellarHost>>(json = FilePath.ArchiveStellarHosts))
        assertNotNull(actual = loadJsonFile<List<Planet>>(json = FilePath.ArchivePlanets))

        deleteJsonFile(json = FilePath.ArchiveStellarHosts)
        deleteJsonFile(json = FilePath.ArchivePlanets)
    }
}
