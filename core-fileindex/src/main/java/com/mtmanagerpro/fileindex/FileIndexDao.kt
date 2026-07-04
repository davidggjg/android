package com.mtmanagerpro.fileindex

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FileIndexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<FileEntry>)

    // חיפוש חכם: לפי hash זהה (עותק מדויק) או לפי שם דומה (fuzzy עם LIKE)
    @Query("SELECT * FROM file_index WHERE hash = :hash OR name LIKE '%' || :nameQuery || '%'")
    suspend fun searchByHashOrName(hash: String, nameQuery: String): List<FileEntry>

    @Query("SELECT * FROM file_index WHERE name LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<FileEntry>
}
