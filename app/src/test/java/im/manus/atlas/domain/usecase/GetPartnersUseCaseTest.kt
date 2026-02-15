package im.manus.atlas.domain.usecase

import im.manus.atlas.domain.model.Partner
import im.manus.atlas.domain.repository.AtlasRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetPartnersUseCaseTest {

    @Mock
    private lateinit var repository: AtlasRepository

    private lateinit var getPartnersUseCase: GetPartnersUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getPartnersUseCase = GetPartnersUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository returns partners`() = runBlocking {
        val partners = listOf(
            Partner(storeId = "1", name = "Partner 1", status = "Active", deliveryStation = "DS1", lat = 0.0, lon = 0.0, radius = 100, capacity = 10)
        )
        `when`(repository.getPartners()).thenReturn(Result.success(partners))

        val result = getPartnersUseCase()

        assertEquals(true, result.isSuccess)
        assertEquals(partners, result.getOrNull())
    }
}
