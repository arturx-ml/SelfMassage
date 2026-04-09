package ai.mlxdroid.selfmassage.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MassageRepositoryTest {

    @Test
    fun zones_returnsThreeZones() {
        assertEquals(3, MassageRepository.zones.size)
    }

    @Test
    fun zones_containsNeckShouldersArms() {
        val ids = MassageRepository.zones.map { it.id }
        assertTrue(ids.containsAll(listOf("neck", "shoulders", "arms")))
    }

    @Test
    fun techniques_returnsTotalOfEight() {
        assertEquals(8, MassageRepository.techniques.size)
    }

    @Test
    fun zoneById_knownId_returnsCorrectZone() {
        val zone = MassageRepository.zoneById("neck")
        assertNotNull(zone)
        assertEquals("Neck", zone!!.name)
    }

    @Test
    fun zoneById_unknownId_returnsNull() {
        assertNull(MassageRepository.zoneById("back"))
    }

    @Test
    fun techniqueById_knownId_returnsCorrectTechnique() {
        val technique = MassageRepository.techniqueById("neck_suboccipital")
        assertNotNull(technique)
        assertEquals("Suboccipital Release", technique!!.name)
    }

    @Test
    fun techniqueById_unknownId_returnsNull() {
        assertNull(MassageRepository.techniqueById("nonexistent"))
    }

    @Test
    fun techniquesForZone_neck_returnsThreeTechniques() {
        val list = MassageRepository.techniquesForZone("neck")
        assertEquals(3, list.size)
        assertTrue(list.all { it.id.startsWith("neck_") })
    }

    @Test
    fun techniquesForZone_arms_returnsTwoTechniques() {
        assertEquals(2, MassageRepository.techniquesForZone("arms").size)
    }

    @Test
    fun techniquesForZone_unknownZone_returnsEmptyList() {
        assertTrue(MassageRepository.techniquesForZone("unknown").isEmpty())
    }

    @Test
    fun techniquesForZone_preservesOrderFromTechniqueIds() {
        val list = MassageRepository.techniquesForZone("shoulders")
        assertEquals("shoulder_cross_friction", list[0].id)
        assertEquals("shoulder_blade_squeeze", list[1].id)
        assertEquals("shoulder_deltoid_strip", list[2].id)
    }

    @Test
    fun eachTechnique_hasAtLeastOneStep() {
        MassageRepository.techniques.forEach { technique ->
            assertTrue(
                "Technique '${technique.id}' has no steps",
                technique.steps.isNotEmpty()
            )
        }
    }

    @Test
    fun eachZone_allTechniqueIdsResolvable() {
        MassageRepository.zones.forEach { zone ->
            zone.techniqueIds.forEach { id ->
                assertNotNull(
                    "Zone '${zone.id}' references unresolvable technique id '$id'",
                    MassageRepository.techniqueById(id)
                )
            }
        }
    }
}
