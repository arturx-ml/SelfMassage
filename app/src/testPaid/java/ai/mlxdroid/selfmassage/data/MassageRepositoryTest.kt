package ai.mlxdroid.selfmassage.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MassageRepositoryTest {

    @Test
    fun zones_returnsFiveZones() {
        assertEquals(5, MassageRepository.zones.size)
    }

    @Test
    fun zones_containsAllExpectedIds() {
        val ids = MassageRepository.zones.map { it.id }
        assertTrue(ids.containsAll(listOf("neck", "shoulders", "arms", "lower_back", "legs")))
    }

    @Test
    fun techniques_returnsFourteen() {
        assertEquals(14, MassageRepository.techniques.size)
    }

    @Test
    fun routines_returnsSix() {
        assertEquals(6, MassageRepository.routines.size)
    }

    @Test
    fun zoneById_knownId_returnsCorrectZone() {
        val zone = MassageRepository.zoneById("lower_back")
        assertNotNull(zone)
        assertEquals("Lower Back", zone!!.name)
    }

    @Test
    fun zoneById_unknownId_returnsNull() {
        assertNull(MassageRepository.zoneById("chest"))
    }

    @Test
    fun techniqueById_knownId_returnsCorrectTechnique() {
        val technique = MassageRepository.techniqueById("back_lumbar_press")
        assertNotNull(technique)
        assertEquals("Lumbar Pressure Points", technique!!.name)
    }

    @Test
    fun techniqueById_unknownId_returnsNull() {
        assertNull(MassageRepository.techniqueById("nonexistent"))
    }

    @Test
    fun techniquesForZone_lowerBack_returnsThreeTechniques() {
        val list = MassageRepository.techniquesForZone("lower_back")
        assertEquals(3, list.size)
        assertTrue(list.all { it.id.startsWith("back_") })
    }

    @Test
    fun techniquesForZone_legs_returnsThreeTechniques() {
        val list = MassageRepository.techniquesForZone("legs")
        assertEquals(3, list.size)
        assertTrue(list.all { it.id.startsWith("leg_") })
    }

    @Test
    fun techniquesForZone_unknownZone_returnsEmptyList() {
        assertTrue(MassageRepository.techniquesForZone("unknown").isEmpty())
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

    @Test
    fun eachRoutine_allTechniqueIdsResolvable() {
        MassageRepository.routines.forEach { routine ->
            routine.techniqueIds.forEach { id ->
                assertNotNull(
                    "Routine '${routine.id}' references unresolvable technique id '$id'",
                    MassageRepository.techniqueById(id)
                )
            }
        }
    }
}
