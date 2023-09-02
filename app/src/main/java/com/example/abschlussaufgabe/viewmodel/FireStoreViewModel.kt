package com.example.abschlussaufgabe.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val TAG = "fireStoreTest"

class FireStoreViewModel : ViewModel() {

    //benutze diese varieable in RegisterFragmenten
    var userData: UserTestDataModel = UserTestDataModel(
        "",
        "",
        "",
        "",
        "",
        0,
        mapOf<String, String>(),
        false,
        false,
        mutableMapOf<String, String>()
    )


    var _currentTimWorkList = MutableLiveData<MutableList<String>>()
    val currentTimWorkList: LiveData<MutableList<String>>
        get() = _currentTimWorkList

    //Beobachte die liveData in login fragment. Zum einlogen
    var _currentUserStore = MutableLiveData<UserTestDataModel>()
    val currentUserStore: LiveData<UserTestDataModel>
        get() = _currentUserStore

    private val db = Firebase.firestore


    fun addNewUserWorkTimeListStore() {


        val userWorkTimeLis: Map<String, Any> = mapOf(
            "workTimeLog" to listOf<String>()
        )

        db.collection("userWorkTimeLogList").document(currentUserStore.value!!.userUid).set(
            userWorkTimeLis
        )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                throw Exception("Error writing document")
            }
    }


    fun addNewUserDataStore(
        userUid: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        baNumber: Int = 0,
        userQualification: Map<String, String>,

        ) {

        val userData = mapOf(
            "userUid" to userUid,
            "email" to email,
            "password" to password,
            "firstName" to firstName,
            "lastName" to lastName,
            "baNumber" to baNumber,

            "userQualification" to userQualification,

            "carStatus" to false,

            "timerStatus" to false,
            "timerMap" to mapOf(
                "startYear" to "0",
                "startMonth" to "0",
                "startDay" to "0",
                "startHour" to "0",
                "startMin" to "0",
                "startSek" to "0",

                "latitude" to "0",
                "longitude" to "0",
                "sap" to "0",
                "position" to "null"

            )
        )

        db.collection("users").document(userUid).set(
            userData
        )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                throw Exception("Error writing document")
            }
    }


    fun saveUserDataStore(userData: UserTestDataModel) {

        val userDataStore = mapOf(
            "userUid" to userData.userUid,
            "email" to userData.email,
            "password" to userData.password,
            "firstName" to userData.firstName,
            "lastName" to userData.lastName,
            "baNumber" to userData.baNumber,

            "userQualification" to userData.userQualification,

            "carStatus" to userData.carStatus,

            "timerStatus" to userData.timerStatus,
            "timerMap" to userData.timerMap

        )

        db.collection("users").document(userData.userUid).set(
            userDataStore
        )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                throw Exception("Error writing document")
            }
    }


    fun saveUserWorkTimeLogStore(workTimeLogList: List<String>) {


        db.collection("workTimeLog").document(userData.userUid).set(
            "userWorkTimeLogList" to workTimeLogList
        )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                throw Exception("Error writing document")
            }
    }


    fun getUserDataStore(userUid: String) {


        try {
            val docRef = db.collection("users").document(userUid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        _currentUserStore.value = UserTestDataModel(

                            document.data?.get("userUid").toString(),
                            document.data?.get("email").toString(),
                            document.data?.get("password").toString(),
                            document.data?.get("firstName").toString(),
                            document.data?.get("lastName").toString(),
                            (document.data?.get("baNumber") as Long?)?.toInt()!!,
                            document.data?.get("userQualification") as Map<String, String>,
                            document.data?.get("carStatus").toString().toBoolean(),
                            document.data?.get("timerStatus").toString().toBoolean(),
                            document.data?.get("timerMap") as MutableMap<String, String>,

                            )
                        Log.d(TAG, _currentUserStore.value.toString())
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        } catch (ex: Exception) {
            Log.e(TAG, ex.message!!)
        }

    }

    fun getWorkTimeListStore(userUid: String) {
        try {
            val docRef = db.collection("workTimeLog").document(userUid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        val workTimeList = document.data!!["second"] as? MutableList<String> ?: emptyList()
                        _currentTimWorkList.value = workTimeList.toMutableList()


                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        } catch (ex: Exception) {
            Log.e(TAG, ex.message!!)
        }
    }
}
