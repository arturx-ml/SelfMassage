package ai.mlxdroid.selfmassage.domain

import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.BodyZone
import javax.inject.Inject

class GetZonesUseCase @Inject constructor(private val repository: MassageRepositoryInterface) {
    operator fun invoke(): List<BodyZone> = repository.zones
}
