package com.example.pomodoroapp.feature_pomodoro.presentation.pomodoro_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoroapp.feature_pomodoro.data.di.IoDispatcher
import com.example.pomodoroapp.feature_pomodoro.data.local.PomodoroDao
import com.example.pomodoroapp.feature_pomodoro.data.local.PomodoroDatabase
import com.example.pomodoroapp.feature_pomodoro.data.local.dto.LocalPomodoroItem
import com.example.pomodoroapp.feature_pomodoro.data.repo.PomodoroRepoImpl
import com.example.pomodoroapp.feature_pomodoro.domain.model.PomodoroItem
import com.example.pomodoroapp.feature_pomodoro.domain.use_case.PomodoroUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PomodoroMainViewModel @Inject constructor(
    private var pomodoroRepoImpl: PomodoroRepoImpl,
    private val database: PomodoroDao,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private var undoPomodoroItem: PomodoroItem? = null
    private var getPomodoroItemsJob: Job? = null
    private var currentPomodorId = null
    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
    }

    val allPomodoros: LiveData<List<LocalPomodoroItem>> = pomodoroRepoImpl.allPomodoros

    fun deletePomodoro(pomodoro: LocalPomodoroItem){
        viewModelScope.launch (Dispatchers.IO) {
            pomodoroRepoImpl.delete(pomodoro)
        }
    }

    fun addPomodoroItem(pomodoro: LocalPomodoroItem){
        viewModelScope.launch(Dispatchers.IO) {
            pomodoroRepoImpl.addPomodoroItem(pomodoro)
        }
    }
    fun getSinglePomodoroItemById(id: Int){
        viewModelScope.launch (Dispatchers.IO){
            pomodoroRepoImpl.getSinglePomodoroItemById(id)
        }
    }
    fun updatePomodoro(pomodoro: LocalPomodoroItem) {
        viewModelScope.launch(Dispatchers.IO) {
            pomodoroRepoImpl.update(pomodoro)
        }
    }



//    fun onEvent(event: PomodoroEvent){
//        when(event){
//            is PomodoroEvent.Delete -> {
//                viewModelScope.launch (dispatcher + errorHandler) {
//                    pomodoroUseCases.deletePomodoroItem(event.pomodoro)
//                    getPomodoroItems()
//                    undoPomodoroItem = event.pomodoro
//                }
//            }
//            is PomodoroEvent.ToggleCompleted -> {
//                viewModelScope.launch (dispatcher + errorHandler) {
//                    pomodoroUseCases.toggleCompletePomodoroItem(pomodoro = event.pomodoro)
//                    getPomodoroItems()
//                }
//            }
//            is PomodoroEvent.UndoDelete ->{
//                viewModelScope.launch (dispatcher + errorHandler) {
//                    pomodoroUseCases.addPomodoroItem(undoPomodoroItem ?: return@launch)
//                    undoPomodoroItem = null
//                    getPomodoroItems()
//                }
//            }
//        }
//    }
//
//
//    fun getPomodoroItems() {
//        getPomodoroItemsJob?.cancel()
//
//        getPomodoroItemsJob = viewModelScope.launch {
//            val result = pomodoroUseCases.getPomodoroItems()
//        }
//    }
}