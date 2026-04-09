package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import ai.mlxdroid.selfmassage.FakeMassageRepository
import ai.mlxdroid.selfmassage.domain.GetTechniquesForZoneUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MassageListViewModelTest {

    private fun viewModel(
        zoneId: String,
        repo: FakeMassageRepository = FakeMassageRepository()
    ) = MassageListViewModel(
        savedStateHandle = SavedStateHandle(mapOf("zoneId" to zoneId)),
        getTechniquesForZone = GetTechniquesForZoneUseCase(repo)
    )

    @Test
    fun uiState_knownZoneId_exposesCorrectZoneAndTechniques() {
        val vm = viewModel("test_zone_1")
        assertEquals("Test Zone One", vm.uiState.value.zone?.name)
        assertEquals(2, vm.uiState.value.techniques.size)
    }

    @Test
    fun uiState_unknownZoneId_exposesNullZoneAndEmptyTechniques() {
        val vm = viewModel("invalid")
        assertNull(vm.uiState.value.zone)
        assertTrue(vm.uiState.value.techniques.isEmpty())
    }

    @Test
    fun zoneId_exposedFromSavedStateHandle() {
        val vm = viewModel("test_zone_2")
        assertEquals("test_zone_2", vm.zoneId)
    }

    @Test
    fun uiState_zone2_exposesOneTechnique() {
        val vm = viewModel("test_zone_2")
        assertEquals(1, vm.uiState.value.techniques.size)
    }

    @Test
    fun uiState_zone1_exposesTwoTechniques() {
        val vm = viewModel("test_zone_1")
        assertEquals(2, vm.uiState.value.techniques.size)
        assertEquals("Test Zone One", vm.uiState.value.zone?.name)
    }
}
