package com.example.openlog.model

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.coroutines.awaitAll
import java.util.*
import javax.security.auth.callback.Callback


class Data {
    var input: String? = ""
    val database = FirebaseDatabase.getInstance("https://openlog-a2b24-default-rtdb.europe-west1.firebasedatabase.app/")

    fun changeInput(input: String) {
        this.input = input
    }

    fun getDataInput(): String? {
        return input
    }

    fun inputData(type: String, input: String, Ref: DatabaseReference) {
        val time = Calendar.getInstance().getTime().toString()
        val inputValue = input.filter { it.isDigit() }
        if (inputValue != "") {
            var data: MutableMap<String, String> = HashMap()
            data[type] = inputValue
            data["time"] = time
            Ref.updateChildren(data as Map<String, Any>)
        }
    }

    fun storeInput(firebaseUser: FirebaseUser): Boolean {
        if (input != null) {
            val myRef = database.getReference("users").child(firebaseUser.uid).push()
            if (input!!.contains("insulin")) {
                inputData("insulin", input!!, myRef)
                return true
            } else if (input!!.contains("kulhydrat")) {
                inputData("kulhydrat", input!!, myRef)
                return true
            } else if (input!!.contains("blodsukker")) {
                inputData("blodsukker", input!!, myRef)
                return true
            }
        }
        return false
    }

    fun getUserData(firebaseUser: FirebaseUser, type: String, callback: (result: List<InputDTO>) -> Unit) {
        val myRef = database.getReference("users").child(firebaseUser.uid)
        val dataList = mutableListOf<InputDTO>()
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if (ds.child(type).exists()){
                        val timeData = ds.child("time").getValue(String::class.java)
                        val typeData = ds.child(type).getValue(String::class.java)
                        val inputDTO  = InputDTO(timeData!!,typeData!!)
                        dataList.add(inputDTO)
                    }
                }
                callback (dataList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        myRef.addValueEventListener(valueEventListener)
    }
}
