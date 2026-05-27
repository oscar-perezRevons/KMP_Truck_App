package truck.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import truck.project.features.admin.presentation.dashboard.AdminDashboardScreen
import truck.project.features.admin.presentation.dashboard.AdminDashboardViewModel
import truck.project.features.admin.presentation.forms.NewDriverScreen
import truck.project.features.admin.presentation.forms.NewTruckScreen
import truck.project.features.admin.presentation.forms.NewRouteScreen
import truck.project.features.admin.presentation.monitor.TripMonitorScreen
import truck.project.features.admin.presentation.profile.AdminProfileScreen
import truck.project.features.auth.presentation.register.AdminRegisterScreen
import truck.project.features.auth.presentation.login.AdminLoginEffect
import truck.project.features.auth.presentation.login.AdminLoginScreen
import truck.project.features.auth.presentation.login.AdminLoginViewModel
import truck.project.features.auth.presentation.selection.SelectionEffect
import truck.project.features.auth.presentation.selection.SelectionScreen
import truck.project.features.auth.presentation.selection.SelectionViewModel
import truck.project.features.driver.presentation.login.DriverPinScreen
import truck.project.features.driver.presentation.trip.DriverAssignedTripScreen
import truck.project.features.driver.presentation.dispatch.DriverDispatchScreen
import truck.project.features.driver.presentation.trip.DriverEnRouteScreen
import truck.project.features.driver.presentation.trip.DriverCloseTripScreen
import truck.project.features.driver.presentation.trip.DriverTripViewModel
import truck.project.presentation.MaintenanceScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    isMaintenanceMode: Boolean,
    onSkipMaintenance: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (isMaintenanceMode) Screens.Maintenance.route else Screens.Selection.route,
        modifier = modifier
    ) {
        composable(Screens.Selection.route) {
            val viewModel: SelectionViewModel = koinViewModel()
            val effect by viewModel.effect.collectAsState(initial = null)

            LaunchedEffect(effect) {
                when (effect) {
                    SelectionEffect.NavigateToAdminLogin -> navController.navigate(Screens.AdminLogin.route)
                    SelectionEffect.NavigateToDriverLogin -> navController.navigate(Screens.DriverPin.route)
                    null -> {}
                }
            }

            SelectionScreen(
                onSelectDriver = { viewModel.onIntent(truck.project.features.auth.presentation.selection.SelectionIntent.SelectDriver) },
                onSelectAdmin = { viewModel.onIntent(truck.project.features.auth.presentation.selection.SelectionIntent.SelectAdmin) }
            )
        }

        composable(Screens.AdminLogin.route) {
            val viewModel: AdminLoginViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            val effect by viewModel.effect.collectAsState(initial = null)

            LaunchedEffect(effect) {
                when (effect) {
                    AdminLoginEffect.NavigateToDashboard -> {
                        navController.navigate(Screens.AdminDashboard.route) {
                            popUpTo(Screens.Selection.route) { inclusive = true }
                        }
                    }
                    AdminLoginEffect.NavigateBack -> navController.popBackStack()
                    null -> {}
                }
            }

            AdminLoginScreen(
                state = state,
                onIntent = viewModel::onIntent
            )
        }

        composable(Screens.AdminDashboard.route) {
            val viewModel: AdminDashboardViewModel = koinViewModel()
            AdminDashboardScreen(
                viewModel = viewModel,
                onNavigateToFleet = { navController.navigate(Screens.AddTruck.route) },
                onNavigateToDrivers = { navController.navigate(Screens.AddDriver.route) },
                onNavigateToTrips = { navController.navigate(Screens.TripMonitor.route) },
                onNavigateToNewTruck = { navController.navigate(Screens.AddTruck.route) },
                onNavigateToNewDriver = { navController.navigate(Screens.AddDriver.route) },
                onNavigateToNewRoute = { navController.navigate(Screens.NewRoute.route) },
                onLogout = {
                    navController.navigate(Screens.Selection.route) {
                        popUpTo(Screens.AdminDashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screens.AddTruck.route) {
            NewTruckScreen(onBack = { navController.popBackStack() })
        }

        composable(Screens.AddDriver.route) {
            NewDriverScreen(onBack = { navController.popBackStack() })
        }

        composable(Screens.NewRoute.route) {
            NewRouteScreen(
                onBack = { navController.popBackStack() },
                onAssign = { navController.popBackStack() }
            )
        }

        composable(Screens.TripMonitor.route) {
            TripMonitorScreen(onBack = { navController.popBackStack() })
        }

        composable(Screens.DriverPin.route) {
            DriverPinScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = { navController.navigate(Screens.DriverAssignedTrip.route) }
            )
        }

        composable(Screens.DriverAssignedTrip.route) {
            val viewModel = koinViewModel<DriverTripViewModel>()
            LaunchedEffect(Unit) {
                viewModel.loadAssignedTrip("driver_123")
            }
            DriverAssignedTripScreen(
                onBack = { navController.popBackStack() },
                onStartInspection = { navController.navigate(Screens.DriverDispatch.route) }
            )
        }

        composable(Screens.DriverDispatch.route) {
            DriverDispatchScreen(
                onBack = { navController.popBackStack() },
                onStartTrip = { navController.navigate(Screens.DriverEnRoute.route) }
            )
        }

        composable(Screens.DriverEnRoute.route) {
            DriverEnRouteScreen(
                onNotifyArrival = { navController.navigate(Screens.DriverCloseTrip.route) },
                onReportStop = { }
            )
        }

        composable(Screens.DriverCloseTrip.route) {
            val viewModel = koinViewModel<DriverTripViewModel>()
            val state by viewModel.state.collectAsState()
            
            LaunchedEffect(state.isTripFinished) {
                if (state.isTripFinished) {
                    navController.navigate(Screens.Selection.route) {
                        popUpTo(Screens.Selection.route) { inclusive = true }
                    }
                }
            }

            DriverCloseTripScreen(
                onBack = { navController.popBackStack() },
                onFinish = { viewModel.finishTrip(100.0, "signature_url_placeholder") }
            )
        }

        composable(Screens.Maintenance.route) {
            MaintenanceScreen(onSkip = onSkipMaintenance)
        }
    }
}
