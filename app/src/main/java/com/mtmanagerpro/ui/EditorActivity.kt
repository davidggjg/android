package com.mtmanagerpro.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mtmanagerpro.zipeditor.ZipExtractor
import java.io.File

/**
 * מסך עריכת קבצים שחולצו מארכיון. בסיום עריכה, לוחצים "שמור וסיים"
 * וזה בונה מחדש ZIP מלא עם השמות המקוריים, מוכן להורדה/התקנה.
 */
class EditorActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ZIP_PATH = "extra_zip_path"
        const val EXTRA_WORK_DIR = "extra_work_dir"
        const val EXTRA_FILE_LIST = "extra_file_list"
    }

    private val zipExtractor = ZipExtractor()
    private lateinit var workDir: String
    private lateinit var originalZipPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workDir = intent.getStringExtra(EXTRA_WORK_DIR) ?: return
        originalZipPath = intent.getStringExtra(EXTRA_ZIP_PATH) ?: return
        // setContentView(R.layout.activity_editor)
        // TODO: לטעון רשימת קבצים לרשימה/RecyclerView ולפתוח עורך טקסט/הקסה מתאים
    }

    /** נקרא כשהמשתמש לוחץ "סיום ושמירה" */
    private fun finishAndRepack(outputFileName: String) {
        val outputDir = File(cacheDir, "output").apply { mkdirs() }
        val outputZip = File(outputDir, outputFileName)
        zipExtractor.repack(workDir, outputZip.absolutePath)
        // הקובץ המוכן נמצא כעת ב-outputZip.absolutePath, מוכן לשיתוף/התקנה
    }
}
