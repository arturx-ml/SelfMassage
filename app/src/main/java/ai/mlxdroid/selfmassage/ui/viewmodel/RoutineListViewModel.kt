package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.ViewModel
import ai.mlxdroid.selfmassage.data.model.Routine
import ai.mlxdroid.selfmassage.domain.GetRoutinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoutineListViewModel @Inject constructor(getRoutines: GetRoutinesUseCase) : ViewModel() {
    val routines: List<Routine> = getRoutines()
}
