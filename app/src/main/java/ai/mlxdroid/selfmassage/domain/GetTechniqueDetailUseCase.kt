package ai.mlxdroid.selfmassage.domain

import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import javax.inject.Inject

class GetTechniqueDetailUseCase @Inject constructor(private val repository: MassageRepositoryInterface) {
    operator fun invoke(techniqueId: String): MassageTechnique? =
        repository.techniqueById(techniqueId)
}
