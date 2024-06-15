package com.example.pomodoroapp

import android.app.Activity
import android.content.Intent
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

    private var timeSelected: Int = 25
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffset: Long = 0
    private var isStart = true


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
//            if (binding.addtaskCv.visibility == View.VISIBLE) {
//                TransitionManager.beginDelayedTransition(binding.addtaskCv, AutoTransition())
//                binding.addtaskCv.visibility = View.GONE
//            } else {
//                TransitionManager.beginDelayedTransition(binding.addtaskCv, AutoTransition())
//                binding.addtaskCv.visibility = View.VISIBLE
//                binding.addTaskButton.visibility = View.GONE
//            }
            val intent = Intent(this, AddTask::class.java)
            getContent.launch(intent)
        }
        binding.startButton.setOnClickListener {
            startTimeSetup()
        }


//        binding.cancelMainButton.setOnClickListener {
//            if (binding.addtaskCv.visibility == View.VISIBLE) {
//                TransitionManager.beginDelayedTransition(binding.addtaskCv, AutoTransition())
//                binding.addtaskCv.visibility = View.GONE
//                binding.addTaskButton.visibility = View.VISIBLE
//            }
//        }
//        binding.addtaskCv.setOnClickListener {
//            if (binding.addtaskCv.visibility == View.VISIBLE) {
//                TransitionManager.beginDelayedTransition(binding.addtaskCv, AutoTransition())
//                binding.addtaskCv.visibility = View.GONE
//                binding.addTaskButton.visibility = View.VISIBLE
//            }
//        }
//        binding.saveMainButton.setOnClickListener {
//            val title = binding.editMainTitle.text.toString()
//            val description = binding.editMainDescription.text.toString()
//
//            val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault())
//            if (isUpdate) {
//                selectedPomodoro = LocalPomodoroItem(
//                    title, description, 1, true, formatter.format(Date()), old_pomodoro.id
//                )
//            } else {
//                selectedPomodoro = LocalPomodoroItem(
//                    title, description, 1, true, formatter.format(Date()), null
//                )
//            }
//            viewModel.addPomodoroItem(selectedPomodoro)
//        }
    }

    override fun onItemClicked(pomodoro: LocalPomodoroItem) {

//        val title = binding.editMainTitle.text.toString()
//        val description = binding.editMainDescription.text.toString()
//        val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault())
//
//        if (binding.addtaskCv.visibility == View.VISIBLE) {
//            TransitionManager.beginDelayedTransition(binding.addtaskCv, AutoTransition())
//            binding.addtaskCv.visibility = View.GONE
//        } else {
//            TransitionManager.beginDelayedTransition(binding.addtaskCv, AutoTransition())
//            binding.addtaskCv.visibility = View.VISIBLE
//            binding.addTaskButton.visibility = View.GONE
//        }
//        if (isUpdate) {
//            selectedPomodoro = LocalPomodoroItem(
//                title, description, 1, true, formatter.format(Date()), old_pomodoro.id
//            )
//            viewModel.updatePomodoro(pomodoro)
//        } else {
//            selectedPomodoro = LocalPomodoroItem(
//                title, description, 1, true, formatter.format(Date()), null
//            )
//        }
//        val data = LocalPomodoroItem(
//            title = title,
//            description = description,
//            1,
//            true,
//            formatter.format(Date()),
//            id = null
//        )
//        viewModel.updatePomodoro(selectedPomodoro)
//
//        pomodoro?.let {
//            viewModel.updatePomodoro(
//                LocalPomodoroItem(
//                    binding.editMainTitle.text.toString(),
//                    binding.editMainDescription.text.toString(),
//                    1,
//                    true,
//                    formatter.format(Date()),
//                    null
//                )
//            )
//        }

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

    private fun resetTime() {
        if (timeCountDown != null) {
            timeCountDown!!.cancel()
            timeProgress = 0
            timeSelected = 0
            pauseOffset = 0
            timeCountDown = null
            binding.startButton.text = "Start"
            isStart = true
            binding.timerTextView.text = "0"
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
                timeProgress++
                pauseOffset = timeSelected.toLong() - millisUntilFinished / 1000
                binding.timerTextView.text = (timeSelected - timeProgress).toString()

            }

            override fun onFinish() {
                resetTime()
                Toast.makeText(this@MainActivity, "Times Up!", Toast.LENGTH_SHORT).show()

            }
        }.start()
    }

    private fun setTimeFunction() {
        resetTime()
        binding.timerTextView.text = "25:00"
        binding.startButton.text = "Start"
        timeSelected = "25:00".toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timeCountDown != null) {
            timeCountDown?.cancel()
            timeProgress = 0
        }
    }

}