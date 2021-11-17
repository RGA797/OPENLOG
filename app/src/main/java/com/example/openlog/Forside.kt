package com.example.openlog

import android.app.Activity
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
        binding.microphoneButton.setOnClickListener{speak()}
    }


    private val REQUEST_CODE_SPEECH_INPUT = 100
    //made with the following tutorials and questions:https://newbedev.com/using-intent-to-call-a-fragment-from-another-fragment , https://www.youtube.com/watch?v=Wv2hafcjEVY,          https://stackoverflow.com/questions/18329104/recognizerintent-change-default-language
    fun speak(){
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        val language = "dk-DK"
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language)
        mIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter input")

        startActivityForResult(mIntent, REQUEST_CODE_SPEECH_INPUT)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data){
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
               //     binding.speechText.text = result[0]
                }
            }
        }
    }



}