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
        val database = FirebaseDatabase.getInstance("https://openlog-a2b24-default-rtdb.europe-west1.firebasedatabase.app/")
        val input = binding.textInput.text.toString()
        if (input != null) {
            val myRef = database.getReference("users").child(firebaseUser.uid).push()
            if (input.contains("insulin")) {
                inputData("insulin",input, myRef)
                Toast.makeText(context, "input saved", Toast.LENGTH_SHORT).show()
                return
            }
            else if (input.contains("kulhydrat")) {
                inputData("kulhydrat",input, myRef)
                Toast.makeText(context, "input saved", Toast.LENGTH_SHORT).show()
                return
            }
            else if (input.contains("blodsukker")) {
                inputData("blodsukker",input, myRef)
                Toast.makeText(context, "input saved", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }

    private fun inputData(type: String, input: String, Ref: DatabaseReference){
        val time = Calendar.getInstance().getTime().toString()
        val inputValue = input.filter { it.isDigit() }
        if (inputValue != "") {
            Ref.child(time).child(type).setValue(inputValue)
        }
        dataViewModel.changeNew(false)
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