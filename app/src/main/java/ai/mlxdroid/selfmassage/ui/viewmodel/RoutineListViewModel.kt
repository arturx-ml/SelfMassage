package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.ViewModel
import ai.mlxdroid.selfmassage.data.model.Routine
import ai.mlxdroid.selfmassage.domain.GetRoutinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoutineListViewModel @Inject constructor(getRoutines: GetRoutinesUseCase) : ViewModel() {
    private val _routines = MutableStateFlow(getRoutines())
    val routines: StateFlow<List<Routine>> = _routines.asStateFlow()
}
