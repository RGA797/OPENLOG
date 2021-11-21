package com.example.openlog

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.openlog.databinding.FragmentForsideBinding


class Forside : Fragment() {

    private lateinit var binding: FragmentForsideBinding
    //viewmodel is made here. using activitymodels for the instantiation, it can be shared among other fragments
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forside, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        binding.buttonWidget.setOnClickListener{ Navigation.findNavController(binding.root).navigate(R.id.navigateFromForsideToHistorik) }
        //binding.microphoneButton.setOnClickListener{speak()}
    }


    fun speak(){
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        val language = "dk-DK"
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language)
        mIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter something to log")
        try {
            activityResultLauncher.launch(mIntent)
        }
        catch (exp: ActivityNotFoundException){
            Toast.makeText(applicationContext, "Device not supported", Toast.LENGTH_SHORT.show())
        }
    }

    activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){val result: ActivityResult? ->
        if (result!!.resultCode== AppCompatActivity.RESULT_OK && result!!.data!= null) {
            val text =result!!.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<Editable>
            binding.speechText.text = text[0]
        }
    }

}