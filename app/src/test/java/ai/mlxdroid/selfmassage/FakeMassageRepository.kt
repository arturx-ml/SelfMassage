package ai.mlxdroid.selfmassage

import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageTechnique

class FakeMassageRepository(
    override val zones: List<BodyZone> = MassageRepository.zones,
    override val techniques: List<MassageTechnique> = MassageRepository.techniques
) : MassageRepositoryInterface {

    override fun zoneById(id: String): BodyZone? = zones.find { it.id == id }

    override fun techniqueById(id: String): MassageTechnique? = techniques.find { it.id == id }

    override fun techniquesForZone(zoneId: String): List<MassageTechnique> {
        val zone = zoneById(zoneId) ?: return emptyList()
        return zone.techniqueIds.mapNotNull { techniqueById(it) }
    }
}
