package com.example.abschlussaufgabe.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.abschlussaufgabe.data.model.StorageMaterialModel


@Database(entities = [StorageMaterialModel::class], version = 1)
abstract class StorageMaterialDatabase: RoomDatabase() {
    abstract val storageMaterialDao: StorageMaterialDao
}

private lateinit var INSTANCE: StorageMaterialDatabase

fun getStorageMaterialDatabase(context: Context): StorageMaterialDatabase {
    synchronized(StorageMaterialDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                StorageMaterialDatabase::class.java,
                "storage_material_database"
            )
                .build()
        }
    }
    return INSTANCE
}