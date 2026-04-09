package ai.mlxdroid.selfmassage.domain

import ai.mlxdroid.selfmassage.FakeMassageRepository
import ai.mlxdroid.selfmassage.data.model.AnimationType
import ai.mlxdroid.selfmassage.data.model.BodyLocation
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GetTechniquesForZoneUseCaseTest {

    private val sampleTechnique = MassageTechnique(
        id = "sample",
        name = "Sample",
        summary = "Sample technique",
        durationMinutes = 3,
        steps = emptyList(),
        animationType = AnimationType.PRESSURE_PULSE,
        bodyLocation = BodyLocation.BASE_OF_SKULL
    )

    @Test
    fun invoke_knownZoneId_returnsPairWithZoneAndTechniques() {
        val repo = FakeMassageRepository()
        val (zone, techniques) = GetTechniquesForZoneUseCase(repo)("test_zone_1")
        assertNotNull(zone)
        assertEquals("test_zone_1", zone?.id)
        assertEquals(2, techniques.size)
    }

    @Test
    fun invoke_unknownZoneId_returnsNullZoneAndEmptyList() {
        val repo = FakeMassageRepository()
        val (zone, techniques) = GetTechniquesForZoneUseCase(repo)("invalid")
        assertNull(zone)
        assertTrue(techniques.isEmpty())
    }

    @Test
    fun invoke_zoneWithNoTechniques_returnsEmptyTechniqueList() {
        val emptyZone = BodyZone("empty", "Empty Zone", "icon", emptyList())
        val repo = FakeMassageRepository(
            zones = listOf(emptyZone),
            techniques = listOf(sampleTechnique)
        )
        val (zone, techniques) = GetTechniquesForZoneUseCase(repo)("empty")
        assertNotNull(zone)
        assertTrue(techniques.isEmpty())
    }

    @Test
    fun invoke_preservesTechniqueOrder() {
        val repo = FakeMassageRepository()
        val (_, techniques) = GetTechniquesForZoneUseCase(repo)("test_zone_1")
        assertEquals("test_technique_1", techniques[0].id)
        assertEquals("test_technique_2", techniques[1].id)
    }
}
