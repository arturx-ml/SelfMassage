package ai.mlxdroid.selfmassage.domain

import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.SessionStep
import javax.inject.Inject

class GetSessionStepsUseCase @Inject constructor(private val repository: MassageRepositoryInterface) {

    fun forTechnique(techniqueId: String): List<SessionStep> {
        val technique = repository.techniqueById(techniqueId) ?: return emptyList()
        return technique.steps.map { step ->
            SessionStep(
                techniqueName = technique.name,
                animationType = technique.animationType,
                bodyLocation = technique.bodyLocation,
                stepOrder = step.order,
                totalStepsInTechnique = technique.steps.size,
                instruction = step.instruction,
                durationSeconds = step.durationSeconds
            )
        }
    }

    fun forRoutine(routineId: String): List<SessionStep> {
        val routine = repository.routineById(routineId) ?: return emptyList()
        return routine.techniqueIds.flatMap { forTechnique(it) }
    }
}
