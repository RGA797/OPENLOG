package com.example.openlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [historik.newInstance] factory method to
 * create an instance of this fragment.
 */
class historik : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_historik, container, false)

        val kulhydratButton = view.findViewById<Button>(R.id.kulhydratHistorikButton)
        val insulinButton =  view.findViewById<Button>(R.id.insulinHistorikButton)
        val blodsukkerButton = view.findViewById<Button>(R.id.blodsukkerHistorikButton)


        kulhydratButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateFromHistorikToKulhydrater)
        }

        insulinButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateFromHistorikToInsulin)
        }

        blodsukkerButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateFromHistorikToBlodsukker)
        }
        return view
    }

}