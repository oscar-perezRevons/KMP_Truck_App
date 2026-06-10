package truck.project.features.admin

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import truck.project.features.admin.data.repository.AdminRepositoryImpl
import truck.project.features.admin.domain.vo.Email
import truck.project.features.admin.domain.vo.Password
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock
import truck.project.features.fleet.data.local.TruckDao
import truck.project.features.fleet.data.local.DriverDao

class AdminRepositoryTest {

    @Mock
    private val truckDao = mock(classOf<TruckDao>())
    @Mock
    private val driverDao = mock(classOf<DriverDao>())

    private val repository = AdminRepositoryImpl(truckDao, driverDao)

    @Test
    fun `login should return admin on success`() = runTest {
        val email = Email("admin@flota.com")
        val password = Password("password123")
        
        val result = repository.login(email, password)
        
        assertTrue(result.isSuccess)
        assertEquals(email, result.getOrNull()?.email)
    }

    @Test
    fun `getAdminProfile should return correct admin data`() = runTest {
        val profile = repository.getAdminProfile().first()
        
        assertEquals("María González", profile?.name)
        assertEquals("Transportes del Norte S.A.", profile?.company)
    }
}
