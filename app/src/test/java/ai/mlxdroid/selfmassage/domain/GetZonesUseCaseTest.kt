package ai.mlxdroid.selfmassage.domain

import ai.mlxdroid.selfmassage.FakeMassageRepository
import ai.mlxdroid.selfmassage.data.model.BodyZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetZonesUseCaseTest {

    @Test
    fun invoke_returnsAllZonesFromRepository() {
        val repo = FakeMassageRepository()
        val result = GetZonesUseCase(repo)()
        assertEquals(repo.zones.size, result.size)
        assertEquals(repo.zones, result)
    }

    @Test
    fun invoke_withEmptyRepo_returnsEmptyList() {
        val repo = FakeMassageRepository(zones = emptyList())
        val result = GetZonesUseCase(repo)()
        assertTrue(result.isEmpty())
    }

    @Test
    fun invoke_returnsZonesInRepoOrder() {
        val customZones = listOf(
            BodyZone("a", "Zone A", "icon", emptyList()),
            BodyZone("b", "Zone B", "icon", emptyList())
        )
        val repo = FakeMassageRepository(zones = customZones)
        val result = GetZonesUseCase(repo)()
        assertEquals("a", result[0].id)
        assertEquals("b", result[1].id)
    }
}
