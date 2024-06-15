package com.example.pomodoroapp.feature_pomodoro.domain.repo

import androidx.lifecycle.LiveData
import com.example.pomodoroapp.feature_pomodoro.domain.model.PomodoroItem

interface PomodoroRepo {
    suspend fun getAllPomodoros(): LiveData<List<PomodoroItem>>
    suspend fun addPomodoroItem(pomodoro: PomodoroItem)
    suspend fun getSinglePomodoroItemById(id: Int): PomodoroItem?
    suspend fun updatePomodoroItem(pomodoro: PomodoroItem)
    suspend fun deletePomodoroItem(pomodoro: PomodoroItem)
}