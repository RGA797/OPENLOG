package com.example.openlog

import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.openlog.databinding.FragmentForsideBinding
import com.example.openlog.databinding.FragmentManuelIndtastningBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manuel_indtastning, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logButton.setOnClickListener{onLog()}
    }

    fun onLog(){
        val input = binding.textInput.text.toString()
        val firebaseUser: FirebaseUser = dataViewModel.getCurrentFirebaseUser()!!
        val database = FirebaseDatabase.getInstance("https://openlog-a2b24-default-rtdb.europe-west1.firebasedatabase.app/")
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val time: String
        val myRef = database.getReference("users").child(firebaseUser.uid).push()
        if (input.contains("insulin")) {
            time = sdf.format(Date())
            val inputValue = input.filter { it.isDigit() }
            if (inputValue != "") {
                myRef.child(time).child("insulin").setValue(inputValue)
            }

            binding.textInput.setText("")
            return

        }
        else if (input.contains("kulhydrat")) {
            time = sdf.format(Date())
            val inputValue = input.filter { it.isDigit() }
            if (inputValue != "") {
                myRef.child(time).child("kulhydrat").setValue(inputValue)
            }
            binding.textInput.setText("")
            return
        }
        else if (input.contains("blodsukker")) {
            time = sdf.format(Date())
            val inputValue = input.filter { it.isDigit() }
            if (inputValue != "") {
                myRef.child(time).child("blodsukker").setValue(inputValue)
            }
            binding.textInput.setText("")
            return
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