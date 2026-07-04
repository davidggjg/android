package com.mtmanagerpro.fileindex

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file_index")
data class FileEntry(
    @PrimaryKey val path: String,
    val name: String,
    val hash: String,
    val size: Long,
    val lastModified: Long
)
