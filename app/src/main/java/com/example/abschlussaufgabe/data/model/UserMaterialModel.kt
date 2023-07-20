package com.example.abschlussaufgabe.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserMaterialModel(

    @PrimaryKey(autoGenerate = true)
    val idMaterial: Int,

    val name: String,
    val locationId: Int
)