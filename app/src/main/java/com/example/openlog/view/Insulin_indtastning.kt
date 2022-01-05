package com.example.openlog.view;
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment;
import androidx.fragment.app.activityViewModels
import com.example.openlog.R
import com.example.openlog.databinding.FragmentBlodsukkerIndtastningBinding
import com.example.openlog.databinding.FragmentInsulinIndtastningBinding
import com.example.openlog.viewModel.DataViewModel
import com.google.firebase.auth.FirebaseUser

class Insulin_indtastning: Fragment() {

    private val dataViewModel: DataViewModel by activityViewModels()
    private lateinit var binding: FragmentInsulinIndtastningBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_insulin_indtastning, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logButton.setOnClickListener{onLogInput()}
    }


    fun onLogInput(){
        val firebaseUser: FirebaseUser = dataViewModel.getCurrentFirebaseUser()!!
        dataViewModel.changeInput("insulin " + binding.textInput.text.toString())
        val success = dataViewModel.saveInput(firebaseUser)
        if (success){
            Toast.makeText(context, "Input Gemt", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Invalit input", Toast.LENGTH_SHORT).show()
        }
    }
}
