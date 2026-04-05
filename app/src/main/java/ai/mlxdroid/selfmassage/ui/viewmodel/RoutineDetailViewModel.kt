package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.data.model.Routine
import ai.mlxdroid.selfmassage.domain.GetRoutineDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoutineDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRoutineDetail: GetRoutineDetailUseCase,
    repository: MassageRepositoryInterface
) : ViewModel() {

    val routine: Routine? = getRoutineDetail(checkNotNull(savedStateHandle["routineId"]))
    val techniques: List<MassageTechnique> = routine?.techniqueIds
        ?.mapNotNull { repository.techniqueById(it) } ?: emptyList()
}
