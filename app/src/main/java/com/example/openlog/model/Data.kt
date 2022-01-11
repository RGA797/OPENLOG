package com.example.openlog.model

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

//this class holds the input of a user before it stores it in a firebase database.
// Fetching data from this database, is also part of its functionality
class Data {
    //the local input and used database. remember, input string is not necessarily saved in the database
    // input should be a string consisting of 2 word, a category and value. ex ("blodsukker 20", "kulhydrat 10", "insulin 5")
    var input: String? = ""
    val database = FirebaseDatabase.getInstance("https://openlog-a2b24-default-rtdb.europe-west1.firebasedatabase.app/")
    val dateConverter = DateConverter()

    //changes instance variable input
    fun changeInput(input: String) {
        this.input = input
    }

    //returns instance variable input
    fun getDataInput(): String? {
        return input
    }

    //stores an input split up in type (category) and value, with a given database reference.
    //the input is saved as a hashmap with 2 key value pairs. one for value-category, and another for time-date.
    fun inputData(type: String, value: String, Ref: DatabaseReference) {
        val time = Calendar.getInstance().getTime().toString()
        val inputValue = value.filter { it.isDigit() }
        if (inputValue != "") {
            var data: MutableMap<String, String> = HashMap()
            data[type] = inputValue
            data["time"] = time
            Ref.updateChildren(data as Map<String, Any>)
        }
    }

    //This function checks for valid inputs, and with a given firebase user, gets the corresponding reference, to store data with inputData.
    //returns true if input successful, false otherwise
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

    //once a user has been created, we want to store its age and gender in the database.
    //this follows the same logic as inputData but with køn-value and age-value pairs
    fun storeUserData(firebaseUser: FirebaseUser, køn: String, alder: String, navn: String){
        val Ref = database.getReference("users").child(firebaseUser.uid).push()
        val data: MutableMap<String, String> = HashMap()
        data["køn"] = køn
        data["alder"] = alder
        data["navn"] = navn
        Ref.updateChildren(data as Map<String, Any>)
    }

    //this function handles the data fetching. for a given date range, firebase user, and type, we give a list of inputDTO.
    //due to firebase being an asynchronous database, we use callback. be aware that values are not immediately given.
    fun updateUserData(firebaseUser: FirebaseUser, type: String, startDate: Date?, endDate: Date?, callback: (result: List<InputDTO>) -> Unit) {
        val myRef = database.getReference("users").child(firebaseUser.uid)
        val dataList = mutableListOf<InputDTO>()
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if (ds.child(type).exists()){
                        if (type == "blodsukker" || type == "insulin" || type == "kulhydrat"){
                            val timeData = ds.child("time").getValue(String::class.java)
                            val typeData = ds.child(type).getValue(String::class.java)
                            if(dateConverter.dateInRange(timeData!!, startDate!!, endDate!!)){
                                val inputDTO  = InputDTO(timeData,typeData!!)
                                dataList.add(inputDTO)
                            }
                        }
                        else if (type == "køn"|| type == "alder"|| type == "navn"  ) {
                            val koenData = ds.child("køn").getValue(String::class.java)
                            val alderData = ds.child("alder").getValue(String::class.java)
                            val navnData = ds.child("navn").getValue(String::class.java)
                            val inputDTO_koenAlder = InputDTO(koenData!!, alderData!!)
                            val inputDTO_navn = InputDTO(navnData!!, " ")
                            dataList.add(inputDTO_koenAlder)
                            dataList.add(inputDTO_navn)
                        }
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