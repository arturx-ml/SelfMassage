package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.domain.GetTechniqueDetailUseCase

class MassageDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getTechniqueDetail: GetTechniqueDetailUseCase
) : ViewModel() {

    val technique: MassageTechnique? = run {
        val techniqueId: String = checkNotNull(savedStateHandle["techniqueId"])
        getTechniqueDetail(techniqueId)
    }

    companion object {
        fun factory(repo: MassageRepositoryInterface = MassageRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MassageDetailViewModel(
                        savedStateHandle = createSavedStateHandle(),
                        getTechniqueDetail = GetTechniqueDetailUseCase(repo)
                    )
                }
            }
    }
}
