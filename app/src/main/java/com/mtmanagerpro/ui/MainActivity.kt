package com.mtmanagerpro.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mtmanagerpro.R
import com.mtmanagerpro.zipeditor.ZipExtractor
import java.io.File

class MainActivity : AppCompatActivity() {

    private val zipExtractor = ZipExtractor()
    private lateinit var statusText: TextView
    private lateinit var fileListText: TextView

    // בורר קבצים של אנדרואיד - מאפשר לבחור כל סוג קובץ, כולל apk/zip
    private val pickFileLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            handlePickedFile(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        fileListText = findViewById(R.id.fileListText)

        findViewById<Button>(R.id.pickFileButton).setOnClickListener {
            // "*/*" מציג את כל הקבצים בבורר; המשתמש יבחר גם APK וגם ZIP
            pickFileLauncher.launch(arrayOf("*/*"))
        }
    }

    private fun handlePickedFile(uri: Uri) {
        statusText.text = "מעתיק ומחלץ..."

        // מעתיקים את הקובץ הנבחר (content://) לנתיב אמיתי בקאש, כי ZipExtractor
        // עובד עם File רגיל ולא עם Uri
        val fileName = queryFileName(uri) ?: "picked_file"
        val localCopy = File(cacheDir, fileName)

        contentResolver.openInputStream(uri)?.use { input ->
            localCopy.outputStream().use { output -> input.copyTo(output) }
        }

        val workDir = File(cacheDir, "extracted_" + localCopy.nameWithoutExtension)

        try {
            val extractedFiles = zipExtractor.extract(localCopy.absolutePath, workDir.absolutePath)
            statusText.text = "חולצו ${extractedFiles.size} קבצים מתוך $fileName"
            fileListText.text = extractedFiles.joinToString("\n") { it.relativeTo(workDir).path }
        } catch (e: Exception) {
            statusText.text = "שגיאה בחילוץ: ${e.message}"
        }
    }

    /** שולף את השם המקורי האמיתי של הקובץ (לא נחתך) מתוך ה-Uri */
    private fun queryFileName(uri: Uri): String? {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex >= 0) {
                return cursor.getString(nameIndex)
            }
        }
        return null
    }
}
