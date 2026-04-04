package ai.mlxdroid.selfmassage.domain

import ai.mlxdroid.selfmassage.FakeMassageRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class GetTechniqueDetailUseCaseTest {

    private val useCase = GetTechniqueDetailUseCase(FakeMassageRepository())

    @Test
    fun invoke_knownId_returnsTechnique() {
        val result = useCase("neck_suboccipital")
        assertNotNull(result)
        assertEquals("Suboccipital Release", result?.name)
    }

    @Test
    fun invoke_unknownId_returnsNull() {
        assertNull(useCase("does_not_exist"))
    }

    @Test
    fun invoke_eachRealTechniqueId_resolvesSuccessfully() {
        val repo = FakeMassageRepository()
        val uc = GetTechniqueDetailUseCase(repo)
        repo.techniques.forEach { technique ->
            assertNotNull(
                "Expected to resolve technique '${technique.id}'",
                uc(technique.id)
            )
        }
    }
}
