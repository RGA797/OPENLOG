package com.example.openlog.model

import com.google.firebase.auth.FirebaseUser

//this class is meant to store who the current firebase user is, and their email/username/age/gender. values are set whenever someone is logged in.
//functions only consist of set and get.
class User {
    private var fireBaseUser: FirebaseUser? = null
    private var email = ""
    private var userName = ""
    private var age = ""
    private var gender = ""

    fun setFirebaseUser(firebaseUser: FirebaseUser){
        this.fireBaseUser = firebaseUser
    }

    fun setEmail (email: String){
        this.email = email
    }

    fun setUsername(userName: String){
        this.userName = userName
    }

    fun setAge(age: String){
        this.age = age
    }

    fun setGender(gender: String){
        this.gender = gender
    }

    fun getFirebaseUser(): FirebaseUser?{
        return fireBaseUser
    }
    fun getEmail(): String {
        return email
    }
    fun getUsername(): String {
        return userName
    }

    fun getAge(): String{
        return age
    }

    fun getGender(): String{
        return gender
    }

}