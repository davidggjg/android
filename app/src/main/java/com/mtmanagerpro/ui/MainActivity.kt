package com.mtmanagerpro.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.mtmanagerpro.R
import com.mtmanagerpro.zipeditor.ZipExtractor
import java.io.File

class MainActivity : AppCompatActivity() {

    private val zipExtractor = ZipExtractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** דוגמה: פתיחת ZIP גדול, חילוץ, פתיחה ב-EditorActivity */
    private fun openZipForEditing(zipPath: String) {
        val workDir = File(cacheDir, "extracted_" + File(zipPath).nameWithoutExtension)
        val files = zipExtractor.extract(zipPath, workDir.absolutePath)

        val intent = Intent(this, EditorActivity::class.java).apply {
            putExtra(EditorActivity.EXTRA_ZIP_PATH, zipPath)
            putExtra(EditorActivity.EXTRA_WORK_DIR, workDir.absolutePath)
            putStringArrayListExtra(
                EditorActivity.EXTRA_FILE_LIST,
                ArrayList(files.map { it.absolutePath })
            )
        }
        startActivity(intent)
    }

    /** "פתח עם אפליקציה מותקנת" - מציג את בורר האפליקציות של אנדרואיד */
    private fun openWithInstalledApp(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            this, "com.mtmanagerpro.fileprovider", file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, contentResolver.getType(uri) ?: "*/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "פתח עם"))
    }
}
