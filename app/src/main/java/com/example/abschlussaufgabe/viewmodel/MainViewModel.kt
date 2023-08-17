package com.example.abschlussaufgabe.viewmodel

import RailStationApi
import android.Manifest
import android.app.Application
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussaufgabe.data.ApiRepository
import com.example.abschlussaufgabe.data.AppRepository
import com.example.abschlussaufgabe.data.local.StorageMaterialDatabase
import com.example.abschlussaufgabe.data.local.getStorageMaterialDatabase
import com.example.abschlussaufgabe.data.model.GpsModel
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {


    var storageMaterialDatabase: StorageMaterialDatabase = getStorageMaterialDatabase(application)

    private val repository = AppRepository(storageMaterialDatabase)
    private val apiRepository = ApiRepository(RailStationApi)

    //Material liste für erst befülüng des datenbank vom lager bestand
    private val storageMaterialList = repository.storageMaterialGroundList

    //ApiObjectLiveData für UI observer
    val bfPhotoList = apiRepository.bfPhotoList

    //komplete Userliste mit UserDataModel
    val userList = repository.userData

    //Einzelne User, wird im LoginFragment deklarirt
    lateinit var userData: UserDataModel


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


    var _gpsLiveData = MutableLiveData<GpsModel>()
    val gpsLiveData: LiveData<GpsModel>
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
                    when {
                        material.locationId == userData.userId && material.materialId > 100000 ->
                            psaMaterialList.add(material)

                        material.locationId == userData.userId && material.materialId < 100000 ->
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
    fun updateMaterialLocation(id: Int, newLocationId: Int) {
        viewModelScope.launch {
            repository.updateStorageMaterial(id, newLocationId)
            _storageMaterialDataList.value = repository.getAllStorageMaterialFromDataBank()
            loadUserMaterialList()
        }
    }


    fun updateGeoLocation() {
        /*   lateinit var fusedLocationClient: FusedLocationProviderClient
        val REQUEST_LOCATION_PERMISSION_CODE = 0

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener() { location: Location ->
                location.let {

                    val latitude = it.latitude
                    val longitude = it.longitude
                    val accuracy = it.accuracy
                    val speed = it.speed
                    val altitude = it.altitude
                    val bearing = it.bearing

                }
                //                        todo neue paramenter
                //binding.tvFive.text = fusedLocationClient.lastLocation.result.elapsedRealtimeAgeMillis.
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_CODE
            )*/
        //}
    }
}