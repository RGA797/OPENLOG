package com.example.openlog

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


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

        binding.lifecycleOwner = viewLifecycleOwner

        binding.profilButton.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.navigateFromForsideToProfil)
        }

        binding.manuelIndtastningButton.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.navigateFromForsideToManuelIndtastning)
        }

        binding.microphoneButton.setOnClickListener{ onMicInput()}

        binding.logButton.setOnClickListener{onLogInput()}

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult? ->
            if (result!!.resultCode== AppCompatActivity.RESULT_OK && result!!.data!= null) {
                val text =result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<Editable>
                dataViewModel.changeInput(text[0].toString())
                dataViewModel.changeNew(true)
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

    fun onLogInput(){
        val firebaseUser: FirebaseUser = dataViewModel.getCurrentFirebaseUser()!!
        val database = FirebaseDatabase.getInstance("https://openlog-a2b24-default-rtdb.europe-west1.firebasedatabase.app/")
        val input = dataViewModel.getInput()
        if (input != null) {
            val myRef = database.getReference("users").child(firebaseUser.uid).push()
            if (input.contains("insulin")) {
                inputData("insulin",input, myRef)
                Toast.makeText(context, "input saved", Toast.LENGTH_SHORT).show()
                return
            }
            else if (input.contains("kulhydrat")) {
                inputData("kulhydrat",input, myRef)
                Toast.makeText(context, "input saved", Toast.LENGTH_SHORT).show()
                return
            }
            else if (input.contains("blodsukker")) {
                inputData("blodsukker",input, myRef)
                Toast.makeText(context, "input saved", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }

    private fun inputData(type: String, input: String, Ref: DatabaseReference){
            val time = Calendar.getInstance().getTime().toString()
            val inputValue = input.filter { it.isDigit() }
            if (inputValue != "") {
                Ref.child(time).child(type).setValue(inputValue)
            }
            dataViewModel.changeNew(false)
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
        Navigation.findNavController(binding.root).navigate(R.id.navigateFromForsideToHistorik)
    }

}