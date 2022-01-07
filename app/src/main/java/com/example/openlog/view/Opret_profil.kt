package com.example.openlog.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.navigation.Navigation
import com.example.openlog.R
import com.example.openlog.viewModel.DataViewModel
import com.google.firebase.database.FirebaseDatabase

class Opret_profil : Fragment() {


    private val dataViewModel: DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_opret_profil, container, false)
        // Inflate the layout for this fragment

        val registerButton = view.findViewById<Button>(R.id.opretProfilButton)
        val emailTextView = view.findViewById<EditText>(R.id.opretEmailText)
        val adgangskodeTextView = view.findViewById<EditText>(R.id.opretAdgangskodeText)
        val koenTextView = view.findViewById<EditText>(R.id.opretKønText)
        val alderTextView = view.findViewById<EditText>(R.id.opretAlderText)

        registerButton.setOnClickListener {
            if (emailTextView.text.isEmpty() || adgangskodeTextView.text.isEmpty()) {
                Toast.makeText(context, "email or password empty", Toast.LENGTH_SHORT).show()
            } else {
                val email: String = emailTextView.text.toString()
                val adgangskode: String = adgangskodeTextView.text.toString()
                val koen: String = koenTextView.text.toString()
                val alder: String = alderTextView.text.toString()

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, adgangskode)
                    .addOnCompleteListener(requireActivity(), OnCompleteListener<AuthResult>() { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            dataViewModel.changeUser(firebaseUser, email)
                            val database = FirebaseDatabase.getInstance("https://openlog-a2b24-default-rtdb.europe-west1.firebasedatabase.app/")
                            val myRef = database.getReference("users")
                            myRef.child(firebaseUser.uid).setValue(email)
                            Toast.makeText(context, "You are now registered", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).navigate(R.id.navigateFromOpretToForside)
                            if (alder.isNotBlank() && alder.isDigitsOnly() && koen.isNotEmpty() && alder.toInt() in 1..99){
                                dataViewModel.saveUserData(firebaseUser, koen, alder)
                            }
                        } else {
                            Toast.makeText(context, "E-mail or password not valid", Toast.LENGTH_SHORT).show()
                        }
                    })


            }
        }
        return view
    }
}