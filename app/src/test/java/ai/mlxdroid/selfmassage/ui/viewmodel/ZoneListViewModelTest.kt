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
        assertEquals(2, vm.zones.value.size)
    }

    @Test
    fun zones_firstItemMatchesFirstRepoZone() {
        val vm = viewModel()
        assertEquals("test_zone_1", vm.zones.value.first().id)
    }

    @Test
    fun zones_emptyRepo_exposesEmptyList() {
        val vm = viewModel(FakeMassageRepository(zones = emptyList()))
        assertTrue(vm.zones.value.isEmpty())
    }

    @Test
    fun zones_customRepo_exposesCustomZones() {
        val customZones = listOf(
            BodyZone("test", "Test Zone", "icon", emptyList())
        )
        val vm = viewModel(FakeMassageRepository(zones = customZones))
        assertEquals(1, vm.zones.value.size)
        assertEquals("test", vm.zones.value.first().id)
    }
}
