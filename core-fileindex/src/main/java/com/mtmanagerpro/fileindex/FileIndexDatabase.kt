package com.mtmanagerpro.fileindex

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FileEntry::class], version = 1)
abstract class FileIndexDatabase : RoomDatabase() {
    abstract fun fileIndexDao(): FileIndexDao

    companion object {
        @Volatile private var INSTANCE: FileIndexDatabase? = null

        fun getInstance(context: Context): FileIndexDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    FileIndexDatabase::class.java,
                    "file_index.db"
                ).build().also { INSTANCE = it }
            }
    }
}
