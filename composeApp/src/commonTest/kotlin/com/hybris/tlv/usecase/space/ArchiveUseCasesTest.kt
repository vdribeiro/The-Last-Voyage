package com.hybris.tlv.usecase.space

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.data.serializer.JsonFile
import com.hybris.tlv.data.serializer.deleteJsonFile
import com.hybris.tlv.data.serializer.loadJsonFile
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

internal class ArchiveUseCasesTest: TestCase() {

    @Test
    fun getArchive() = runUnitTest {
        deleteJsonFile(json = JsonFile.ArchiveStellarHosts)
        deleteJsonFile(json = JsonFile.ArchivePlanets)

        assertNull(actual = loadJsonFile(json = JsonFile.ArchiveStellarHosts))
        assertNull(actual = loadJsonFile(json = JsonFile.ArchivePlanets))

        assertTrue(actual = useCases.archive.getArchive())
        assertNotNull(actual = loadJsonFile<List<StellarHost>>(json = JsonFile.ArchiveStellarHosts))
        assertNotNull(actual = loadJsonFile<List<Planet>>(json = JsonFile.ArchivePlanets))

        deleteJsonFile(json = JsonFile.ArchiveStellarHosts)
        deleteJsonFile(json = JsonFile.ArchivePlanets)
    }
}
