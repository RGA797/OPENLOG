package com.example.openlog.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.openlog.databinding.FragmentGraphOptionsBinding
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.openlog.R

const val CARB = 0
const val INSULIN = 1
const val BLOODSUGAR = 2

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
        binding.insulinButton.setOnClickListener{onCategory(INSULIN)}
        binding.blodsukkerButton.setOnClickListener{onCategory(BLOODSUGAR)}
        binding.kulhydraterButton.setOnClickListener{onCategory(CARB)}

        binding.generateGraph.setOnClickListener{Navigation.findNavController(view).navigate(R.id.navigateFromGraphOptionsToGraph)}
    }

    fun onCategory(category: Int){
        if (dataViewModel.categorySelected(category)) {
            if (category == CARB) {
                binding.kulhydraterButton.visibility = View.VISIBLE
                binding.kulhydraterhighlight.visibility = View.INVISIBLE
            } else if (category == INSULIN) {
                binding.insulinButton.visibility = View.VISIBLE
                binding.Insulinhighlight.visibility = View.INVISIBLE
            } else {
                binding.blodsukkerButton.visibility = View.VISIBLE
                binding.blodsukkerhighlight.visibility = View.INVISIBLE
            }
        } else {
            if (category == CARB) {
                binding.kulhydraterButton.visibility = View.INVISIBLE
                binding.kulhydraterhighlight.visibility = View.VISIBLE
            } else if (category == INSULIN) {
                binding.insulinButton.visibility = View.INVISIBLE
                binding.Insulinhighlight.visibility = View.VISIBLE
            } else {
                binding.blodsukkerButton.visibility = View.INVISIBLE
                binding.blodsukkerhighlight.visibility = View.VISIBLE
            }
        }
        dataViewModel.flipCategory(category)
    }
}