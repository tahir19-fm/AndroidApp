package com.example.androidapp.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsModel::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appDao(): AppDatabaseDao
}