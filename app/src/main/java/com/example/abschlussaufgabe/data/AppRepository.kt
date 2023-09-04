package com.example.abschlussaufgabe.data


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussaufgabe.data.local.StorageMaterialDatabase
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.data.model.UserTestDataModel

/**
 * ## Information
 * ### `AppRepository` - Repository-Klasse, die als Zwischenschicht zwischen der Datenbank und der UI dient.
 * Hier werden Datenbank-Operationen ausgeführt und Daten für die UI bereitgestellt.
 * ###
 * ## Funktionen
 * - [loadStorageMaterial]: Lädt fest definierte Lagermaterialdaten.
 * - [insertAll]: Fügt eine Liste von Lagermaterialien in die Datenbank ein.
 * - [getAllStorageMaterialFromDataBank]: Holt alle Lagermaterialdaten aus der Datenbank.
 * - [updateStorageMaterial]: Aktualisiert den Standort eines bestimmten Materials.
 * - (Nicht genutzt) [insert]: Fügt ein einzelnes Lagermaterial in die Datenbank ein.
 * - (Nicht genutzt) [getCountStorageMaterial]: Gibt die Anzahl der Lagermaterialien in der Datenbank zurück.
 * - (Nicht genutzt) [getMaterialsByUserIdLiveData] : Holt Lagermaterialdaten nach User-ID.
 * - (Nicht genutzt) [getById]: Holt Lagermaterialdaten nach Material-ID.
 */
class AppRepository(
    private val storageMaterialDatabase: StorageMaterialDatabase
) {

    // Beispiel für ein Benutzermodell.
    var user = UserTestDataModel("","", "", "", "",234234, mutableMapOf(),   false, false, mutableMapOf())

    // LiveData-Liste, die grundlegende Lagermaterialdaten enthält.
    private val _storageMaterialGroundList = MutableLiveData<List<StorageMaterialModel>>()
    val storageMaterialGroundList: LiveData<List<StorageMaterialModel>>
        get() = _storageMaterialGroundList

    // LiveData-Liste, die aktuelle Lagermaterialdaten aus der Datenbank enthält.
    private val _storageMaterialDataList = MutableLiveData<List<StorageMaterialModel>>()
    val storageMaterialDataList: LiveData<List<StorageMaterialModel>>
        get() = _storageMaterialDataList

    // Initialisierung, die Benutzer und Lagermaterialdaten lädt.
    init {
        loadStorageMaterial()
    }

    /**
     * ## Information
     * Funktion, die fest definierte Lagermaterialdaten lädt.
     */ fun loadStorageMaterial() {
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

    /**
     * ## Information
     * Fügt alle Lagermaterialien in die Datenbank ein.
     */
    suspend fun insertAll(storageMaterialList: List<StorageMaterialModel>) {
        try {
            storageMaterialDatabase.storageMaterialDao.insertAll(_storageMaterialGroundList.value!!)
        } catch (e: Exception) {
            Log.e("TAG", "Failed to insert into database: $e")
        }
    }

    /**
     * ## Information
     * Holt alle Lagermaterialdaten aus der Datenbank.
     * @return Liste von Lagermaterialdaten.
     */
    suspend fun getAllStorageMaterialFromDataBank(): List<StorageMaterialModel> {
        try {
            return storageMaterialDatabase.storageMaterialDao.getAll()
        } catch (e: java.lang.Exception) {
            Log.e("AppRepository", "${e.message}")
        }
        return emptyList()
    }

    /**
     * ## Information
     * Aktualisiert den Standort eines bestimmten Materials.
     */
    suspend fun updateStorageMaterial(materialId: Int, userId: String) {
        storageMaterialDatabase.storageMaterialDao.updateStorage(materialId, userId)
        val index = _storageMaterialGroundList.value!!.indexOfFirst { it.materialId == materialId }
        _storageMaterialGroundList.value!![index].locationId = userId
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