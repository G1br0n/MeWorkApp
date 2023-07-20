package com.example.abschlussaufgabe.data

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussaufgabe.data.local.UserMaterialDatabase
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.data.model.UserMaterialModel
import java.time.LocalDate

class AppRepository(
    private val  userMaterialDatabase: UserMaterialDatabase
) {


    val userMaterialList: LiveData<List<UserMaterialModel>> = userMaterialDatabase.userMaterialDao.getAll()

    //UserList
    private val _user = MutableLiveData<List<UserDataModel>>()
    val user: LiveData<List<UserDataModel>>
        get() = _user

    private val _material = MutableLiveData<List<StorageMaterialModel>>()
    val material: LiveData<List<StorageMaterialModel>>
        get() = _material



    //todo: init block !!!!
    init {
        loadUser()
        loadMaterial()
    }

    private fun loadUser(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _user.value = listOf(
                UserDataModel(1001,"Eugen","Lange","12.08.1989","Gibron", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),listOf(
                    LocalDate.of(2023,5,22),LocalDate.of(2023,5,22),LocalDate.of(2023,1,22),LocalDate.of(2023,2,22),LocalDate.of(2023,3,22),LocalDate.of(2023,4,22)),102030,152535),
                UserDataModel(1002,"Lucas","Hard","21.06.1986","Biebr", "password", listOf("SIPO","BüP","SAS","BE","HIB"),listOf(
                    LocalDate.of(2023,5,22),LocalDate.of(2023,5,22),LocalDate.of(2023,1,22),LocalDate.of(2023,1,11),LocalDate.of(2023,1,11),LocalDate.of(2023,1,11)),112030,162535),

                // UserDataModel(1003,"Anita","Koch","15.04.1980","Ludwigshafen", "password", listOf("SAKRA","BüP","SAS","BE","HIB"),listOf(),122030,172535),
                // UserDataModel(1004,"Michael","Schmidt","27.11.1990","Heidelberg", "password", listOf("SIPO","BüP","SAS","BE","HIB"),listOf(),132030,182535),
                // UserDataModel(1005,"Sabine","Bauer","31.01.1979","Mainz", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),listOf(),142030,192535),
                // UserDataModel(1006,"Max","Müller","03.07.1982","Mannheim", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),listOf(),152030,202535),
                // UserDataModel(1007,"Linda","Weber","06.10.1988","Wiesbaden", "password", listOf("SIPO","BüP","SAS","BE","HIB"),listOf(),162030,212535),
                // UserDataModel(1008,"Felix","Schneider","23.03.1985","Karlsruhe", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),listOf(),172030,222535),
                // UserDataModel(1009,"Monika","Meyer","18.02.1991","Kaiserslautern", "password", listOf("SIPO","BüP","SAS","BE","HIB"),listOf(),182030,232535),
                // UserDataModel(1010,"Leon","Wolf","05.05.1993","Saarbrücken", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),listOf(),192030,242535),
            )
        }
    }


    private fun loadMaterial() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _material.value = listOf(
                StorageMaterialModel(1, "GSMR-1", 1),
                StorageMaterialModel(2, "BahneErderGarnitur-1", 1),
                StorageMaterialModel(3, "GSMR-2", 1),
                StorageMaterialModel(4, "BüpGarnitur-1", 1),
                StorageMaterialModel(5, "LA-1", 1),
                StorageMaterialModel(6, "LA-33", 1),
                StorageMaterialModel(7, "E-Sail-1", 1),
                StorageMaterialModel(8, "E-Sail-2", 1),
                StorageMaterialModel(9, "E-Sail-4", 1),
                StorageMaterialModel(10, "ZPW-10-22", 1),
                StorageMaterialModel(11, "HS-1", 1),
                StorageMaterialModel(12, "HS-2", 1),
                StorageMaterialModel(13, "HS-3", 1),
                StorageMaterialModel(14, "HS-4", 1),
                StorageMaterialModel(15, "ZPW-10-23", 1),
                StorageMaterialModel(16, "E-Sail-5", 1),
                StorageMaterialModel(17, "E-Sail-6", 1),
                StorageMaterialModel(18, "LA-34", 1),
                StorageMaterialModel(19, "LA-35", 1),
                StorageMaterialModel(20, "BüpGarnitur-2", 1),
                StorageMaterialModel(21, "GSMR-3", 1),
                StorageMaterialModel(22, "BahneErderGarnitur-2", 1),
                StorageMaterialModel(23, "HS-5", 1),
                StorageMaterialModel(24, "HS-6", 1),
                StorageMaterialModel(25, "ZPW-10-24", 1),
                StorageMaterialModel(26, "E-Sail-7", 1),
                StorageMaterialModel(27, "E-Sail-8", 1),
                StorageMaterialModel(28, "LA-36", 1),
                StorageMaterialModel(29, "LA-37", 1),
                StorageMaterialModel(30, "BüpGarnitur-3", 1),
                StorageMaterialModel(31, "GSMR-4", 1),
                StorageMaterialModel(32, "BahneErderGarnitur-3", 1),
                StorageMaterialModel(33, "HS-7", 1),
                StorageMaterialModel(34, "HS-8", 1),
                StorageMaterialModel(35, "ZPW-10-25", 1),
                StorageMaterialModel(36, "E-Sail-9", 1),
                StorageMaterialModel(37, "E-Sail-10", 1),
                StorageMaterialModel(38, "LA-38", 1),
                StorageMaterialModel(39, "LA-39", 1),
                StorageMaterialModel(40, "BüpGarnitur-4", 1),
                StorageMaterialModel(41, "GSMR-5", 1),
                StorageMaterialModel(42, "BahneErderGarnitur-4", 1)
            )
        }

        }
        suspend fun insert(userMaterial: UserMaterialModel) {
            try {
                userMaterialDatabase.userMaterialDao.insert(userMaterial)
            } catch (e: Exception) {
                Log.e("TAG", "Failed to insert into database: $e")
            }
    }
}