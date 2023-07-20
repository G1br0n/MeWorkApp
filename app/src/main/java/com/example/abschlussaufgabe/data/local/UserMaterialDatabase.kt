package com.example.abschlussaufgabe.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.abschlussaufgabe.data.model.UserMaterialModel

@Database(entities = [UserMaterialModel::class], version = 1)
abstract class UserMaterialDatabase: RoomDatabase() {
    abstract val userMaterialDao: UserMaterialDao
}

private lateinit var INSTANCE: UserMaterialDatabase

fun getUserMaterialDatabase(context: Context): UserMaterialDatabase {
    synchronized(UserMaterialDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                UserMaterialDatabase::class.java,
                "user_material_database"
            )
                .build()
        }
    }
    return INSTANCE
}