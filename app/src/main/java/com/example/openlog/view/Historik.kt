//package com.example.openlog.view
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import androidx.navigation.Navigation
//import com.example.openlog.R
//
//class historik : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view =  inflater.inflate(R.layout.fragment_historik, container, false)
//
//        val kulhydratButton = view.findViewById<ImageButton>(R.id.kulhydratHistorikButton)
//        val insulinButton =  view.findViewById<ImageButton>(R.id.insulinHistorikButton)
//        val blodsukkerButton = view.findViewById<ImageButton>(R.id.blodsukkerHistorikButton)
//
//
//        kulhydratButton.setOnClickListener{
//            Navigation.findNavController(view).navigate(R.id.navigateFromHistorikToKulhydrater)
//        }
//
//        insulinButton.setOnClickListener{
//            Navigation.findNavController(view).navigate(R.id.navigateFromHistorikToInsulin)
//        }
//
//        blodsukkerButton.setOnClickListener{
//            Navigation.findNavController(view).navigate(R.id.navigateFromHistorikToBlodsukker)
//        }
//        return view
//    }
//
//}