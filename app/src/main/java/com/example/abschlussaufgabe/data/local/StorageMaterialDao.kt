package com.example.abschlussaufgabe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.abschlussaufgabe.data.model.StorageMaterialModel

/**
 * ## Information
 * `StorageMaterialDao` - Datenzugriffsobjekt, das Methoden für den Zugriff auf die Lagermaterialien-Tabelle definiert.
 * ###
 * ## Funktionen
 * - [insert]: Fügt ein Lagermaterial in die Datenbank ein oder aktualisiert es.
 * - [insertAll]: Fügt eine Liste von Lagermaterialien in die Datenbank ein oder bricht den Vorgang bei Konflikten ab.
 * - [updateStorage]: Aktualisiert die Lokations-ID eines Lagermaterials basierend auf der Material-ID.
 * - [getAll]: Ruft alle Lagermaterialien aus der Datenbank ab.
 * - [getById]: Ruft ein bestimmtes Lagermaterial basierend auf seiner Material-ID ab.
 * - [deleteAll]: Löscht alle Lagermaterialien aus der Datenbank.
 * - [getMaterialsByUserId]: Ruft alle Lagermaterialien ab, die sich an einer bestimmten Lokation befinden.
 * - [getCount]: Ruft die Gesamtanzahl der Lagermaterialien in der Datenbank ab.
 */
@Dao
interface StorageMaterialDao {
    /**
     * ## Information
     * Fügt ein Lagermaterial in die Datenbank ein oder aktualisiert es, falls bereits vorhanden.
     * @param storageMaterial Das einzufügende oder zu aktualisierende Lagermaterial.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(storageMaterial: StorageMaterialModel)

    /**
     * ## Information
     * Fügt eine Liste von Lagermaterialien in die Datenbank ein oder bricht den Vorgang ab, wenn ein Konflikt auftritt.
     * @param storageMaterialList Die Liste der einzufügenden Lagermaterialien.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(storageMaterialList: List<StorageMaterialModel>)

    /**
     * ## Information
     * Aktualisiert die Lokations-ID eines Lagermaterials anhand der Material-ID.
     * @param materialId Die ID des Lagermaterials.
     * @param locationId Die neue Lokations-ID.
     */
    @Query("UPDATE StorageMaterialModel SET locationId = :locationId WHERE materialId = :materialId")
    suspend fun updateStorage(materialId: Int, locationId: String)

    /**
     * ## Information
     * Ruft alle Lagermaterialien aus der Datenbank ab.
     * @return List<StorageMaterialModel>.
     */
    @Query("SELECT * FROM StorageMaterialModel")
    suspend fun getAll(): List<StorageMaterialModel>

    /**
     * ## Information
     * Ruft ein Lagermaterial anhand seiner Material-ID ab.
     * @param materialId Die ID des gesuchten Lagermaterials.
     * @return Eine List<StorageMaterialModel> des gefundenen Lagermaterials.
     */
    @Query("SELECT * FROM StorageMaterialModel WHERE materialId = :materialId")
    suspend fun getById(materialId: Int): List<StorageMaterialModel>

    /**
     * ## Information
     * Löscht alle Lagermaterialien aus der Datenbank.
     */
    @Query("DELETE FROM StorageMaterialModel")
    suspend fun deleteAll()

    /**
     * ## Information
     * Ruft alle Lagermaterialien ab, die sich an einer bestimmten Lokation befinden.
     * @param userId Die ID der Lokation.
     * @return List<StorageMaterialModel>.
     */
    @Query("SELECT * FROM StorageMaterialModel WHERE locationId = :userId")
    suspend fun getMaterialsByUserId(userId: String): List<StorageMaterialModel>

    /**
     * ## Information
     * Ruft die Anzahl der Lagermaterialien in der Datenbank ab.
     * @return Int. Die Anzahl der Lagermaterialien.
     */
    @Query("SELECT COUNT(*) FROM StorageMaterialModel")
    suspend fun getCount(): Int
}
