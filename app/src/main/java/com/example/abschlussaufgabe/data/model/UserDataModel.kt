package com.example.abschlussaufgabe.data.model



import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


data class UserDataModel(
    val userId: Int,
    val userFirstName: String,
    val userLastName: String,
    val userBirthDate: String,

    val userImage: Int,

    val userLogIn: String,
    val userPassword: String,

    val userQualification: List<String>,
    val userQualificationFit: List<LocalDate>,
    
    val userBaNumber: Int,
    val userBupNumber: Int,

    var haveTheCar: Boolean,

)
