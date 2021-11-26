package com.example.openlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.navigation.Navigation

class opret_profil : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_opret_profil, container, false)
        // Inflate the layout for this fragment

        val registerButton = view.findViewById<Button>(R.id.opretProfilButton)
        val emailTextView = view.findViewById<EditText>(R.id.opretEmailText)
        val adgangskodeTextView = view.findViewById<EditText>(R.id.opretAdgangskodeText)

        registerButton.setOnClickListener {
            if (emailTextView.text.isEmpty() || adgangskodeTextView.text.isEmpty()) {
                Toast.makeText(context, "email or password empty", Toast.LENGTH_SHORT).show()
            } else {
                val email: String = emailTextView.text.toString()
                val adgangskode: String = adgangskodeTextView.text.toString()

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, adgangskode)
                    .addOnCompleteListener(requireActivity(), OnCompleteListener<AuthResult>() { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            //viewModel.setCurrentUser(firebaseUser.iud)
                            //viewModel.setCurrenEmail(email)
                            Toast.makeText(context, "You are now registered", Toast.LENGTH_SHORT)
                                .show()
                            Navigation.findNavController(view)
                                .navigate(R.id.navigateFromOpretToForside)
                        } else {
                            Toast.makeText(context, "E-mail or password not valid", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
        return view
    }
}