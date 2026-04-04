package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.domain.GetTechniquesForZoneUseCase

data class MassageListUiState(
    val zone: BodyZone?,
    val techniques: List<MassageTechnique>
)

class MassageListViewModel(
    savedStateHandle: SavedStateHandle,
    getTechniquesForZone: GetTechniquesForZoneUseCase
) : ViewModel() {

    val zoneId: String = checkNotNull(savedStateHandle["zoneId"])

    val uiState: MassageListUiState = run {
        val (zone, techniques) = getTechniquesForZone(zoneId)
        MassageListUiState(zone = zone, techniques = techniques)
    }

    companion object {
        fun factory(repo: MassageRepositoryInterface = MassageRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MassageListViewModel(
                        savedStateHandle = createSavedStateHandle(),
                        getTechniquesForZone = GetTechniquesForZoneUseCase(repo)
                    )
                }
            }
    }
}
