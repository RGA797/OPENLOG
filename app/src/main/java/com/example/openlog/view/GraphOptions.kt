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
import java.util.*

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
        binding.insulinHighlight.setOnClickListener{onCategory(INSULIN)}

        binding.bloodSugarButton.setOnClickListener{onCategory(BLOODSUGAR)}
        binding.bloodSugarHighlight.setOnClickListener{onCategory(BLOODSUGAR)}

        binding.carbohydrateButton.setOnClickListener{onCategory(CARB)}
        binding.carbohydrateHighlight.setOnClickListener{onCategory(CARB)}

        binding.generateGraph.setOnClickListener{

            val fireBaseUser = dataViewModel.getCurrentFirebaseUser()

            val date = Calendar.getInstance()

            date.set(1,1,1,1,1,1)
            val start = date.time
            date.set(2222,1,1,1,1,1)
            val end = date.time

            dataViewModel.updateInputData(fireBaseUser!!,"insulin", start, end)
            Navigation.findNavController(view).navigate(R.id.navigateFromGraphOptionsToGraph)}
    }

    //flips visibility between highlighted and non highlighted buttons
    fun onCategory(category: Int){
        if (dataViewModel.categorySelected(category)) {
            when (category) {
                CARB -> {
                    binding.carbohydrateButton.visibility = View.VISIBLE
                    binding.carbohydrateHighlight.visibility = View.INVISIBLE
                }
                INSULIN -> {
                    binding.insulinButton.visibility = View.VISIBLE
                    binding.insulinHighlight.visibility = View.INVISIBLE
                }
                BLOODSUGAR -> {
                    binding.bloodSugarButton.visibility = View.VISIBLE
                    binding.bloodSugarHighlight.visibility = View.INVISIBLE
                }
            }
        } else {
            when (category) {
                CARB -> {
                    binding.carbohydrateButton.visibility = View.INVISIBLE
                    binding.carbohydrateHighlight.visibility = View.VISIBLE
                }
                INSULIN -> {
                    binding.insulinButton.visibility = View.INVISIBLE
                    binding.insulinHighlight.visibility = View.VISIBLE
                }
                BLOODSUGAR -> {
                    binding.bloodSugarButton.visibility = View.INVISIBLE
                    binding.bloodSugarHighlight.visibility = View.VISIBLE
                }
            }
        }
        dataViewModel.flipCategory(category)
    }
}