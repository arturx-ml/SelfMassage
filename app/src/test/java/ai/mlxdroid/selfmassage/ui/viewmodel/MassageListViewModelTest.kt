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
        val vm = viewModel("neck")
        assertEquals("Neck", vm.uiState.zone?.name)
        assertEquals(3, vm.uiState.techniques.size)
    }

    @Test
    fun uiState_unknownZoneId_exposesNullZoneAndEmptyTechniques() {
        val vm = viewModel("invalid")
        assertNull(vm.uiState.zone)
        assertTrue(vm.uiState.techniques.isEmpty())
    }

    @Test
    fun zoneId_exposedFromSavedStateHandle() {
        val vm = viewModel("shoulders")
        assertEquals("shoulders", vm.zoneId)
    }

    @Test
    fun uiState_arms_exposesTwoTechniques() {
        val vm = viewModel("arms")
        assertEquals(2, vm.uiState.techniques.size)
    }

    @Test
    fun uiState_shoulders_exposesThreeTechniques() {
        val vm = viewModel("shoulders")
        assertEquals(3, vm.uiState.techniques.size)
        assertEquals("Shoulders", vm.uiState.zone?.name)
    }
}
