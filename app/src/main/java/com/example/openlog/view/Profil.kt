package com.example.openlog.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.openlog.R
import com.example.openlog.databinding.FragmentProfilBinding
import com.example.openlog.viewModel.DataViewModel


class Profil : Fragment() {
    private val dataViewModel: DataViewModel by activityViewModels()

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: FragmentProfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profil, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dataViewModel = dataViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        if (dataViewModel.userDataList.size != 0 ) {
            binding.alderTekst.text = dataViewModel.userDataList.get(0).getInputOne()
            binding.koenTekst.text = dataViewModel.userDataList.get(0).getInputTwo()
        }
    }



}