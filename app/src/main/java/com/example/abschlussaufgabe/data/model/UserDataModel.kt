package com.example.abschlussaufgabe.data.model



import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


data class UserDataModel(
   val userUid: String,

    val userId: Long,
    val userFirstName: String,
    val userLastName: String,
    val userBirthDate: String,


    //todo variable IMG
    //val userImage: Int,

    val userLogIn: String,
    val userPassword: String,

    val userQualification: Array<String>,
    val userQualificationFit: Array<Timestamp>,
    
    val userBaNumber: Long,
    val userBupNumber: Long,

    var haveTheCar: Boolean,

)
