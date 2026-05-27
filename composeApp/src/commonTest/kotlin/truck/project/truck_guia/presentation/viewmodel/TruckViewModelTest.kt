package truck.project.truck_guia.presentation.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import truck.project.truck_guia.domain.model.Truck
import truck.project.truck_guia.domain.repository.TruckRepository
import truck.project.truck_guia.domain.usecase.*
import truck.project.truck_guia.domain.vo.Placa
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TruckViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeTruckRepository
    private lateinit var viewModel: TruckViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeTruckRepository()
        
        val getTrucksUseCase = GetTrucksUseCase(fakeRepository)
        val saveTruckUseCase = SaveTruckUseCase(fakeRepository)
        val updateTruckUseCase = UpdateTruckUseCase(fakeRepository)
        val deleteTruckUseCase = DeleteTruckUseCase(fakeRepository)
        val syncTrucksUseCase = SyncTrucksUseCase(fakeRepository)

        viewModel = TruckViewModel(
            getTrucksUseCase,
            saveTruckUseCase,
            updateTruckUseCase,
            deleteTruckUseCase,
            syncTrucksUseCase
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando el repositorio emite camiones el estado se actualiza`() = runTest {
        val trucks = listOf(
            Truck(id = 1, licensePlate = Placa.create("ABC-1234"), model = "Volvo FH")
        )
        
        viewModel.state.test {
            assertEquals(emptyList<Truck>(), awaitItem().trucks)
            fakeRepository.emitTrucks(trucks)
            assertEquals(trucks, awaitItem().trucks)
        }
    }

    @Test
    fun `al cambiar la placa el estado refleja el nuevo valor`() = runTest {
        viewModel.state.test {
            awaitItem()
            val nuevaPlaca = "XYZ-9876"
            viewModel.onLicensePlateChange(nuevaPlaca)
            assertEquals(nuevaPlaca, awaitItem().licensePlate)
        }
    }

    @Test
    fun `al sincronizar se maneja el estado de carga correctamente`() = runTest {
        viewModel.state.test {
            awaitItem()
            viewModel.onSync()
            assertEquals(true, awaitItem().isLoading)
            assertEquals(false, awaitItem().isLoading)
        }
    }

    @Test
    fun `al seleccionar un camion para editar se cargan sus datos en el estado`() = runTest {
        val truck = Truck(id = 1, licensePlate = Placa.create("ABC-1234"), model = "Volvo FH")
        
        viewModel.state.test {
            awaitItem()
            viewModel.onEditTruck(truck)
            val state = awaitItem()
            assertEquals(truck, state.editingTruck)
            assertEquals("ABC-1234", state.licensePlate)
            assertEquals("Volvo FH", state.model)
        }
    }
}

class FakeTruckRepository : TruckRepository {
    private val trucksFlow = MutableSharedFlow<List<Truck>>(replay = 1)
    suspend fun emitTrucks(trucks: List<Truck>) {
        trucksFlow.emit(trucks)
    }
    override fun getAllTrucks(): Flow<List<Truck>> = trucksFlow
    override suspend fun saveTruck(truck: Truck) {}
    override suspend fun updateTruck(truck: Truck) {}
    override suspend fun deleteTruck(truck: Truck) {}
    override suspend fun sync() {
    }
}
