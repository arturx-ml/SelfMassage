package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.domain.GetTechniqueDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MassageDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTechniqueDetail: GetTechniqueDetailUseCase
) : ViewModel() {

    private val techniqueId: String = savedStateHandle["techniqueId"] ?: ""

    private val _technique = MutableStateFlow(
        if (techniqueId.isNotEmpty()) getTechniqueDetail(techniqueId) else null
    )
    val technique: StateFlow<MassageTechnique?> = _technique.asStateFlow()

    private val _error = MutableStateFlow(
        when {
            techniqueId.isEmpty() -> "Technique not found"
            _technique.value == null -> "Technique not found"
            else -> null
        }
    )
    val error: StateFlow<String?> = _error.asStateFlow()
}
