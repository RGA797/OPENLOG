package com.example.openlog.model

import android.graphics.Color

class LineData(type: Int) {
    var color: Int = 0

    init {
        when(type) {
            0 -> {
                color = Color.YELLOW
            }
            1 -> {
                color = Color.BLUE
            }
            2 -> {
                color = Color.RED
            }
        }
    }

}