package com.mtmanagerpro.search

import com.mtmanagerpro.fileindex.FileEntry
import com.mtmanagerpro.fileindex.FileIndexDao
import java.io.File
import java.security.MessageDigest

/**
 * חיפוש חכם: כשעורכים קובץ, מוצא קבצים קשורים בכל המכשיר -
 * הן לפי hash זהה (עותק מדויק) והן לפי שם דומה.
 */
class SmartFileSearch(private val dao: FileIndexDao) {

    suspend fun findRelated(currentFile: File): List<FileEntry> {
        val hash = currentFile.sha256()
        val baseName = currentFile.nameWithoutExtension
        return dao.searchByHashOrName(hash, baseName)
            .filter { it.path != currentFile.absolutePath }
    }

    private fun File.sha256(): String {
        val digest = MessageDigest.getInstance("SHA-256")
        inputStream().use { input ->
            val buffer = ByteArray(8192)
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                digest.update(buffer, 0, read)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}
