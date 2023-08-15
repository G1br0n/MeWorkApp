package com.example.abschlussaufgabe.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.abschlussaufgabe.data.model.StorageMaterialModel

@Dao
interface StorageMaterialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(storageMaterial: StorageMaterialModel)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(storageMaterialList: List<StorageMaterialModel>)


    @Query("UPDATE StorageMaterialModel SET locationId = :locationId WHERE materialId = :materialId")
    suspend fun updateStorage(materialId: Int, locationId: Int)

    @Query("SELECT * FROM StorageMaterialModel")
    suspend fun getAll(): List<StorageMaterialModel>

    @Query("SELECT * FROM StorageMaterialModel WHERE locationId = :id")
    suspend fun getById(id: Int): List<StorageMaterialModel>

    @Query("DELETE FROM StorageMaterialModel")
    suspend fun deleteAll()

    @Query("SELECT * FROM StorageMaterialModel WHERE locationId = :userId")
    suspend fun getMaterialsByUserId(userId: String): List<StorageMaterialModel>

    @Query("SELECT COUNT(*) FROM StorageMaterialModel")
    suspend fun getCount():Int
}