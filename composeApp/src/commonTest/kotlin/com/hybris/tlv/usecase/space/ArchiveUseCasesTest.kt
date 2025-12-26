package com.hybris.tlv.usecase.space

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.serializer.ARCHIVE_PLANETS_JSON
import com.hybris.tlv.serializer.ARCHIVE_STELLAR_HOSTS_JSON
import com.hybris.tlv.serializer.loadJsonFile
import com.hybris.tlv.storage.deleteFile
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

internal class ArchiveUseCasesTest: TestCase() {

    @Test
    fun getArchive() = runUnitTest {
        deleteFile(path = ARCHIVE_STELLAR_HOSTS_JSON)
        deleteFile(path = ARCHIVE_PLANETS_JSON)

        assertNull(actual = loadJsonFile(path = ARCHIVE_STELLAR_HOSTS_JSON))
        assertNull(actual = loadJsonFile(path = ARCHIVE_PLANETS_JSON))

        assertTrue(actual = useCases.archive.getArchive())
        assertNotNull(actual = loadJsonFile<List<StellarHost>>(path = ARCHIVE_STELLAR_HOSTS_JSON))
        assertNotNull(actual = loadJsonFile<List<Planet>>(path = ARCHIVE_PLANETS_JSON))

        deleteFile(path = ARCHIVE_STELLAR_HOSTS_JSON)
        deleteFile(path = ARCHIVE_PLANETS_JSON)
    }
}
