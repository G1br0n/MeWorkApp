package com.example.abschlussaufgabe.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussaufgabe.data.model.MaterialModel
import com.example.abschlussaufgabe.data.model.UserDataModel
import java.time.LocalDate
import java.util.Date

class AppRepository {

    //todo: UserList
    private val _user = MutableLiveData<List<UserDataModel>>()
    val user: LiveData<List<UserDataModel>>
        get() = _user

    private val _material = MutableLiveData<List<MaterialModel>>()
    val material: LiveData<List<MaterialModel>>
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


    fun loadMaterial() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _material.value = listOf(
                MaterialModel(1, "GSMR-1", LocalDate.of(1991, 12, 12),1),
                MaterialModel(2, "BahneErderGarnitur-1", LocalDate.of(1991, 12, 12),1),
                MaterialModel(3, "GSMR-2", LocalDate.of(1991, 12, 12),1),
                MaterialModel(4, "BüpGarnitur-1", LocalDate.of(1991, 12, 12),1),
                MaterialModel(5, "LA-1", LocalDate.of(1991, 12, 12),1),
                MaterialModel(6, "LA-33", LocalDate.of(1991, 12, 12),1),
                MaterialModel(7, "E-Sail-1", LocalDate.of(1991, 12, 12),1),
                MaterialModel(8, "E-Sail-2", LocalDate.of(1991, 12, 12),1),
                MaterialModel(9, "E-Sail-4", LocalDate.of(1991, 12, 12),1),
                MaterialModel(10, "ZPW-10-22", LocalDate.of(1991, 12, 12),1),
                MaterialModel(11, "HS-1", LocalDate.of(1991, 12, 12),1),
                MaterialModel(12, "HS-2", LocalDate.of(1991, 12, 12),1),
                MaterialModel(13, "HS-3", LocalDate.of(1991, 12, 12),1),
                MaterialModel(14, "HS-4", LocalDate.of(1991, 12, 12),1),
                MaterialModel(15, "ZPW-10-23", LocalDate.of(1991, 12, 12),1),
                MaterialModel(16, "E-Sail-5", LocalDate.of(1991, 12, 12),1),
                MaterialModel(17, "E-Sail-6", LocalDate.of(1991, 12, 12),1),
                MaterialModel(18, "LA-34", LocalDate.of(1991, 12, 12),1),
                MaterialModel(19, "LA-35", LocalDate.of(1991, 12, 12),1),
                MaterialModel(20, "BüpGarnitur-2", LocalDate.of(1991, 12, 12),1),
                MaterialModel(21, "GSMR-3", LocalDate.of(1991, 12, 12),1),
                MaterialModel(22, "BahneErderGarnitur-2", LocalDate.of(1991, 12, 12),1),
                MaterialModel(23, "HS-5", LocalDate.of(1991, 12, 12),1),
                MaterialModel(24, "HS-6", LocalDate.of(1991, 12, 12),1),
                MaterialModel(25, "ZPW-10-24", LocalDate.of(1991, 12, 12),1),
                MaterialModel(26, "E-Sail-7", LocalDate.of(1991, 12, 12),1),
                MaterialModel(27, "E-Sail-8", LocalDate.of(1991, 12, 12),1),
                MaterialModel(28, "LA-36", LocalDate.of(1991, 12, 12),1),
                MaterialModel(29, "LA-37", LocalDate.of(1991, 12, 12),1),
                MaterialModel(30, "BüpGarnitur-3", LocalDate.of(1991, 12, 12),1),
                MaterialModel(31, "GSMR-4", LocalDate.of(1991, 12, 12),1),
                MaterialModel(32, "BahneErderGarnitur-3", LocalDate.of(1991, 12,12),1),
                MaterialModel(33, "HS-7", LocalDate.of(1991, 12, 12),1),
                MaterialModel(34, "HS-8", LocalDate.of(1991, 12, 12),1),
                MaterialModel(35, "ZPW-10-25", LocalDate.of(1991, 12, 12),1),
                MaterialModel(36, "E-Sail-9", LocalDate.of(1991, 12, 12),1),
                MaterialModel(37, "E-Sail-10", LocalDate.of(1991, 12, 12),1),
                MaterialModel(38, "LA-38", LocalDate.of(1991, 12, 12),1),
                MaterialModel(39, "LA-39", LocalDate.of(1991, 12, 12),1),
                MaterialModel(40, "BüpGarnitur-4", LocalDate.of(1991, 12, 12),1),
                MaterialModel(41, "GSMR-5", LocalDate.of(1991, 12, 12),1),
                MaterialModel(42, "BahneErderGarnitur-4", LocalDate.of(1991, 12, 12),1)
            )
        }
    }
}