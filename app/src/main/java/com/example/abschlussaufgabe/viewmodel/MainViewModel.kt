package com.example.abschlussaufgabe.viewmodel

import RailStationApi
import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.ApiRepository
import com.example.abschlussaufgabe.data.AppRepository
import com.example.abschlussaufgabe.data.local.StorageMaterialDatabase
import com.example.abschlussaufgabe.data.local.getStorageMaterialDatabase
import com.example.abschlussaufgabe.data.model.WorkRunModel
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.data.model.UserTestDataModel

import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {


    var storageMaterialDatabase: StorageMaterialDatabase = getStorageMaterialDatabase(application)

    private val repository = AppRepository(storageMaterialDatabase)
    private val apiRepository = ApiRepository(RailStationApi)

    //Material liste für erst befülüng des datenbank vom lager bestand
    private val storageMaterialList = repository.storageMaterialGroundList

    //Api Object LiveData für UI observer
    val bfPhotoList = apiRepository.bfPhotoList

    //komplete Userliste mit UserDataModel
    val userDataList = repository.userDataList

    //Einzelne User, wird im LoginFragment deklarirt
    var userData: UserTestDataModel = repository.user


    val storageMaterialDataList: LiveData<List<StorageMaterialModel>>
        get() = _storageMaterialDataList
    private var _storageMaterialDataList = MutableLiveData<List<StorageMaterialModel>>()


    //User material liste aus dem lagerbestand DAO
    val userMaterialList: LiveData<List<StorageMaterialModel>>
        get() = _userMaterialList
    private val _userMaterialList = MutableLiveData<List<StorageMaterialModel>>()

    val userPsaMaterialList: LiveData<List<StorageMaterialModel>>
        get() = _userPsaMaterialList
    private val _userPsaMaterialList = MutableLiveData<List<StorageMaterialModel>>()


    var _gpsLiveData = MutableLiveData<WorkRunModel>()
    val gpsLiveData: LiveData<WorkRunModel>
        get() = _gpsLiveData


    init {
        viewModelScope.launch {
            repository.insertAll(storageMaterialList.value!!)
            _storageMaterialDataList.value = repository.getAllStorageMaterialFromDataBank()
            loadUserMaterialList()
            loadBfPhotoList()
        }

    }

    //Diese fonktion ladet random eine neu obekt aus der API
    fun loadBfPhotoList() {
        viewModelScope.launch {
            apiRepository.getBfPhotoList()
        }
    }

    //Diese funktion checkt den daten bank und sortirt die material wenn er zu eingelogten user id past für den RecyclerView().layoutMangerFlexbox
    fun loadUserMaterialList() {


        viewModelScope.launch {
            val materialList = mutableListOf<StorageMaterialModel>()
            val psaMaterialList = mutableListOf<StorageMaterialModel>()


            //material wird in 2 listen sortirt, materialId und lockationId zu userId abhändig
            _storageMaterialDataList.value?.let { materials ->
                for (material in materials) {
                    Log.e("loadUser","${userData.userUid.toString()} ${material.locationId.toString()}")
                    when {
                        material.locationId.toString() == userData.userUid.toString() && material.materialId > 100000 ->
                            psaMaterialList.add(material)

                        material.locationId == userData.userUid && material.materialId < 100000 ->
                            materialList.add(material)

                    }
                }
            }

            //Verpacke die 2 listen in live data um UI abhändige elemente zu observen
            _userPsaMaterialList.value = psaMaterialList
            _userMaterialList.value = materialList

        }
    }


    //mit der funktion überschreibe ich die daten in storage_material_table RoomData
    fun updateMaterialLocation(id: Int, newLocationId: String) {
        viewModelScope.launch {
            repository.updateStorageMaterial(id, newLocationId)
            _storageMaterialDataList.value = repository.getAllStorageMaterialFromDataBank()
            loadUserMaterialList()
        }
    }


    //Überprüfe mit der funktion ob Id in der liste ist oder nicht, wen nicht schmeise feller raus
    fun checkMaterialId(id: Int) {
        val checkIdList = mutableListOf<StorageMaterialModel>()

        for (materialId in storageMaterialDataList.value!!) {
            if (materialId.materialId == id) {
                checkIdList.add(materialId)
            }
        }

        if (checkIdList.isEmpty()) {
            throw Exception("ID befindet sich nicht in der liste")
        }

    }


    //mediaplayer für QR
    private lateinit var mediaPlayer: MediaPlayer
    fun playQrSound(context: Context){
        // MediaPlayer initialisieren
        //TODO Sound wechseln wav qr_sound_2 funktioniert nich
        mediaPlayer = MediaPlayer.create(context, R.raw.qr_sound)

        mediaPlayer.setVolume(1f,1f)
        mediaPlayer.start()
    }

    fun playClickSound(context: Context){
        // MediaPlayer initialisieren
        mediaPlayer = MediaPlayer.create(context, R.raw.click_sound)

        mediaPlayer.setVolume(1f,1f)
        mediaPlayer.start()
    }


    fun playActionSound(context: Context){
        // MediaPlayer initialisieren
        mediaPlayer = MediaPlayer.create(context, R.raw.action_sound_1)

        mediaPlayer.setVolume(1f,1f)
        mediaPlayer.start()
    }

    fun playLockedSound(context: Context){
        // MediaPlayer initialisieren
        mediaPlayer = MediaPlayer.create(context, R.raw.locked_sound)

        mediaPlayer.setVolume(1f,1f)
        mediaPlayer.start()
    }

    fun playLogInSound(context: Context){
        // MediaPlayer initialisieren
        mediaPlayer = MediaPlayer.create(context, R.raw.log_in_sound)

        mediaPlayer.setVolume(1f,1f)
        mediaPlayer.start()
    }
}