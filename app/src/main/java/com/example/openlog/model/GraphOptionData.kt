package com.example.openlog.model

import java.util.*

class GraphOptionData {
    private val categoryArray = arrayOf(false, false, false)
    private val selectedDates = arrayOfNulls<Date>(2)
    private var isStartDate = true

    //flips boolean value for category in bool array indicating if it is to be displayed on graph or not
    fun flipCategory(index: Int) {
        categoryArray[index] = !categoryArray[index]
    }

    //checks if category in given index is selected by user
    fun categorySelected(index : Int): Boolean {
        return categoryArray[index]
    }

    // sets a date to selected flipping between first and second arrayindex
    fun setDateSelected(date: Date) {
        if (isStartDate) {
            selectedDates[0] = date
            isStartDate = false
        } else {
            selectedDates[1] = date
            isStartDate = true
        }
    }

    //returns array with selected dates, earlier date in first index, later date in second
    fun getSelectedDates(): Array<Date?> {
        var firstDate = selectedDates[0]
        var secondDate = selectedDates[1]
        val dayInMiliseconds = 86400000

        if (secondDate == null && firstDate != null) {
            secondDate = Date(firstDate.time+dayInMiliseconds)
        }

        if (firstDate != null && secondDate != null) {
            if (firstDate.after(secondDate)) {
                selectedDates[0] = secondDate
                selectedDates[1] = firstDate
            }
        }
        var tempArray = arrayOf(
            selectedDates[0],
            selectedDates[1]
        )

        selectedDates[0] = null
        selectedDates[1] = null

        return tempArray
    }
}