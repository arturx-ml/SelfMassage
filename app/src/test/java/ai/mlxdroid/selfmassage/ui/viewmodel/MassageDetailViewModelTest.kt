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
        zoneId: String = "neck",
        techniqueId: String,
        repo: FakeMassageRepository = FakeMassageRepository()
    ) = MassageDetailViewModel(
        savedStateHandle = SavedStateHandle(mapOf("zoneId" to zoneId, "techniqueId" to techniqueId)),
        getTechniqueDetail = GetTechniqueDetailUseCase(repo)
    )

    @Test
    fun technique_knownId_exposesCorrectTechnique() {
        val vm = viewModel(techniqueId = "neck_suboccipital")
        assertNotNull(vm.technique)
        assertEquals("Suboccipital Release", vm.technique?.name)
    }

    @Test
    fun technique_unknownId_exposesNull() {
        val vm = viewModel(techniqueId = "bad_id")
        assertNull(vm.technique)
    }

    @Test
    fun technique_hasCorrectStepCount_forSuboccipitalRelease() {
        val vm = viewModel(techniqueId = "neck_suboccipital")
        assertEquals(4, vm.technique?.steps?.size)
    }

    @Test
    fun technique_hasCorrectAnimationType_forSuboccipitalRelease() {
        val vm = viewModel(techniqueId = "neck_suboccipital")
        assertEquals(AnimationType.PRESSURE_PULSE, vm.technique?.animationType)
    }

    @Test
    fun technique_kneadingTechnique_hasCircularAnimation() {
        val vm = viewModel(techniqueId = "neck_trapezius_knead")
        assertEquals(AnimationType.CIRCULAR, vm.technique?.animationType)
    }

    @Test
    fun technique_allRealTechniqueIds_resolveSuccessfully() {
        FakeMassageRepository().techniques.forEach { t ->
            val vm = viewModel(techniqueId = t.id)
            assertNotNull("Expected '${t.id}' to resolve", vm.technique)
        }
    }
}
