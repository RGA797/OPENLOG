package com.example.openlog.model

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*
import java.text.SimpleDateFormat


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
            else if (input!!.contains("blodsukker")) {
                inputData("blodsukker", input!!, myRef)
                return true
            }
        }
        return false
    }

    fun storeUserData(firebaseUser: FirebaseUser, køn: String, alder: String ){
        val Ref = database.getReference("users").child(firebaseUser.uid).push()
        val data: MutableMap<String, String> = HashMap()
        data["køn"] = køn
        data["alder"] = alder
        Ref.updateChildren(data as Map<String, Any>)
    }
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
                            if(dateInRange(timeData!!, startDate!!, endDate!!)){
                                val inputDTO  = InputDTO(timeData,typeData!!)
                                dataList.add(inputDTO)
                            }
                        }
                        else if (type == "køn"|| type == "alder" ) {
                            val koenData = ds.child("køn").getValue(String::class.java)
                            val alderData = ds.child("alder").getValue(String::class.java)
                            val inputDTO = InputDTO(koenData!!, alderData!!)
                            dataList.add(inputDTO)
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

    fun dateInRange(dateString: String, startDate: Date, endDate: Date): Boolean{
        val date = convertStringToDate(dateString)
        return (date.after(startDate) && date.before(endDate))
    }

    fun convertStringToDate(time: String): Date {

        val stringList = time.split(" ")
        val day = stringList[2].toInt()
        val month = when(stringList[1]){
            "Jan" -> 0
            "Feb" -> 1
            "Mar" -> 2
            "Apr" -> 3
            "May" -> 4
            "Jun" -> 5
            "Jul" -> 6
            "Aug" -> 7
            "Sep" -> 8
            "Oct" -> 9
            "Now" -> 10
            "Dec" -> 11
            else -> {0}
        }
        val hourOfDay = stringList[3].split(":")[0].toInt()
        val minuteOfDay = stringList[3].split(":")[1].toInt()
        val seconOfDay = stringList[3].split(":")[2].toInt()

        val year = stringList[5].toInt()
        val date = Calendar.getInstance()
        date.set(year, month, day, hourOfDay, minuteOfDay, seconOfDay)

        return date.time
    }
}
