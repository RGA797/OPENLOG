package com.example.openlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
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
        val widgetButton = view.findViewById<ImageButton>(R.id.buttonWidget)

        widgetButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateFromForsideToHistorik)
        }

        return view
    }

}