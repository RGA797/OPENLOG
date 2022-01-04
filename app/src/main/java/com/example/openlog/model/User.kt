package com.example.openlog.model

import com.google.firebase.auth.FirebaseUser

class User {
    private var fireBaseUser: FirebaseUser? = null
    private var email = ""

    fun setFirebaseUser(firebaseUser: FirebaseUser){
        this.fireBaseUser = firebaseUser
    }

    fun setEmail (email: String){
        this.email = email
    }

    fun getFirebaseUser(): FirebaseUser?{
        return fireBaseUser
    }
    fun getEmail(): String {
        return email
    }
}