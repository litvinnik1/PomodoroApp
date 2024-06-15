package com.example.pomodoroapp.feature_pomodoro.domain.util

import androidx.annotation.DrawableRes

data class PomodoroListDataclass(
    val name: String,
    val pomodoroAmount: Int,
    @DrawableRes val completedImage: Int
)
