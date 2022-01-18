package com.example.openlog.model

const val YELLOW = 4293507916.toInt()
const val BLUE = 4280062934.toInt()
const val RED = 4293483617.toInt()

class LineData(type: Int) {
    var color: Int = 0

    init {
        when(type) {
            0 -> {
                color = YELLOW
            }
            1 -> {
                color = BLUE
            }
            2 -> {
                color = RED
            }
        }
    }
}