package com.example.openlog.model

import java.util.*

class DateConverter {

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