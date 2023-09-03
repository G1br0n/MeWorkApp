package com.example.abschlussaufgabe.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.abschlussaufgabe.data.model.StorageMaterialModel

/**
 * ## Information
 * ### Die Datenbankklasse `StorageMaterialDatabase` repräsentiert die Raumdatenbank für Lagermaterialien.
 *
 * Diese Datenbank enthält eine Tabelle für Lagermaterialien und verwendet das [StorageMaterialModel] als Entity.
 *
 * @property storageMaterialDao Das Data Access Object (DAO) für den Zugriff auf die Lagermaterialien-Tabelle.
 */
@Database(entities = [StorageMaterialModel::class], version = 2)
abstract class StorageMaterialDatabase : RoomDatabase() {
    abstract val storageMaterialDao: StorageMaterialDao
}

private lateinit var INSTANCE: StorageMaterialDatabase

/**
 * ## Information
 * ### Die [getStorageMaterialDatabase] Funktion erstellt oder gibt eine Instanz der [StorageMaterialDatabase] zurück.
 *
 * Diese Funktion stellt sicher, dass nur eine Instanz der Datenbank in der App vorhanden ist.
 *
 * @param context Der Anwendungskontext.
 * @return Eine Instanz der [StorageMaterialDatabase].
 */
fun getStorageMaterialDatabase(context: Context): StorageMaterialDatabase {
    synchronized(StorageMaterialDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                StorageMaterialDatabase::class.java,
                "storage_material_table"
            )
                .fallbackToDestructiveMigration()  // Fügt eine destruktive Migration hinzu, um Datenbankänderungen durchzuführen.
                .build()
        }
    }
    return INSTANCE
}
