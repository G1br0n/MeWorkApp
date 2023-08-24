package com.example.abschlussaufgabe.viewmodel

import android.service.autofill.UserData
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.google.firebase.ktx.Firebase

import com.google.firebase.firestore.ktx.firestore
import java.sql.Timestamp

const val TAG = "FirestoreViewModel"

class FireStoreViewModel : ViewModel() {



    private val _currentUser = MutableLiveData<UserTestDataModel>()
    val currentUser: LiveData<UserTestDataModel>
        get() = _currentUser

    val db = Firebase.firestore

    fun addNewUser(
        userUid: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        baNumber:Int = 0,
        userQualification: List<String>,

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
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    /*fun getUserData(userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    _currentUser.value = UserDataModel(

                        document.data?.get("userUid").toString(),
                        document.data?.get("userId") as Long,
                        document.data?.get("userFirstName").toString(),
                        document.data?.get("userLastName").toString(),
                        document.data?.get("userBirthDate").toString(),
                        document.data?.get("userLogIn").toString(),
                        document.data?.get("userPassword").toString(),
                        document.data?.get("userQualification") as Array<String>,
                        document.data?.get("userQualification") as Array<Timestamp>,
                        document.data?.get("userBaNumber") as Long,
                        document.data?.get("userBupNumber") as Long,
                        document.data?.get("haveTheCar") as Boolean,

                    )
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }*/

}