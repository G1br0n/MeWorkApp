package com.example.abschlussaufgabe.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.google.firebase.ktx.Firebase

import com.google.firebase.firestore.ktx.firestore

const val TAG = "fireStore"

class FireStoreViewModel : ViewModel() {

    var userData: UserTestDataModel = UserTestDataModel("","","","","",0, mapOf())





    private val _currentUserStore = MutableLiveData<UserTestDataModel>()
    val currentUserStore: LiveData<UserTestDataModel>
        get() = _currentUserStore

    val db = Firebase.firestore

    fun addNewUser(
        userUid: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        baNumber:Int = 0,
        userQualification: Map<String,String>,

    ) {

        val userData = mapOf(
            "userUid" to userUid,
            "email" to email,
           "password" to password,
            "firstName" to firstName,
            "lastName" to lastName,
            "baNumber" to baNumber,

            "userQualification" to userQualification

        )

        db.collection("users").document(userUid).set(
            userData
        )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

     fun getUserData(userUid: String) {


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
                           document.data?.get("userQualification") as Map<String, String>

                       )
                       Log.d(TAG, _currentUserStore.value.toString())
                   } else {
                       Log.d(TAG, "No such document")
                   }

                   println(_currentUserStore)
               }
               .addOnFailureListener { exception ->
                   Log.d(TAG, "get failed with ", exception)
               }
       }catch (ex: Exception){
           Log.e(TAG, ex.message!!)
       }

    }

}
