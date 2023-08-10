package com.example.abschlussaufgabe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class StorageMaterialModel(

    @PrimaryKey(autoGenerate = true)
    val materialId: Int,

    val name: String,
    var locationId: Int
)