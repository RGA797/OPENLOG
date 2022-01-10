package com.example.openlog.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.navigation.Navigation
import com.example.openlog.R
import com.example.openlog.databinding.FragmentForsideBinding
import com.example.openlog.databinding.FragmentOpretProfilBinding
import com.example.openlog.viewModel.DataViewModel
import com.google.firebase.database.FirebaseDatabase

class Opretprofil : Fragment() {

    private val dataViewModel: DataViewModel by activityViewModels()
    private lateinit var binding: FragmentOpretProfilBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_opret_profil, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        //upon pressing the register button, a user is created. if succesful, optional values for age and gender is saved
        binding.opretProfilButton.setOnClickListener {onOpretButton()}
        //val languages = resources.getStringArray(R.array.Køn)
        //val arrayAdapter = ArrayAdapter (requireContext(),R.layout.dropdown_item())
    }
    fun onOpretButton(){
        if (binding.opretEmailText.text?.isEmpty() == true || binding.opretAdgangskodeText.text?.isEmpty() == true ) {
            Toast.makeText(context, "email eller adgangskode er tom", Toast.LENGTH_SHORT).show()
        } else {
            val email: String = binding.opretEmailText.text.toString()
            val adgangskode: String = binding.opretAdgangskodeText.text.toString()
            val koen: String = binding.opretKNText.text.toString()
            val alder: String = binding.opretAlderText.text.toString()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, adgangskode)
                .addOnCompleteListener(requireActivity(), OnCompleteListener<AuthResult>() { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        dataViewModel.changeUser(firebaseUser, email)
                        Toast.makeText(context, "Du er nu registreret", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(binding.root).navigate(R.id.navigateFromOpretToForside)
                        if (alder.isNotBlank() && alder.isDigitsOnly() && koen.isNotEmpty() && alder.toInt() in 1..99){
                            dataViewModel.saveUserData(firebaseUser, koen, alder)
                        }
                    } else {
                        Toast.makeText(context, "E-mail eller adgangskode er ikke valit", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}