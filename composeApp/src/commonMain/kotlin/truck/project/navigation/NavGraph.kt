package truck.project.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.compose.viewmodel.koinViewModel
import truck.project.features.admin.presentation.dashboard.AdminDashboardScreen
import truck.project.features.admin.presentation.dashboard.AdminDashboardViewModel
import truck.project.features.admin.presentation.dashboard.AdminDashboardIntent
import truck.project.features.admin.presentation.dashboard.AdminDashboardEffect
import truck.project.features.admin.presentation.forms.NewDriverScreen
import truck.project.features.admin.presentation.forms.NewTruckScreen
import truck.project.features.admin.presentation.forms.NewRouteScreen
import truck.project.features.admin.presentation.forms.NewDriverViewModel
import truck.project.features.admin.presentation.forms.NewTruckViewModel
import truck.project.features.admin.presentation.forms.NewRouteViewModel
import truck.project.features.admin.presentation.forms.AssignTripScreen
import truck.project.features.admin.presentation.forms.AssignTripViewModel
import truck.project.features.admin.presentation.monitor.TripMonitorScreen
import truck.project.features.admin.presentation.showroom.VolvoShowroomScreen
import truck.project.features.driver.presentation.history.DriverHistoryScreen
import truck.project.features.driver.presentation.stats.DriverStatsScreen
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.admin.presentation.profile.AdminProfileScreen
import truck.project.features.driver.presentation.profile.DriverProfileScreen
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
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (isMaintenanceMode) Screens.Maintenance.route else Screens.Selection.route,
        modifier = modifier,
        enterTransition = { 
            fadeIn(animationSpec = tween(700)) + 
            slideInHorizontally(animationSpec = tween(700, easing = FastOutSlowInEasing)) { it } +
            scaleIn(initialScale = 0.9f, animationSpec = tween(700))
        },
        exitTransition = { 
            fadeOut(animationSpec = tween(700)) + 
            slideOutHorizontally(animationSpec = tween(700, easing = FastOutSlowInEasing)) { -it } +
            scaleOut(targetScale = 1.1f, animationSpec = tween(700))
        },
        popEnterTransition = { 
            fadeIn(animationSpec = tween(700)) + 
            slideInHorizontally(animationSpec = tween(700, easing = FastOutSlowInEasing)) { -it } +
            scaleIn(initialScale = 1.1f, animationSpec = tween(700))
        },
        popExitTransition = { 
            fadeOut(animationSpec = tween(700)) + 
            slideOutHorizontally(animationSpec = tween(700, easing = FastOutSlowInEasing)) { it } +
            scaleOut(targetScale = 0.9f, animationSpec = tween(700))
        }
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
                onSelectAdmin = { viewModel.onIntent(truck.project.features.auth.presentation.selection.SelectionIntent.SelectAdmin) },
                onToggleTheme = onToggleTheme,
                onToggleLanguage = onToggleLanguage
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
                    AdminLoginEffect.NavigateToRegister -> navController.navigate(Screens.AdminRegister.route)
                    AdminLoginEffect.NavigateBack -> navController.popBackStack()
                    null -> {}
                }
            }

            AdminLoginScreen(
                state = state,
                onIntent = viewModel::onIntent
            )
        }

        composable(Screens.AdminRegister.route) {
            AdminRegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screens.AdminDashboard.route) {
                        popUpTo(Screens.Selection.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screens.AdminDashboard.route) {
            val viewModel: AdminDashboardViewModel = koinViewModel()
            val effect by viewModel.effect.collectAsState(initial = null)

            LaunchedEffect(effect) {
                when (effect) {
                    AdminDashboardEffect.NavigateToFleet -> navController.navigate(Screens.AddTruck.route)
                    AdminDashboardEffect.NavigateToDrivers -> navController.navigate(Screens.AddDriver.route)
                    AdminDashboardEffect.NavigateToTrips -> navController.navigate(Screens.TripMonitor.route)
                    AdminDashboardEffect.NavigateToProfile -> navController.navigate(Screens.AdminProfile.route)
                    AdminDashboardEffect.NavigateToLogin -> {
                        navController.navigate(Screens.Selection.route) {
                            popUpTo(Screens.AdminDashboard.route) { inclusive = true }
                        }
                    }
                    else -> {}
                }
            }

            AdminDashboardScreen(
                viewModel = viewModel,
                onNavigateToFleet = { viewModel.onIntent(AdminDashboardIntent.NavigateToFleet) },
                onNavigateToDrivers = { viewModel.onIntent(AdminDashboardIntent.NavigateToDrivers) },
                onNavigateToTrips = { viewModel.onIntent(AdminDashboardIntent.NavigateToTrips) },
                onNavigateToNewTruck = { truck ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("truckIdToEdit", truck?.id)
                    navController.navigate(Screens.AddTruck.route)
                },
                onNavigateToNewDriver = { driver ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("driverIdToEdit", driver?.id)
                    navController.navigate(Screens.AddDriver.route)
                },
                onNavigateToNewRoute = { tripId, origin, destination ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("tripIdToEdit", tripId)
                    navController.currentBackStackEntry?.savedStateHandle?.set("originToEdit", origin)
                    navController.currentBackStackEntry?.savedStateHandle?.set("destinationToEdit", destination)
                    navController.navigate(Screens.NewRoute.route)
                },
                onNavigateToAssignTrip = { tripId ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("tripIdToAssign", tripId)
                    navController.navigate(Screens.AssignTrip.route)
                },
                onNavigateToProfile = { navController.navigate(Screens.AdminProfile.route) },
                onNavigateToShowroom = { navController.navigate(Screens.VolvoShowroom.route) },
                onLogout = { viewModel.onIntent(AdminDashboardIntent.Logout) }
            )
        }

        composable(Screens.AdminProfile.route) {
            AdminProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screens.Selection.route) {
                        popUpTo(Screens.AdminDashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screens.AddTruck.route) {
            val viewModel: NewTruckViewModel = koinViewModel()
            val truckId = navController.previousBackStackEntry?.savedStateHandle?.get<String>("truckIdToEdit")
            
            LaunchedEffect(truckId) {
                truckId?.let { viewModel.loadTruckById(it) }
            }
            
            NewTruckScreen(onBack = { 
                navController.previousBackStackEntry?.savedStateHandle?.remove<String>("truckIdToEdit")
                navController.popBackStack() 
            }, viewModel = viewModel)
        }

        composable(Screens.AddDriver.route) {
            val viewModel: NewDriverViewModel = koinViewModel()
            val driverId = navController.previousBackStackEntry?.savedStateHandle?.get<String>("driverIdToEdit")
            
            LaunchedEffect(driverId) {
                driverId?.let { viewModel.loadDriverById(it) }
            }
            
            NewDriverScreen(onBack = { 
                navController.previousBackStackEntry?.savedStateHandle?.remove<String>("driverIdToEdit")
                navController.popBackStack() 
            }, viewModel = viewModel)
        }

        composable(Screens.NewRoute.route) {
            val viewModel: NewRouteViewModel = koinViewModel()
            val tripId = navController.previousBackStackEntry?.savedStateHandle?.get<String>("tripIdToEdit")
            val origin = navController.previousBackStackEntry?.savedStateHandle?.get<String>("originToEdit")
            val destination = navController.previousBackStackEntry?.savedStateHandle?.get<String>("destinationToEdit")

            LaunchedEffect(tripId) {
                if (tripId != null && origin != null && destination != null) {
                    viewModel.setEditTrip(tripId, origin, destination)
                }
            }

            NewRouteScreen(
                onBack = { 
                    navController.previousBackStackEntry?.savedStateHandle?.remove<String>("tripIdToEdit")
                    navController.popBackStack() 
                },
                onAssign = { 
                    navController.previousBackStackEntry?.savedStateHandle?.remove<String>("tripIdToEdit")
                    navController.popBackStack() 
                },
                viewModel = viewModel
            )
        }

        composable(Screens.AssignTrip.route) {
            val tripId = navController.previousBackStackEntry?.savedStateHandle?.get<String>("tripIdToAssign")
            if (tripId != null) {
                AssignTripScreen(
                    tripId = tripId,
                    onBack = { navController.popBackStack() },
                    onSuccess = { navController.popBackStack() }
                )
            }
        }

        composable(Screens.TripMonitor.route) {
            TripMonitorScreen(onBack = { navController.popBackStack() })
        }

        composable(Screens.VolvoShowroom.route) {
            VolvoShowroomScreen(onBack = { navController.popBackStack() })
        }

        composable(Screens.DriverHistory.route) {
            DriverHistoryScreen(onBack = { navController.popBackStack() })
        }

        composable(Screens.DriverStats.route) {
            DriverStatsScreen(onBack = { navController.popBackStack() })
        }

        composable(Screens.DriverPin.route) {
            val viewModel: DriverTripViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(state.loginSuccess) {
                if (state.loginSuccess) {
                    navController.navigate(Screens.DriverAssignedTrip.route + "/${state.loggedDriverId}") {
                        popUpTo(Screens.Selection.route) { inclusive = true }
                    }
                }
            }

            DriverPinScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = { /* Handled by LaunchedEffect */ },
                viewModel = viewModel
            )
        }

        composable(
            route = Screens.DriverAssignedTrip.route + "/{driverId}",
            arguments = listOf(navArgument("driverId") { type = NavType.StringType })
        ) { backStackEntry ->
            val driverId = backStackEntry.savedStateHandle.get<String>("driverId") ?: ""
            val viewModel: DriverTripViewModel = koinViewModel()
            
            LaunchedEffect(driverId) {
                viewModel.loadAssignedTrip(driverId)
            }
            
            DriverAssignedTripScreen(
                onBack = { navController.popBackStack() },
                onStartInspection = { 
                    val trip = viewModel.state.value.currentTrip
                    if (trip?.status == truck.project.features.driver.domain.model.TripStatus.EN_ROUTE) {
                        navController.navigate(Screens.DriverEnRoute.route + "/$driverId")
                    } else {
                        navController.navigate(Screens.DriverDispatch.route + "/$driverId")
                    }
                },
                onProfile = { id -> navController.navigate(Screens.DriverProfile.route + "/$id") },
                onHistory = { navController.navigate(Screens.DriverHistory.route) },
                onStats = { navController.navigate(Screens.DriverStats.route) },
                viewModel = viewModel
            )
        }

        composable(
            route = Screens.DriverDispatch.route + "/{driverId}",
            arguments = listOf(navArgument("driverId") { type = NavType.StringType })
        ) { backStackEntry ->
            val driverId = backStackEntry.savedStateHandle.get<String>("driverId") ?: ""
            val viewModel: DriverTripViewModel = koinViewModel()
            LaunchedEffect(driverId) { viewModel.loadAssignedTrip(driverId) }
            
            DriverDispatchScreen(
                onBack = { navController.popBackStack() },
                onStartTrip = { navController.navigate(Screens.DriverEnRoute.route + "/$driverId") },
                viewModel = viewModel
            )
        }

        composable(
            route = Screens.DriverEnRoute.route + "/{driverId}",
            arguments = listOf(navArgument("driverId") { type = NavType.StringType })
        ) { backStackEntry ->
            val driverId = backStackEntry.savedStateHandle.get<String>("driverId") ?: ""
            val viewModel: DriverTripViewModel = koinViewModel()
            LaunchedEffect(driverId) { viewModel.loadAssignedTrip(driverId) }

            DriverEnRouteScreen(
                onNotifyArrival = { navController.navigate(Screens.DriverCloseTrip.route + "/$driverId") },
                onReportStop = { },
                viewModel = viewModel
            )
        }

        composable(
            route = Screens.DriverCloseTrip.route + "/{driverId}",
            arguments = listOf(navArgument("driverId") { type = NavType.StringType })
        ) { backStackEntry ->
            val driverId = backStackEntry.savedStateHandle.get<String>("driverId") ?: ""
            val viewModel: DriverTripViewModel = koinViewModel()
            LaunchedEffect(driverId) { viewModel.loadAssignedTrip(driverId) }

            DriverCloseTripScreen(
                onBack = { navController.popBackStack() },
                onFinish = {
                    navController.navigate(Screens.Selection.route) {
                        popUpTo(Screens.Selection.route) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Screens.DriverProfile.route + "/{driverId}",
            arguments = listOf(navArgument("driverId") { type = NavType.StringType })
        ) { backStackEntry ->
            val driverId = backStackEntry.savedStateHandle.get<String>("driverId") ?: ""
            val viewModel: DriverTripViewModel = koinViewModel()
            LaunchedEffect(driverId) { 
                viewModel.loadDriverProfile(driverId) 
            }
            
            DriverProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screens.Selection.route) {
                        popUpTo(Screens.Selection.route) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

        composable(Screens.Maintenance.route) {
            MaintenanceScreen(onSkip = onSkipMaintenance)
        }
    }
}
