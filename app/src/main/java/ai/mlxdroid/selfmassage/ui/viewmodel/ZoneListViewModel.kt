package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.ViewModel
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.domain.GetZonesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ZoneListViewModel @Inject constructor(getZones: GetZonesUseCase) : ViewModel() {
    val zones: List<BodyZone> = getZones()
}
