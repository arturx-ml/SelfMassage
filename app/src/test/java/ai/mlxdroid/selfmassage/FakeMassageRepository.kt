package ai.mlxdroid.selfmassage

import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.AnimationType
import ai.mlxdroid.selfmassage.data.model.BodyLocation
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageStep
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.data.model.Routine

class FakeMassageRepository(
    override val zones: List<BodyZone> = defaultZones,
    override val techniques: List<MassageTechnique> = defaultTechniques,
    override val routines: List<Routine> = defaultRoutines
) : MassageRepositoryInterface {

    override fun zoneById(id: String): BodyZone? = zones.find { it.id == id }

    override fun techniqueById(id: String): MassageTechnique? = techniques.find { it.id == id }

    override fun techniquesForZone(zoneId: String): List<MassageTechnique> {
        val zone = zoneById(zoneId) ?: return emptyList()
        return zone.techniqueIds.mapNotNull { techniqueById(it) }
    }

    override fun routineById(id: String): Routine? = routines.find { it.id == id }

    companion object {
        val defaultTechniques = listOf(
            MassageTechnique(
                id = "test_technique_1",
                name = "Test Technique One",
                summary = "First test technique",
                durationMinutes = 3,
                animationType = AnimationType.PRESSURE_PULSE,
                bodyLocation = BodyLocation.BASE_OF_SKULL,
                steps = listOf(
                    MassageStep(1, "Step one", 60),
                    MassageStep(2, "Step two", 60),
                    MassageStep(3, "Step three", 30)
                )
            ),
            MassageTechnique(
                id = "test_technique_2",
                name = "Test Technique Two",
                summary = "Second test technique",
                durationMinutes = 4,
                animationType = AnimationType.CIRCULAR,
                bodyLocation = BodyLocation.UPPER_TRAPEZIUS,
                steps = listOf(
                    MassageStep(1, "Step one", 60),
                    MassageStep(2, "Step two", 60),
                    MassageStep(3, "Step three", 60),
                    MassageStep(4, "Step four", 30)
                )
            ),
            MassageTechnique(
                id = "test_technique_3",
                name = "Test Technique Three",
                summary = "Third test technique",
                durationMinutes = 3,
                animationType = AnimationType.HORIZONTAL_SWEEP,
                bodyLocation = BodyLocation.FOREARM,
                steps = listOf(
                    MassageStep(1, "Step one", 60),
                    MassageStep(2, "Step two", 60),
                    MassageStep(3, "Step three", 30)
                )
            )
        )

        val defaultZones = listOf(
            BodyZone(
                id = "test_zone_1",
                name = "Test Zone One",
                iconName = "SelfImprovement",
                techniqueIds = listOf("test_technique_1", "test_technique_2")
            ),
            BodyZone(
                id = "test_zone_2",
                name = "Test Zone Two",
                iconName = "Straighten",
                techniqueIds = listOf("test_technique_3")
            )
        )

        val defaultRoutines = listOf(
            Routine(
                id = "test_routine_1",
                name = "Test Routine One",
                description = "A test routine combining techniques one and two.",
                iconName = "SelfImprovement",
                durationMinutes = 7,
                techniqueIds = listOf("test_technique_1", "test_technique_2")
            )
        )
    }
}
