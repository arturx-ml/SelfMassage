package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.ViewModel
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.domain.GetZonesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ZoneListViewModel @Inject constructor(getZones: GetZonesUseCase) : ViewModel() {
    private val _zones = MutableStateFlow(getZones())
    val zones: StateFlow<List<BodyZone>> = _zones.asStateFlow()
}
