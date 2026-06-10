package truck.project.features.admin.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import truck.project.features.admin.domain.repository.AdminRepository
import truck.project.features.admin.domain.model.Admin
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.trips.data.local.TripDao
import truck.project.features.trips.data.local.ExpenseDao
import truck.project.features.driver.data.mapper.toDomain
import truck.project.core.reporting.ReportService
import truck.project.core.platform.FileOpener
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AdminDashboardViewModel(
    private val repository: AdminRepository,
    private val tripDao: TripDao,
    private val expenseDao: ExpenseDao,
    private val reportService: ReportService? = null,
    private val fileOpener: FileOpener? = null
) : ViewModel() {

    private val _state = MutableStateFlow(AdminDashboardState())
    val state: StateFlow<AdminDashboardState> = _state.asStateFlow()

    private val _effect = Channel<AdminDashboardEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadStats()
        syncData()
    }

    private fun syncData() {
        viewModelScope.launch {
            repository.sync()
        }
    }

    fun onIntent(intent: AdminDashboardIntent) {
        when (intent) {
            AdminDashboardIntent.LoadStats -> {
                loadStats()
                syncData()
            }
            AdminDashboardIntent.NavigateToFleet -> {
                viewModelScope.launch { _effect.send(AdminDashboardEffect.NavigateToFleet) }
            }
            AdminDashboardIntent.NavigateToDrivers -> {
                viewModelScope.launch { _effect.send(AdminDashboardEffect.NavigateToDrivers) }
            }
            AdminDashboardIntent.NavigateToTrips -> {
                viewModelScope.launch { _effect.send(AdminDashboardEffect.NavigateToTrips) }
            }
            AdminDashboardIntent.NavigateToProfile -> {
                viewModelScope.launch { _effect.send(AdminDashboardEffect.NavigateToProfile) }
            }
            AdminDashboardIntent.Logout -> {
                viewModelScope.launch { 
                    repository.logout()
                    _effect.send(AdminDashboardEffect.NavigateToLogin) 
                }
            }
            is AdminDashboardIntent.DeactivateDriver -> {
                viewModelScope.launch {
                    repository.deactivateDriver(intent.driverId)
                }
            }
            is AdminDashboardIntent.DeleteDriver -> {
                viewModelScope.launch {
                    repository.deleteDriver(intent.driverId)
                }
            }
            is AdminDashboardIntent.EditDriver -> {
                viewModelScope.launch {
                    _effect.send(AdminDashboardEffect.NavigateToEditDriver(intent.driver))
                }
            }
            is AdminDashboardIntent.DeleteTruck -> {
                viewModelScope.launch {
                    repository.deleteTruck(intent.truckId)
                }
            }
            is AdminDashboardIntent.EditTruck -> {
                viewModelScope.launch {
                    _effect.send(AdminDashboardEffect.NavigateToEditTruck(intent.truck))
                }
            }
            is AdminDashboardIntent.DeleteTrip -> {
                viewModelScope.launch {
                    repository.deleteTrip(intent.tripId)
                }
            }
            is AdminDashboardIntent.EditTrip -> {
                viewModelScope.launch {
                    _effect.send(AdminDashboardEffect.NavigateToEditTrip(intent.trip))
                }
            }
            AdminDashboardIntent.GenerateTripsReport -> generateTripsReport()
            AdminDashboardIntent.GenerateFleetReport -> generateFleetReport()
            AdminDashboardIntent.GenerateDriversReport -> generateDriversReport()
            AdminDashboardIntent.DismissReportDialog -> {
                _state.update { it.copy(showReportDialog = false, reportData = null) }
            }
            is AdminDashboardIntent.OpenReport -> {
                fileOpener?.openFile(intent.path)
            }
            is AdminDashboardIntent.SelectTripForMonitor -> {
                observeSelectedTripExpenses(intent.tripId)
            }
        }
    }

    private var expensesJob: kotlinx.coroutines.Job? = null
    private fun observeSelectedTripExpenses(tripId: String) {
        expensesJob?.cancel()
        expensesJob = viewModelScope.launch {
            // Need a way to get expenses for a trip from repository
            // I'll use tripDao to get trip and then observe expenses via expenseDao
            expenseDao.getExpensesByTrip(tripId).collect { entities ->
                val domainExpenses = entities.map { it.toDomain() }
                _state.update { it.copy(selectedTripExpenses = domainExpenses) }
            }
        }
    }

    private fun generateTripsReport() {
        viewModelScope.launch {
            val adminId = _state.value.admin?.id ?: return@launch
            _state.update { it.copy(isGeneratingReport = true, reportMessage = "Generando reporte de rutas...") }
            tripDao.getAllTrips(adminId).first().let { trips ->
                reportService?.generateTripsReport(trips)?.onSuccess { result ->
                    _state.update { it.copy(
                        isGeneratingReport = false, 
                        reportMessage = result.summary, 
                        showReportDialog = true, 
                        reportData = result.filePath
                    ) }
                }?.onFailure {
                    _state.update { it.copy(isGeneratingReport = false, reportMessage = "Error al generar reporte") }
                } ?: run {
                     _state.update { it.copy(isGeneratingReport = false, reportMessage = "Servicio no disponible") }
                }
            }
        }
    }

    private fun generateFleetReport() {
        viewModelScope.launch {
            _state.update { it.copy(isGeneratingReport = true, reportMessage = "Analizando estado de flota...") }
            val trucks = _state.value.trucks
            reportService?.generateFleetReport(trucks)?.onSuccess { result ->
                _state.update { it.copy(
                    isGeneratingReport = false, 
                    reportMessage = result.summary, 
                    showReportDialog = true, 
                    reportData = result.filePath
                ) }
            } ?: run {
                _state.update { it.copy(isGeneratingReport = false, reportMessage = "Error en el sistema de reportes") }
            }
        }
    }

    private fun generateDriversReport() {
        viewModelScope.launch {
            _state.update { it.copy(isGeneratingReport = true, reportMessage = "Exportando personal...") }
            val drivers = _state.value.drivers
            reportService?.generateDriversReport(drivers)?.onSuccess { result ->
                _state.update { it.copy(
                    isGeneratingReport = false, 
                    reportMessage = result.summary, 
                    showReportDialog = true, 
                    reportData = result.filePath
                ) }
            } ?: run {
                _state.update { it.copy(isGeneratingReport = false, reportMessage = "No se pudo exportar personal") }
            }
        }
    }

    private fun loadStats() {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val startOfDay = Clock.System.now().toEpochMilliseconds() - (now.hour * 3600 + now.minute * 60 + now.second) * 1000

        viewModelScope.launch {
            repository.getAdminProfile().collectLatest { admin ->
                if (admin == null) {
                    _state.update { it.copy(admin = null) }
                    return@collectLatest
                }
                
                combine(
                    repository.getTruckCount(),
                    repository.getDriverCount(),
                    tripDao.getActiveTrips(admin.id),
                    tripDao.getAllTrips(admin.id),
                    expenseDao.getTodayTotalExpenses(admin.id, startOfDay),
                    repository.getTrucks(),
                    repository.getDrivers()
                ) { flows ->
                    val truckCount = flows[0] as? Int ?: 0
                    val driverCount = flows[1] as? Int ?: 0
                    @Suppress("UNCHECKED_CAST")
                    val activeTrips = flows[2] as? List<truck.project.features.trips.data.local.TripEntity> ?: emptyList()
                    @Suppress("UNCHECKED_CAST")
                    val allTrips = flows[3] as? List<truck.project.features.trips.data.local.TripEntity> ?: emptyList()
                    val expenses = flows[4] as? Double ?: 0.0
                    @Suppress("UNCHECKED_CAST")
                    val trucks = flows[5] as? List<Truck> ?: emptyList()
                    @Suppress("UNCHECKED_CAST")
                    val drivers = flows[6] as? List<Driver> ?: emptyList()

                    val plannedTrips = allTrips.filter { it.status == "PLANNED" }

                    AdminDashboardState(
                        admin = admin,
                        truckCount = truckCount,
                        driverCount = driverCount,
                        activeTripsCount = activeTrips.size,
                        activeTrips = activeTrips,
                        plannedTrips = plannedTrips,
                        todayExpenses = expenses,
                        trucks = trucks,
                        drivers = drivers
                    )
                }.collect { newState ->
                    _state.value = newState
                }
            }
        }
    }
}
