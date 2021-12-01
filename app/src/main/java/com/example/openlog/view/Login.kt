package com.example.openlog.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import androidx.fragment.app.activityViewModels
import com.example.openlog.R
import com.example.openlog.viewModel.DataViewModel


class Login : Fragment() {


    private val dataViewModel: DataViewModel by activityViewModels()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val opretButton = view.findViewById<Button>(R.id.opretButton)
        val emailTextView =  view.findViewById<EditText>(R.id.loginEmailText)
        val adgangskodeTextView =  view.findViewById<EditText>(R.id.loginAdgangskodeText)
        loginButton.setOnClickListener{
            // val email = viewmodel.getcurrentUser
            if (emailTextView.text.isEmpty() || adgangskodeTextView.text.isEmpty()) {
                Toast.makeText(context, "email or password not given", Toast.LENGTH_SHORT).show()
            } else {
                val email: String = emailTextView.text.toString()
                val adgangskode: String = adgangskodeTextView.text.toString()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,adgangskode).addOnCompleteListener(requireActivity(), OnCompleteListener<AuthResult>(){ task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            dataViewModel.changeUser(firebaseUser, email)
                            Toast.makeText(context, "You are now logged in", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view)
                                .navigate(R.id.navigateFromLoginToForside)
                        } else {
                            Toast.makeText(
                                context,
                                "E-mail or password not valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }


        opretButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.navigateFromLoginToOpret)
        }
        return view
    }

}
