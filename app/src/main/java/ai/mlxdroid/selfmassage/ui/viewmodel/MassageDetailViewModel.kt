package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.domain.GetTechniqueDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MassageDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTechniqueDetail: GetTechniqueDetailUseCase
) : ViewModel() {

    val technique: MassageTechnique? = run {
        val techniqueId: String = checkNotNull(savedStateHandle["techniqueId"])
        getTechniqueDetail(techniqueId)
    }
}
