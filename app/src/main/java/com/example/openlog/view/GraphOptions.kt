package com.example.openlog.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import com.example.openlog.databinding.FragmentGraphOptionsBinding
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.openlog.R
import com.example.openlog.model.BLOODSUGAR
import com.example.openlog.model.CARB
import com.example.openlog.model.INSULIN
import java.util.*

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

        binding.calendarView.setOnDateChangeListener { p0, p1, p2, p3 ->
            val date = Calendar.getInstance()

            date.set(p1,p2,p3,0,0,0)
            dataViewModel.setDateSelected(date.time)
        }
        binding.insulinButton.setOnClickListener{onCategory(INSULIN)}
        binding.insulinHighlight.setOnClickListener{onCategory(INSULIN)}

        binding.bloodSugarButton.setOnClickListener{onCategory(BLOODSUGAR)}
        binding.bloodSugarHighlight.setOnClickListener{onCategory(BLOODSUGAR)}

        binding.carbohydrateButton.setOnClickListener{onCategory(CARB)}
        binding.carbohydrateHighlight.setOnClickListener{onCategory(CARB)}

        binding.generateGraph.setOnClickListener{
            dataViewModel.updateInputData()
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