package ai.mlxdroid.selfmassage.domain

import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.Routine
import javax.inject.Inject

class GetRoutinesUseCase @Inject constructor(private val repo: MassageRepositoryInterface) {
    operator fun invoke(): List<Routine> = repo.routines
}
