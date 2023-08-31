package com.example.abschlussaufgabe.data


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussaufgabe.data.local.StorageMaterialDatabase
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import java.sql.Timestamp

class AppRepository(
    private val storageMaterialDatabase: StorageMaterialDatabase
) {


    var user = UserTestDataModel("","", "", "", "",234234, mutableMapOf(),   false, false, mutableMapOf())


    //UserList mit login daten passwort und anderen werten, wird aus feste liste in funktion loadUser geladen
    private val _userDataList = MutableLiveData<List<UserDataModel>>()
    val userDataList: LiveData<List<UserDataModel>>
        get() = _userDataList


    //TODO: Diese wariable kann mann zu standarte Lise umbauen
    //Variable für grund Datenbank  wird aus loadStorageMaterial() gefült, wenn das app erste mal instalirt wird damit der daten bank gefült wird
    private val _storageMaterialGroundList = MutableLiveData<List<StorageMaterialModel>>()
    val storageMaterialGroundList: LiveData<List<StorageMaterialModel>>
        get() = _storageMaterialGroundList


    //Diese variable wird aus dem daten bank geladen auch nach dem eine objeckt geändert wurde,
    private val _storageMaterialDataList = MutableLiveData<List<StorageMaterialModel>>()
    val storageMaterialDataList: LiveData<List<StorageMaterialModel>>
        get() = _storageMaterialDataList


    init {
        loadUser()
        loadStorageMaterial()
    }


    //feste user liste
    fun loadUser() {
        _userDataList.value = listOf(
            //User 1
            UserDataModel(
                "",
                1001,
                "Eugen",
                "Lange",
                "12.08.1989",
                "Gibron",
                "password",
                arrayOf("Sakra","SIPO", "BüP", "SAS", "BE", "HIB"),
                arrayOf(
                    Timestamp(2023, 5, 22,12,0,0,1),
                    Timestamp(2023, 5, 22,12,0,0,1),
                    Timestamp(2023, 5, 22,12,0,0,1),
                    Timestamp(2023, 5, 22,12,0,0,1),
                    Timestamp(2023, 5, 22,12,0,0,1),
                    Timestamp(2023, 5, 22,12,0,0,1),
                ),
                102030,
                152535,

                false
            ),

            //User 2
            UserDataModel(
                "",
                1002,
                "Lucas",
                "Hard",
                "21.06.1986",
                "Biebr",
                "password",
                arrayOf("SIPO", "BüP", "SAS", "BE", "HIB"),
                arrayOf(
                    Timestamp(2023, 5, 22,12,0,0,1),
                    Timestamp(2023, 5, 22,12,0,0,1),
                    Timestamp(2023, 5, 22,12,0,0,1),
                    Timestamp(2023, 5, 22,12,0,0,1),
                    Timestamp(2023, 5, 22,12,0,0,1),
                ),
                112030,
                162535,
                false
            )
        )
    }

    //feste datensatz mit material liste für lager und psa material, id bis 100.000 ist lager bestand ab 100.000 psa ausrüstung
    //Beachte es gibt material id die zeigt material nummer und lockationId die zeigen sol wo sich der material gerade befindet
    fun loadStorageMaterial() {
        _storageMaterialGroundList.value = listOf(
            StorageMaterialModel(1, "GSMR-1", "1"),
            StorageMaterialModel(2, "BahnErdeGarnitur-1", "1"),
            StorageMaterialModel(3, "GSMR-2", "1001"),
            StorageMaterialModel(4, "BüpGarnitur-1", "1001"),
            StorageMaterialModel(5, "LA-1", "1002"),
            StorageMaterialModel(6, "LA-33", "1002"),
            StorageMaterialModel(7, "E-Sail-1", "1"),
            StorageMaterialModel(8, "E-Sail-2", "1"),
            StorageMaterialModel(9, "E-Sail-4", "1"),
            StorageMaterialModel(10, "ZPW-10-22", "1"),
            StorageMaterialModel(11, "HS-1", "1001"),
            StorageMaterialModel(12, "HS-2", "1"),
            StorageMaterialModel(13, "HS-3", "1"),
            StorageMaterialModel(14, "HS-4", "1"),
            StorageMaterialModel(15, "ZPW-10-23", "1"),
            StorageMaterialModel(16, "E-Sail-5", "1"),
            StorageMaterialModel(17, "E-Sail-6", "1"),
            StorageMaterialModel(18, "LA-34", "1"),
            StorageMaterialModel(19, "LA-35", "1"),
            StorageMaterialModel(20, "BüpGarnitur-2", "1"),
            StorageMaterialModel(21, "GSMR-3", "1"),
            StorageMaterialModel(22, "BahnErdeGarnitur-2", "1"),
            StorageMaterialModel(23, "HS-5", "1"),
            StorageMaterialModel(24, "HS-6", "1"),
            StorageMaterialModel(25, "ZPW-10-24", "1"),
            StorageMaterialModel(26, "E-Sail-7", "1"),
            StorageMaterialModel(27, "E-Sail-8", "1"),
            StorageMaterialModel(28, "LA-36", "1"),
            StorageMaterialModel(29, "LA-37", "1"),
            StorageMaterialModel(30, "BüpGarnitur-3", "1"),
            StorageMaterialModel(31, "GSMR-4", "1"),
            StorageMaterialModel(32, "BahnErdeGarnitur-3", "1"),
            StorageMaterialModel(33, "HS-7", "1"),
            StorageMaterialModel(34, "HS-8", "1"),
            StorageMaterialModel(35, "ZPW-10-25", "1"),
            StorageMaterialModel(36, "E-Sail-9", "1"),
            StorageMaterialModel(37, "E-Sail-10", "1"),
            StorageMaterialModel(38, "LA-38", "1"),
            StorageMaterialModel(39, "LA-39", "1"),
            StorageMaterialModel(40, "BüpGarnitur-4", "1"),
            StorageMaterialModel(41, "GSMR-5", "1"),
            StorageMaterialModel(42, "BahnErdeGarnitur-4", "1"),
            StorageMaterialModel(100001, "WesteRot", "1001"),
            StorageMaterialModel(100002, "WesteGrün", "1001"),
            StorageMaterialModel(100003, "HoseRot", "1001"),
            StorageMaterialModel(100004, "HoseGrün,", "1"),
            StorageMaterialModel(100005, "S3-S", "1"),
            StorageMaterialModel(100006, "HelmBlau", "1"),
            StorageMaterialModel(100007, "HelmRot", "1"),
            StorageMaterialModel(100008, "Schutzbrille", "1"),
            StorageMaterialModel(100009, "ArbeitsschuheS1", "1"),
            StorageMaterialModel(100010, "ArbeitsschuheS2", "1"),
            StorageMaterialModel(100011, "HandschuheLeder", "1"),
            StorageMaterialModel(100012, "HandschuheNitril", "1"),
            StorageMaterialModel(100013, "Atemschutzmaske", "1"),
            StorageMaterialModel(100014, "Ohrenschützer", "1"),
            StorageMaterialModel(100015, "Kniepolster", "1"),
            StorageMaterialModel(100016, "Sicherheitsgurt", "1"),
            StorageMaterialModel(100017, "Reflektorweste", "1"),
            StorageMaterialModel(100018, "Feuerschutzanzug", "1"),
            StorageMaterialModel(100019, "Chemikalienschürze", "1"),
            StorageMaterialModel(100020, "Isolierhandschuhe", "1"),
            StorageMaterialModel(100021, "SchutzhelmMitVisier", "1"),
            StorageMaterialModel(100022, "Vollschutzanzug", "1"),
            StorageMaterialModel(100023, "Sicherheitsstiefel", "1"),
            StorageMaterialModel(100024, "Gehörschutzstöpsel", "1"),
            StorageMaterialModel(100025, "AtemschutzHalbmaske", "1")
        )

    }


    //Die funktion initialiesirt den daten banck bei onconflicktStrategy.Abort damit der datenbank sich nicht auf grund wert setzt sonder verändert bleibt
    suspend fun insertAll(storageMaterialList: List<StorageMaterialModel>) {
        try {
            storageMaterialDatabase.storageMaterialDao.insertAll(_storageMaterialGroundList.value!!)


        } catch (e: Exception) {
            Log.e("TAG", "Failed to insert into database: $e")
        }
    }

    //Diese funktion befült  die LiveData Aus dem datenbank
    suspend fun getAllStorageMaterialFromDataBank(): List<StorageMaterialModel> {
        try {
            return storageMaterialDatabase.storageMaterialDao.getAll()
        } catch (e: java.lang.Exception) {
            Log.e("AppRepository", "${e.message}")
        }
        return emptyList()
    }

    //Mit diese funktion verändere ich die lokationId von einem betimtimten material
    suspend fun updateStorageMaterial(materialId: Int, userId: String) {

        storageMaterialDatabase.storageMaterialDao.updateStorage(materialId, userId)

        var test = _storageMaterialGroundList.value!!.indexOfFirst { it.materialId == materialId }
        _storageMaterialGroundList.value!![test].locationId = userId
    }




//TODO:--------------------------------- Nicht genutzte funktionen ---------------------------------
    suspend fun insert(storageMaterial: StorageMaterialModel) {
        try {
            storageMaterialDatabase.storageMaterialDao.insert(storageMaterial)
        } catch (e: Exception) {
            Log.e("TAG", "Failed to insert into database: $e")
        }
    }

    suspend fun getCountStorageMaterial(): Int {
        return storageMaterialDatabase.storageMaterialDao.getCount()
    }

    suspend fun getMaterialsByUserIdLiveData(userId: String): List<StorageMaterialModel> {
        return storageMaterialDatabase.storageMaterialDao.getMaterialsByUserId(userId)
    }

    suspend fun getById(materialId: Int): List<StorageMaterialModel> {
        return storageMaterialDatabase.storageMaterialDao.getById(materialId)
    }
//TODO:---------------------------------------------------------------------------------------------


}