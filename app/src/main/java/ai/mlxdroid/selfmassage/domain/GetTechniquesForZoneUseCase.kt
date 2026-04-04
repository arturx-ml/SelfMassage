package ai.mlxdroid.selfmassage.domain

import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageTechnique

class GetTechniquesForZoneUseCase(private val repository: MassageRepositoryInterface) {
    operator fun invoke(zoneId: String): Pair<BodyZone?, List<MassageTechnique>> =
        repository.zoneById(zoneId) to repository.techniquesForZone(zoneId)
}
