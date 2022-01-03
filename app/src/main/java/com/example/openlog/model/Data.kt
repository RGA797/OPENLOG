package com.example.openlog.model

import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class Data {
    var input: String? = null

    fun changeInput(input: String){
        this.input = input
    }

    fun getDataInput(): String? {
        return input
    }

    fun inputData(type: String, input: String, Ref: DatabaseReference){
        val time = Calendar.getInstance().getTime().toString()
        val inputValue = input.filter { it.isDigit() }
        if (inputValue != "") {
            Ref.child(time).child(type).setValue(inputValue)
        }
    }

    fun storeInput(firebaseUser: FirebaseUser): Boolean {
        val database = FirebaseDatabase.getInstance("https://openlog-a2b24-default-rtdb.europe-west1.firebasedatabase.app/")
        if (input != null) {
            val myRef = database.getReference("users").child(firebaseUser.uid).push()
            if (input!!.contains("insulin")) {
                inputData("insulin", input!!, myRef)
                return true
            }
            else if (input!!.contains("kulhydrat")) {
                inputData("kulhydrat", input!!, myRef)
                return true
            }
            else if (input!!.contains("blodsukker")) {
                inputData("blodsukker", input!!, myRef)
                return true
            }
        }
        return false
    }
}