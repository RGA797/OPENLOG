package com.example.openlog

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.openlog.databinding.FragmentForsideBinding


class Forside : Fragment() {

    private val dataViewModel: DataViewModel by activityViewModels()
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: FragmentForsideBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forside, container, false)
        return binding.root
    }

    //viewmodel is made here. using activitymodels for the instantiation, it can be shared among other fragments
    // code largely gotten from https://www.youtube.com/watch?v=YWNkNrmqff8&ab_channel=Dr.VipinClasses
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        binding.dataViewModel = dataViewModel

        super.onViewCreated(view, savedInstanceState)

        binding.profilButton.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.navigateFromForsideToProfil)
        }

        binding.manuelIndtastningButton.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.navigateFromForsideToManuelIndtastning)
        }

        binding.microphoneButton.setOnClickListener{onMicInput()}

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult? ->
            if (result!!.resultCode== AppCompatActivity.RESULT_OK && result!!.data!= null) {
                val text =result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<Editable>
                binding.speechText.text = text[0]
            }
        }
    }

    fun onMicInput(){
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        val language = "da-DK"
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
        val view = getView()
        Navigation.findNavController(view!!).navigate(R.id.navigateFromForsideToHistorik)
    }

}