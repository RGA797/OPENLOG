package com.example.openlog.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.openlog.R
import com.example.openlog.databinding.FragmentManuelIndtastningBinding
import com.example.openlog.viewModel.DataViewModel
import java.util.*


class ManuelIndtastning : Fragment() {
    private val dataViewModel: DataViewModel by activityViewModels()
    private lateinit var binding: FragmentManuelIndtastningBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_manuel_indtastning, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val list = dataViewModel.userInputList
        binding.logButton.setOnClickListener{onLogInput()}
        binding.insulinButton.setOnClickListener{onInsulinButton()}
        binding.blodsukkerButton.setOnClickListener{onBlodsukkerButton()}
        binding.kulhydraterButton.setOnClickListener{onKulhydraterButton()}
    }

    fun onLogInput(){
        var type = ""
        if (binding.Insulinhighlight.isVisible){
            type = "insulin "
        }

        else if (binding.blodsukkerhighlight.isVisible){
            type = "blodsukker "
        }

        else if (binding.kulhydraterhighlight.isVisible){
            type = "kulhydrat "
        }

        dataViewModel.changeInput(type + binding.textInput.text.toString())

        val success = dataViewModel.saveInput()
        if (success){
            Toast.makeText(context, "Input Gemt", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Invalit input", Toast.LENGTH_SHORT).show()
        }

    }

   fun onInsulinButton(){
       binding.insulinButton.visibility = View.INVISIBLE
       binding.blodsukkerButton.visibility = View.VISIBLE
       binding.kulhydraterButton.visibility = View.VISIBLE
       binding.Insulinhighlight.visibility = View.VISIBLE
       binding.blodsukkerhighlight.visibility = View.INVISIBLE
       binding.kulhydraterhighlight.visibility = View.INVISIBLE
       binding.logButton.visibility = View.VISIBLE
       binding.textInput.visibility = View.VISIBLE
   }


    fun onBlodsukkerButton(){
        binding.blodsukkerButton.visibility = View.INVISIBLE
        binding.insulinButton.visibility = View.VISIBLE
        binding.kulhydraterButton.visibility = View.VISIBLE
        binding.blodsukkerhighlight.visibility = View.VISIBLE
        binding.Insulinhighlight.visibility = View.INVISIBLE
        binding.kulhydraterhighlight.visibility = View.INVISIBLE
        binding.logButton.visibility = View.VISIBLE
        binding.textInput.visibility = View.VISIBLE
    }

   fun onKulhydraterButton(){
       binding.kulhydraterButton.visibility = View.INVISIBLE
       binding.blodsukkerButton.visibility = View.VISIBLE
       binding.insulinButton.visibility = View.VISIBLE
       binding.kulhydraterhighlight.visibility = View.VISIBLE
       binding.blodsukkerhighlight.visibility = View.INVISIBLE
       binding.Insulinhighlight.visibility = View.INVISIBLE
       binding.logButton.visibility = View.VISIBLE
       binding.textInput.visibility = View.VISIBLE

    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater){
        menuInflater.inflate(R.menu.nav_menu_forside,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.historikDropdown -> onHistorikDropdown()
        }
        when (item.itemId){
            R.id.infoDropdown -> onInfoDropdown()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onHistorikDropdown(){
        Navigation.findNavController(binding.root).navigate(R.id.navigateFromManualInputToGraphOptions)
    }

    fun onInfoDropdown(){
        Navigation.findNavController(binding.root).navigate(R.id.navigateFromManualInputToInfo)
    }
}

