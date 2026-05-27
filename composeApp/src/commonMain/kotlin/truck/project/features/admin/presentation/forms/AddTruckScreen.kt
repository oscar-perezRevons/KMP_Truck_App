package truck.project.features.admin.presentation.forms

import androidx.compose.runtime.Composable
import truck.project.features.fleet.presentation.viewmodel.TruckViewModel

@Composable
fun AddTruckScreen(
    onBack: () -> Unit
) {
    NewTruckScreen(onBack = onBack)
}
