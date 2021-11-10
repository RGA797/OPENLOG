package com.example.openlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

class opret_profil : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_opret_profil, container, false)
        // Inflate the layout for this fragment
        val opretProfilButton = view.findViewById<Button>(R.id.opretProfilButton)
        opretProfilButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateFromOpretToForside)
        }
        return view
    }
}