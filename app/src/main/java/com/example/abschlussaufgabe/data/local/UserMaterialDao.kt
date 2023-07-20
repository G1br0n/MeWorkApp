package com.example.abschlussaufgabe.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.abschlussaufgabe.data.model.UserMaterialModel

@Dao
interface UserMaterialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userMaterial: UserMaterialModel)

    @Update
    suspend fun update(userMaterial: UserMaterialModel)

    @Query("SELECT * FROM UserMaterialModel")
    fun getAll(): LiveData<List<UserMaterialModel>>

    @Query("SELECT * FROM UserMaterialModel WHERE idMaterial = :id")
    fun getById(id: Int): LiveData<UserMaterialModel>

    @Query("DELETE FROM UserMaterialModel")
    suspend fun deleteAll()
}