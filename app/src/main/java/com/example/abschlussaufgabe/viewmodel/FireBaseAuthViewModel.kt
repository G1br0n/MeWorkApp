package com.example.abschlussaufgabe.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FireBaseAuthViewModel: ViewModel() {


        private val firebaseAuth = FirebaseAuth.getInstance()

        private val _currentUserBase = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
        val currentUserBase : LiveData<FirebaseUser?>
            get() = _currentUserBase



        fun register(
            email: String,
            password: String,
            firstName: String,
            lastName: String,
            baNumber:Int = 0,
            userQualification: Map<String,String>,
        ) {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {

                    FireStoreViewModel().addNewUser(
                        authResult.result.user!!.uid,
                        email,
                        password,
                        firstName,
                        lastName,
                        baNumber,
                        userQualification
                    )


                } else {
                    Log.e("ERROR", "${authResult.exception}")
                }
            }

        }


        fun login(email: String, password: String){

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    _currentUserBase.value = firebaseAuth.currentUser

                } else {

                    Log.e("ERROR", "${authResult.exception}")
                }

            }

        }


        fun logout() {
            firebaseAuth.signOut()
            _currentUserBase.value = firebaseAuth.currentUser
        }


}