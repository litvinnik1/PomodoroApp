package com.example.pomodoroapp.feature_pomodoro.presentation.pomodoro_main

import com.example.pomodoroapp.feature_pomodoro.domain.model.PomodoroItem

sealed class PomodoroEvent {
    data class Delete(val pomodoro: PomodoroItem): PomodoroEvent()
    data class ToggleCompleted(val pomodoro: PomodoroItem): PomodoroEvent()
    object UndoDelete: PomodoroEvent()
}