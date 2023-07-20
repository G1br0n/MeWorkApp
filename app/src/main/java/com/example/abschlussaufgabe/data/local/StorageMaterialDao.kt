package com.example.abschlussaufgabe.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.data.model.UserMaterialModel

@Dao
interface StorageMaterialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(storageMaterial: StorageMaterialModel)

    @Update
    suspend fun update(storageMaterial: StorageMaterialModel)

    @Query("SELECT * FROM StorageMaterialModel")
    fun getAll(): LiveData<List<StorageMaterialModel>>

    @Query("SELECT * FROM StorageMaterialModel WHERE idMaterial = :id")
    fun getById(id: Int): LiveData<StorageMaterialModel>

    @Query("DELETE FROM StorageMaterialModel")
    suspend fun deleteAll()
}