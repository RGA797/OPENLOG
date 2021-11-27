package com.example.openlog

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
        if (fireBaseUser != null) {
            return fireBaseUser
        }
        else {
            return null
        }
    }

    fun getEmail(): String {
        return email
    }
}