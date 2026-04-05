package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.domain.GetTechniquesForZoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class MassageListUiState(
    val zone: BodyZone?,
    val techniques: List<MassageTechnique>
)

@HiltViewModel
class MassageListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTechniquesForZone: GetTechniquesForZoneUseCase
) : ViewModel() {

    val zoneId: String = checkNotNull(savedStateHandle["zoneId"])

    val uiState: MassageListUiState = run {
        val (zone, techniques) = getTechniquesForZone(zoneId)
        MassageListUiState(zone = zone, techniques = techniques)
    }
}
