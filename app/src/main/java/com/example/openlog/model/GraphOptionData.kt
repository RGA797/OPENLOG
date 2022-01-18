package com.example.openlog.model

import java.util.*
const val CARB = 0
const val INSULIN = 1
const val BLOODSUGAR = 2
class GraphOptionData {
    private val categoryArray = arrayOf(false, false, false)
    private var selectedDates = arrayOfNulls<Date>(2)
    private var copySelectedDates = arrayOfNulls<Date>(2)
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
            copySelectedDates[0] = date
            isStartDate = false
        } else {
            selectedDates[1] = date
            copySelectedDates[1] = date
            isStartDate = true
        }
    }

    //returns array with selected dates, earlier date in first index, later date in second
    fun getSelectedDates(): Array<Date?> {
        val firstDate = selectedDates[0]
        val secondDate = selectedDates[1]
        //86400000
        val millisecondsInDay = 86400000

        if (firstDate != null && secondDate != null) {
            if (firstDate.after(secondDate)) {
                selectedDates[0] = secondDate
                selectedDates[1] = firstDate
            }
        } else if (secondDate == null && firstDate != null) {  //if only one day selected
            selectedDates[1] = Date(firstDate.time)
        }
        //add 1 day to enddate to get from beginning of start date to end of end date
        if (firstDate != null && secondDate != null)
        selectedDates[1] = Date(selectedDates[1]?.time!!+millisecondsInDay)

        if (selectedDates[0] != null)
        return selectedDates

        else {
            val dateOne = Date(1)
            val dateTwo = Date(1000)
            return arrayOf(dateOne, dateTwo)
        }
    }

    // can cause problems since array is not reset
    // (must find better solution to handling selected dates)
    fun getCopySelectedDates(): Array<Date?> {
        val firstDate = copySelectedDates[0]
        val secondDate = copySelectedDates[1]

        if (firstDate != null && secondDate != null) {
            if (firstDate.after(secondDate)) {
                copySelectedDates[0] = secondDate
                copySelectedDates[1] = firstDate
            }
        }
        return copySelectedDates
    }

    // resets selected dates and categories
    // TODO : create a better way to reset selected dates (currently causes bug when selecting single day)
    fun resetDatesAndCategories() {
        selectedDates[0] = null
        selectedDates[1] = null
        categoryArray[0] = false
        categoryArray[1] = false
        categoryArray[2] = false
        isStartDate = true
    }
}