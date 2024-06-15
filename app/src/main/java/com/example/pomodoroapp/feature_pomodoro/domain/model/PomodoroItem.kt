package com.example.pomodoroapp.feature_pomodoro.domain.model

import androidx.annotation.DrawableRes
import androidx.room.PrimaryKey

data class PomodoroItem (
    val title: String,
    val description: String,
    val count: Int,
    val completed: Boolean,
    val date: String,
    val id:Int?
)