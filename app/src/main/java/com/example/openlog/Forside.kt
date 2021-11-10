package com.example.openlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

class Forside : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_forside, container, false)
        // Inflate the layout for this fragment
        //val kulhydratButton = view.findViewById<Button>(R.id.kulhydraterButton)
        //val insulinButton =  view.findViewById<Button>(R.id.insulinButton)
        //val blodsukkerButton = view.findViewById<Button>(R.id.blodsukkerButton)

        return view
    }

}