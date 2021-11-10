package com.example.openlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

class Login : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val opretButton = view.findViewById<Button>(R.id.opretButton)
        loginButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateFromLoginToForside)
        }
        opretButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateFromLoginToOpret)
        }
        return view
    }
}
