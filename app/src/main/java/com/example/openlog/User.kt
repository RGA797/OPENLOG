package com.example.openlog

import com.google.firebase.auth.FirebaseUser

class User {
    private var fireBaseUser: FirebaseUser? = null

    fun setFirebaseUser(user: FirebaseUser){
        fireBaseUser = user
    }

    fun getFirebaseUser(): FirebaseUser?{
        return fireBaseUser
    }
}