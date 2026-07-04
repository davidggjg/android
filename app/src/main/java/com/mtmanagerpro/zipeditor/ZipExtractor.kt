package com.mtmanagerpro.zipeditor

import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.File
import java.nio.charset.Charset

/**
 * חילוץ ובנייה מחדש של ארכיוני ZIP/APK תוך שמירה מלאה על שמות הקבצים
 * המקוריים (כולל עברית ותווי יוניקוד), במקום להשתמש ב-ZipInputStream
 * הרגיל של Java שחותך/משבש שמות לא-ASCII.
 */
class ZipExtractor {

    /** מחלץ ארכיון שלם לתיקיית עבודה, שומר על השמות המקוריים */
    fun extract(zipPath: String, destDir: String): List<File> {
        val zipFile = ZipFile(zipPath)
        zipFile.charset = Charset.forName("UTF-8")
        val target = File(destDir).apply { mkdirs() }
        zipFile.extractAll(target.absolutePath)
        return target.walkTopDown().filter { it.isFile }.toList()
    }

    /** בונה מחדש ZIP חדש מתיקיית העבודה, עם אותם שמות קבצים מקוריים */
    fun repack(sourceDir: String, outputZipPath: String) {
        val outputFile = File(outputZipPath)
        if (outputFile.exists()) outputFile.delete()

        val zipFile = ZipFile(outputFile)
        zipFile.charset = Charset.forName("UTF-8")

        val params = ZipParameters().apply {
            encryptionMethod = EncryptionMethod.NONE
            isEncryptFiles = false
        }

        File(sourceDir).listFiles()?.forEach { entry ->
            if (entry.isDirectory) {
                zipFile.addFolder(entry, params)
            } else {
                zipFile.addFile(entry, params)
            }
        }
    }

    /** עדכון קובץ בודד בתוך ZIP קיים בלי לפרק את כולו מחדש */
    fun updateSingleFile(zipPath: String, editedFile: File) {
        val zipFile = ZipFile(zipPath)
        zipFile.charset = Charset.forName("UTF-8")
        val params = ZipParameters()
        zipFile.addFile(editedFile, params) // דורס entry קיים עם אותו שם
    }
}
