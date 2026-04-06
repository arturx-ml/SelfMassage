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

    private val techniqueId: String = savedStateHandle["techniqueId"] ?: ""

    val technique: MassageTechnique? = if (techniqueId.isNotEmpty()) {
        getTechniqueDetail(techniqueId)
    } else null

    val error: String? = when {
        techniqueId.isEmpty() -> "Technique not found"
        technique == null -> "Technique not found"
        else -> null
    }
}
