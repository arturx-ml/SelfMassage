package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.domain.GetTechniquesForZoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class MassageListUiState(
    val zone: BodyZone? = null,
    val techniques: List<MassageTechnique> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MassageListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTechniquesForZone: GetTechniquesForZoneUseCase
) : ViewModel() {

    val zoneId: String = savedStateHandle["zoneId"] ?: ""

    private val _uiState = MutableStateFlow(
        if (zoneId.isNotEmpty()) {
            val (zone, techniques) = getTechniquesForZone(zoneId)
            MassageListUiState(zone = zone, techniques = techniques)
        } else {
            MassageListUiState(error = "Zone not found")
        }
    )
    val uiState: StateFlow<MassageListUiState> = _uiState.asStateFlow()
}
