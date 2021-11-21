package com.example.openlog

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

class Forside : Fragment() {

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //viewmodel is made here. using activitymodels for the instantiation, it can be shared among other fragments
    // code largely gotten from https://www.youtube.com/watch?v=YWNkNrmqff8&ab_channel=Dr.VipinClasses
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_forside, container, false)
        val buttonWidget = view.findViewById<ImageButton>(R.id.buttonWidget)
        val microphoneButton =  view.findViewById<ImageButton>(R.id.microphoneButton)
        val speechText = view.findViewById<TextView>(R.id.speechText)

        microphoneButton.setOnClickListener{
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
                Toast.makeText(context, "Device not supported", Toast.LENGTH_SHORT).show()
            }
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult? ->
            if (result!!.resultCode== AppCompatActivity.RESULT_OK && result!!.data!= null) {
                val text =result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<Editable>
                speechText.text = text[0]
                inputData(text[0])
            }
        }
        buttonWidget.setOnClickListener{ Navigation.findNavController(view).navigate(R.id.navigateFromForsideToHistorik) }
        return view
    }

    fun inputData(data: Editable) {
        if (data.contains("kulhydrat")){
            val kulhydratInput = KulhydratInput(data)
            database.child("kulhydraterInput").child("kulhydrat").setValue(kulhydratInput)
        }
    }
}