package ai.mlxdroid.selfmassage.ui.viewmodel

import ai.mlxdroid.selfmassage.FakeMassageRepository
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.domain.GetZonesUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ZoneListViewModelTest {

    private fun viewModel(repo: FakeMassageRepository = FakeMassageRepository()) =
        ZoneListViewModel(GetZonesUseCase(repo))

    @Test
    fun zones_exposesAllZonesFromRepo() {
        val vm = viewModel()
        assertEquals(3, vm.zones.size)
    }

    @Test
    fun zones_firstItemMatchesFirstRepoZone() {
        val vm = viewModel()
        assertEquals("neck", vm.zones.first().id)
    }

    @Test
    fun zones_emptyRepo_exposesEmptyList() {
        val vm = viewModel(FakeMassageRepository(zones = emptyList()))
        assertTrue(vm.zones.isEmpty())
    }

    @Test
    fun zones_customRepo_exposesCustomZones() {
        val customZones = listOf(
            BodyZone("test", "Test Zone", "icon", emptyList())
        )
        val vm = viewModel(FakeMassageRepository(zones = customZones))
        assertEquals(1, vm.zones.size)
        assertEquals("test", vm.zones.first().id)
    }
}
