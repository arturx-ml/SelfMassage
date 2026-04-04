package ai.mlxdroid.selfmassage.domain

import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.MassageTechnique

class GetTechniqueDetailUseCase(private val repository: MassageRepositoryInterface) {
    operator fun invoke(techniqueId: String): MassageTechnique? =
        repository.techniqueById(techniqueId)
}
