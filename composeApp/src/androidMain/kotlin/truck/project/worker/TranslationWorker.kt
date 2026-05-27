package truck.project.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import android.util.Log
import truck.project.features.fleet.data.local.TruckDao
import truck.project.core.data.remote.TranslationService
import java.util.Locale

class TranslationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val truckDao: TruckDao by inject()
    private val translationService: TranslationService by inject()

    override suspend fun doWork(): Result {
        Log.d("TranslationWorker", "Iniciando trabajo de traducción...")
        val trucksToTranslate = truckDao.getTrucksToTranslateOnce()
        
        if (trucksToTranslate.isEmpty()) {
            Log.d("TranslationWorker", "No hay camiones para traducir.")
            return Result.success()
        }

        Log.d("TranslationWorker", "Encontrados ${trucksToTranslate.size} camiones para traducir.")
        val currentLanguage = Locale.getDefault().language

        trucksToTranslate.forEach { truck ->
            val result = translationService.translate(truck.status, currentLanguage)
            
            if (result != null && !result.startsWith("ERROR_LOG_INTERNAL:")) {
                truckDao.update(
                    truck.copy(
                        statusTranslated = result,
                        needsTranslation = false
                    )
                )
            }
        }

        return Result.success()
    }
}
