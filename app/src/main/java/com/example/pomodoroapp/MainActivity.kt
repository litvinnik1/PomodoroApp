package com.example.pomodoroapp

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pomodoroapp.databinding.ActivityMainBinding
import com.example.pomodoroapp.feature_pomodoro.data.local.PomodoroDatabase
import com.example.pomodoroapp.feature_pomodoro.data.local.dto.LocalPomodoroItem
import com.example.pomodoroapp.feature_pomodoro.data.repo.PomodoroRepoImpl
import com.example.pomodoroapp.feature_pomodoro.domain.model.PomodoroItem
import com.example.pomodoroapp.feature_pomodoro.domain.util.PomodoroListAdapter
import com.example.pomodoroapp.feature_pomodoro.domain.util.PomodoroListDataclass
import com.example.pomodoroapp.feature_pomodoro.presentation.pomodoro_main.PomodoroMainViewModel
import com.example.pomodoroapp.feature_pomodoro.presentation.utils.CustomCountDownTimer
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PomodoroListAdapter.PomodorosClickListener,
    PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: PomodoroDatabase
    private val viewModel: PomodoroMainViewModel by viewModels()
    lateinit var adapter: PomodoroListAdapter
    lateinit var selectedPomodoro: LocalPomodoroItem
    private lateinit var old_pomodoro: LocalPomodoroItem

    private var timeSelected: Int = 1 * 60
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffset: Long = 0
    private var isStart = true
    private var isPomodoro = true

    var isUpdate = false

    private val updatePomodoro = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val pomodoro = result.data?.getSerializableExtra("pomodoro") as? LocalPomodoroItem
            if (pomodoro != null) {
                viewModel.addPomodoroItem(pomodoro)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initializing the UI
        initUI()
        viewModel.allPomodoros.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }


    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = PomodoroListAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(
            ActivityResultContracts
                .StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val pomodoro = result.data?.getSerializableExtra("pomodoro") as? LocalPomodoroItem
                if (pomodoro != null) {
                    viewModel.addPomodoroItem(pomodoro)
                }
            }
        }

        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, AddTask::class.java)
            getContent.launch(intent)
        }
        binding.startButton.setOnClickListener {
            startTimeSetup()
        }

        //Add listeners for Pomodoro, Short Break and Long Break buttons
        binding.pomodoroBtn.setOnClickListener {
            setTime(25,true)
        }
        binding.shortbreakBtn.setOnClickListener {
            setTime(5,false)
        }
        binding.longbreakBtn.setOnClickListener {
            setTime(15,false)
        }
    }

    private fun setTime(minutes:Int, isPomodoro: Boolean){
        timeSelected = minutes * 60
        this.isPomodoro = isPomodoro
        binding.timerTextView.text = String.format("%02d:00", minutes)
        resetTime(timeSelected)
        if (isPomodoro) {
            binding.timerLinearLayout.setBackgroundResource(R.color.cardview_red)
        } else {
            binding.timerLinearLayout.setBackgroundResource(R.color.cardview_yellow)
        }
    }

    override fun onItemClicked(pomodoro: LocalPomodoroItem) {
        val intent = Intent(this@MainActivity, AddTask::class.java)
        intent.putExtra("current_pomodoro", pomodoro)
        updatePomodoro.launch(intent)
    }

    override fun onLongItemClicked(pomodoro: LocalPomodoroItem, cardView: CardView) {
        selectedPomodoro = pomodoro
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_pomodoro) {
            viewModel.deletePomodoro(selectedPomodoro)
            return true
        }
        return false
    }

    private fun addExtraTime() {
        if(timeSelected != 0){
            timeSelected += 15
            timePause()
            startTimer(pauseOffset)
            Toast.makeText(this, "15 sec added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetTime(resetTimeSelected: Int) {
        if (timeCountDown != null) {
            timeCountDown!!.cancel()
            timeProgress = 0
            timeSelected = resetTimeSelected
            pauseOffset = 0
            timeCountDown = null
            binding.startButton.text = "Start"
            isStart = true
            binding.timerTextView.text = String.format("%02d:00", resetTimeSelected / 60)
        }
    }

    private fun timePause() {
        if (timeCountDown != null) {
            timeCountDown!!.cancel()
        }
    }

    private fun startTimeSetup() {
        if (timeSelected > timeProgress) {
            if (isStart) {
                binding.startButton.text = "Pause"
                startTimer(pauseOffset)
                isStart = false
            } else {
                isStart = true
                binding.startButton.text = "Resume"
                timePause()
            }
        } else {
            Toast.makeText(this, "Enter Time", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startTimer(pauseOffSetL: Long) {
        timeCountDown = object : CountDownTimer(
            (timeSelected * 1000).toLong() - pauseOffSetL * 1000, 1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                val hours: Long = (millisUntilFinished / 1000) / 3600
                val minutes: Long = ((millisUntilFinished / 1000) % 3600) / 60
                val seconds: Long = (millisUntilFinished / 1000) % 60
                val timeFormatted =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                pauseOffset = timeSelected.toLong() - millisUntilFinished / 1000
                binding.timerTextView.text = timeFormatted

            }

            override fun onFinish() {
               if (isPomodoro) {
                   setTime(5, false)
                   Toast.makeText(this@MainActivity, "Time for a short break!", Toast.LENGTH_SHORT).show()
                   val alarm = MediaPlayer.create(this@MainActivity, R.raw.alarm)
                   alarm.start()
               } else {
                   setTime(25,true)
                   Toast.makeText(this@MainActivity, "Back to work!", Toast.LENGTH_LONG).show()
                   val alarm = MediaPlayer.create(this@MainActivity, R.raw.alarm)
                   alarm.start()
               }
            }
        }.start()
    }

//    private fun setTimeFunction() {
//        resetTime()
//        binding.timerTextView.text = "25:00"
//        binding.startButton.text = "Start"
//        timeSelected = "25:00".toInt()
//    }

    override fun onDestroy() {
        super.onDestroy()
        if (timeCountDown != null) {
            timeCountDown?.cancel()
            timeProgress = 0
        }
    }

}