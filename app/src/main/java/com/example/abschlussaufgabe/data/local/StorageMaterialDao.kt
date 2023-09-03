package com.example.abschlussaufgabe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.abschlussaufgabe.data.model.StorageMaterialModel

/**
 * Das Datenzugriffsobjekt (DAO) `StorageMaterialDao` definiert Methoden für den Zugriff auf die Lagermaterialien-Tabelle.
 */
@Dao
interface StorageMaterialDao {
    /**
     * Fügt ein Lagermaterial in die Datenbank ein oder aktualisiert es, falls bereits vorhanden.
     * @param storageMaterial Das einzufügende oder zu aktualisierende Lagermaterial.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(storageMaterial: StorageMaterialModel)

    /**
     * Fügt eine Liste von Lagermaterialien in die Datenbank ein oder bricht den Vorgang ab, wenn ein Konflikt auftritt.
     * @param storageMaterialList Die Liste der einzufügenden Lagermaterialien.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(storageMaterialList: List<StorageMaterialModel>)

    /**
     * Aktualisiert die Lokations-ID eines Lagermaterials anhand der Material-ID.
     * @param materialId Die ID des Lagermaterials.
     * @param locationId Die neue Lokations-ID.
     */
    @Query("UPDATE StorageMaterialModel SET locationId = :locationId WHERE materialId = :materialId")
    suspend fun updateStorage(materialId: Int, locationId: String)

    /**
     * Ruft alle Lagermaterialien aus der Datenbank ab.
     * @return Eine Liste der Lagermaterialien.
     */
    @Query("SELECT * FROM StorageMaterialModel")
    suspend fun getAll(): List<StorageMaterialModel>

    /**
     * Ruft ein Lagermaterial anhand seiner Material-ID ab.
     * @param materialId Die ID des gesuchten Lagermaterials.
     * @return Eine Liste des gefundenen Lagermaterials.
     */
    @Query("SELECT * FROM StorageMaterialModel WHERE materialId = :materialId")
    suspend fun getById(materialId: Int): List<StorageMaterialModel>

    /**
     * Löscht alle Lagermaterialien aus der Datenbank.
     */
    @Query("DELETE FROM StorageMaterialModel")
    suspend fun deleteAll()

    /**
     * Ruft alle Lagermaterialien ab, die sich an einer bestimmten Lokation befinden.
     * @param userId Die ID der Lokation.
     * @return Eine Liste der Lagermaterialien an der angegebenen Lokation.
     */
    @Query("SELECT * FROM StorageMaterialModel WHERE locationId = :userId")
    suspend fun getMaterialsByUserId(userId: String): List<StorageMaterialModel>

    /**
     * Ruft die Anzahl der Lagermaterialien in der Datenbank ab.
     * @return Die Anzahl der Lagermaterialien.
     */
    @Query("SELECT COUNT(*) FROM StorageMaterialModel")
    suspend fun getCount(): Int
}
