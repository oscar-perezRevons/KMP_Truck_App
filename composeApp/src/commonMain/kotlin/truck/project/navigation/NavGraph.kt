package truck.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import truck.project.truck_guia.presentation.screen.TruckScreen
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
        startDestination = if (isMaintenanceMode) Screens.Maintenance.route else Screens.TruckList.route,
        modifier = modifier
    ) {
        composable(Screens.TruckList.route) {
            TruckScreen()
        }
        composable(Screens.Maintenance.route) {
            MaintenanceScreen(onSkip = onSkipMaintenance)
        }
    }
}
