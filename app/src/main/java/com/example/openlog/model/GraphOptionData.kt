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

    fun getSelectedDates(): Array<Date?> {
        return selectedDates
    }
}