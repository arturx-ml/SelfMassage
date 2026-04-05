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
        id = "neck_suboccipital",
        name = "Suboccipital Release",
        summary = "Releases tension at the base of the skull",
        durationMinutes = 3,
        steps = emptyList(),
        animationType = AnimationType.PRESSURE_PULSE,
        bodyLocation = BodyLocation.BASE_OF_SKULL
    )

    @Test
    fun invoke_knownZoneId_returnsPairWithZoneAndTechniques() {
        val repo = FakeMassageRepository()
        val (zone, techniques) = GetTechniquesForZoneUseCase(repo)("neck")
        assertNotNull(zone)
        assertEquals("neck", zone?.id)
        assertEquals(3, techniques.size)
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
        val (_, techniques) = GetTechniquesForZoneUseCase(repo)("shoulders")
        assertEquals("shoulder_cross_friction", techniques[0].id)
        assertEquals("shoulder_blade_squeeze", techniques[1].id)
        assertEquals("shoulder_deltoid_strip", techniques[2].id)
    }
}

