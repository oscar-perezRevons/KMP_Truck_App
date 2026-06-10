package truck.project.core.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class AndroidFileOpener(private val context: Context) : FileOpener {
    override fun openFile(path: String) {
        try {
            val file = File(path)
            if (!file.exists()) return

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(Intent.createChooser(intent, "Abrir Reporte PDF"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
