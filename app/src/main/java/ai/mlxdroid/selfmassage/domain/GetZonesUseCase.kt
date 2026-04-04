package ai.mlxdroid.selfmassage.domain

import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.BodyZone

class GetZonesUseCase(private val repository: MassageRepositoryInterface) {
    operator fun invoke(): List<BodyZone> = repository.zones
}
