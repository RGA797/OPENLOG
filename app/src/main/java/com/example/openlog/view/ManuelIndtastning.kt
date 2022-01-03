package com.example.openlog.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.openlog.R
import com.example.openlog.databinding.FragmentManuelIndtastningBinding
import com.example.openlog.viewModel.DataViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class manuelIndtastning : Fragment() {
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
        binding.logButton.setOnClickListener{onLogInput()}
    }

    fun onLogInput(){
        val firebaseUser: FirebaseUser = dataViewModel.getCurrentFirebaseUser()!!
        dataViewModel.changeInput(binding.textInput.text.toString())
        val success = dataViewModel.saveInput(firebaseUser)
        if (success){
            Toast.makeText(context, "Input Gemt", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Invalit input", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater){
        menuInflater.inflate(R.menu.nav_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.historikDropdown -> onHistorikDropdown()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onHistorikDropdown(){
        Navigation.findNavController(binding.root).navigate(R.id.navigateFromManuelIndtastningToHistorik)
    }

}