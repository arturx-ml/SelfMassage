package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import ai.mlxdroid.selfmassage.FakeMassageRepository
import ai.mlxdroid.selfmassage.data.model.AnimationType
import ai.mlxdroid.selfmassage.domain.GetTechniqueDetailUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MassageDetailViewModelTest {

    private fun viewModel(
        zoneId: String = "test_zone_1",
        techniqueId: String,
        repo: FakeMassageRepository = FakeMassageRepository()
    ) = MassageDetailViewModel(
        savedStateHandle = SavedStateHandle(mapOf("zoneId" to zoneId, "techniqueId" to techniqueId)),
        getTechniqueDetail = GetTechniqueDetailUseCase(repo)
    )

    @Test
    fun technique_knownId_exposesCorrectTechnique() {
        val vm = viewModel(techniqueId = "test_technique_1")
        assertNotNull(vm.technique.value)
        assertEquals("Test Technique One", vm.technique.value?.name)
    }

    @Test
    fun technique_unknownId_exposesNull() {
        val vm = viewModel(techniqueId = "bad_id")
        assertNull(vm.technique.value)
    }

    @Test
    fun technique_hasCorrectStepCount() {
        val vm = viewModel(techniqueId = "test_technique_1")
        assertEquals(3, vm.technique.value?.steps?.size)
    }

    @Test
    fun technique_hasCorrectAnimationType_pressurePulse() {
        val vm = viewModel(techniqueId = "test_technique_1")
        assertEquals(AnimationType.PRESSURE_PULSE, vm.technique.value?.animationType)
    }

    @Test
    fun technique_secondTechnique_hasCircularAnimation() {
        val vm = viewModel(techniqueId = "test_technique_2")
        assertEquals(AnimationType.CIRCULAR, vm.technique.value?.animationType)
    }

    @Test
    fun technique_allDefaultTechniqueIds_resolveSuccessfully() {
        FakeMassageRepository().techniques.forEach { t ->
            val vm = viewModel(techniqueId = t.id)
            assertNotNull("Expected '${t.id}' to resolve", vm.technique.value)
        }
    }
}
