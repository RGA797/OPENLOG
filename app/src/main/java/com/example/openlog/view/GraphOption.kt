package com.example.openlog.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.openlog.R
import com.example.openlog.databinding.FragmentGraphOptionsBinding
import androidx.fragment.app.activityViewModels


class GraphOption : Fragment() {

    private val dataViewModel: com.example.openlog.viewModel.DataViewModel by activityViewModels()
    private lateinit var binding: FragmentGraphOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGraphOptionsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding..setOnClickListener{onGenerateGraph()}
        binding.insulinButton.setOnClickListener{onInsulinButton()}
        binding.blodsukkerButton.setOnClickListener{onBlodsukkerButton()}
        binding.kulhydraterButton.setOnClickListener{onKulhydraterButton()}
    }

    fun onGenerateGraph(){

    }

    fun onInsulinButton(){
        binding.insulinButton.visibility = View.INVISIBLE
        binding.Insulinhighlight.visibility = View.VISIBLE
    }

    fun onBlodsukkerButton(){
        binding.blodsukkerButton.visibility = View.INVISIBLE
        binding.blodsukkerhighlight.visibility = View.VISIBLE
    }

    fun onKulhydraterButton(){
        binding.kulhydraterButton.visibility = View.INVISIBLE
        binding.kulhydraterhighlight.visibility = View.VISIBLE
    }


}