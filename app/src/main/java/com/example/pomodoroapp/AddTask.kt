package com.example.pomodoroapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pomodoroapp.databinding.ActivityAddTaskBinding
import com.example.pomodoroapp.databinding.ActivityMainBinding
import com.example.pomodoroapp.feature_pomodoro.data.local.dto.LocalPomodoroItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class AddTask : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    private lateinit var pomodoro: LocalPomodoroItem
    private lateinit var old_pomodoro: LocalPomodoroItem
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            old_pomodoro = (intent.getSerializableExtra("current_pomodoro") as LocalPomodoroItem)
            binding.editTextTitle.setText(old_pomodoro.title)
            binding.editTextDescription.setText(old_pomodoro.description)
            isUpdate = true
        } catch (e: Exception){

            e.printStackTrace()
        }
        binding.saveButton.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()

            if(title.isNotEmpty() || description.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault())
                if(isUpdate){
                    pomodoro = LocalPomodoroItem(
                        title, description, 2,true,formatter.format(Date()),old_pomodoro.id
                    )
                } else{
                    pomodoro = LocalPomodoroItem(
                        title, description, 2, true, formatter.format(Date()), null
                    )
                }
                val intent = Intent()
                intent.putExtra("pomodoro", pomodoro)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddTask, "Please enter some text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        binding.cancelButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}