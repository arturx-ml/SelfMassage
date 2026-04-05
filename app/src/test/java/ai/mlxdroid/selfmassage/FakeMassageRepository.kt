package ai.mlxdroid.selfmassage

import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.data.model.Routine

class FakeMassageRepository(
    override val zones: List<BodyZone> = MassageRepository.zones,
    override val techniques: List<MassageTechnique> = MassageRepository.techniques,
    override val routines: List<Routine> = MassageRepository.routines
) : MassageRepositoryInterface {

    override fun zoneById(id: String): BodyZone? = zones.find { it.id == id }

    override fun techniqueById(id: String): MassageTechnique? = techniques.find { it.id == id }

    override fun techniquesForZone(zoneId: String): List<MassageTechnique> {
        val zone = zoneById(zoneId) ?: return emptyList()
        return zone.techniqueIds.mapNotNull { techniqueById(it) }
    }

    override fun routineById(id: String): Routine? = routines.find { it.id == id }
}
