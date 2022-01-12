package com.example.openlog.model

import java.util.*

const val CARB = 0
const val INSULIN = 1
const val BLOODSUGAR = 2

class GraphOptionData {
    private var categoryArray = arrayOf(false, false, false)
    private var selectedDates = arrayOfNulls<Date>(2)
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

        //if only one day selected
//        if (secondDate == null && firstDate != null) {
//            secondDate = Date(firstDate.time+dayInMiliseconds)
//        }

        if (firstDate != null && secondDate != null) {
            if (firstDate.after(secondDate)) {
                selectedDates[0] = secondDate
                selectedDates[1] = firstDate
            }
        }
        return selectedDates
    }

    // puts dates into selectedDates that should have no values
    // sets all categories to not chosen
    // (temporary solution)
    // TODO : better solution for resetting dates
    fun resetDateAndCategory() {
        val date = Calendar.getInstance()
        date.set(1, 1, 1, 1, 1, 1)
        selectedDates[0] = date.time
        date.set(1, 1, 1, 2, 1, 1)
        selectedDates[1] = date.time

        categoryArray = arrayOf(false, false, false)
    }
}