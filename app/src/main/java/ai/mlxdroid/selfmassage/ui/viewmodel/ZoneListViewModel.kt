package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.MassageRepositoryInterface
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.domain.GetZonesUseCase

class ZoneListViewModel(getZones: GetZonesUseCase) : ViewModel() {

    val zones: List<BodyZone> = getZones()

    companion object {
        fun factory(repo: MassageRepositoryInterface = MassageRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { ZoneListViewModel(GetZonesUseCase(repo)) }
            }
    }
}
