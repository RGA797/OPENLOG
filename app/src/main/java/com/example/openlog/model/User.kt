package com.example.openlog.model

import com.google.firebase.auth.FirebaseUser

//this class is meant to store who the current firebase user is and their email. values are set whenever someone is logged in.
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