package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.data.model.Routine
import ai.mlxdroid.selfmassage.domain.GetRoutineDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoutineDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRoutineDetail: GetRoutineDetailUseCase,
    repository: MassageRepositoryInterface
) : ViewModel() {

    private val routineId: String = savedStateHandle["routineId"] ?: ""

    private val _routine = MutableStateFlow(
        if (routineId.isNotEmpty()) getRoutineDetail(routineId) else null
    )
    val routine: StateFlow<Routine?> = _routine.asStateFlow()

    private val _techniques = MutableStateFlow(
        _routine.value?.techniqueIds?.mapNotNull { repository.techniqueById(it) } ?: emptyList()
    )
    val techniques: StateFlow<List<MassageTechnique>> = _techniques.asStateFlow()

    private val _error = MutableStateFlow(
        when {
            routineId.isEmpty() -> "Routine not found"
            _routine.value == null -> "Routine not found"
            else -> null
        }
    )
    val error: StateFlow<String?> = _error.asStateFlow()
}
