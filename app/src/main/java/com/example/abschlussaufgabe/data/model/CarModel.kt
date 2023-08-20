package com.example.abschlussaufgabe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class CarModel (
    @PrimaryKey(autoGenerate = true)
    val carId: Int,
    var locationId:  Int,
    var statusDrive: Boolean,

    var oilControlDate: Date,
    var oilControlMileage: Int,

    var refuelDate: Date,
    var refuelMileage: Int,

    var startDrivingDate: Date,
    var startDrivingMileage: Int,

    var stopDrivingDate: Date,
    var stopDrivingMileage: Int,

    var getInCarDate: Date,
    var getInCarMileage: Int,

    var getOutCarDate: Date,
    var getOutCarMileage: Int,
        ){

}