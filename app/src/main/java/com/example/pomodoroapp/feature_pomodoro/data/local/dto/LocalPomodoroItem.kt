package com.example.pomodoroapp.feature_pomodoro.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "pomodoro")
data class LocalPomodoroItem(
    val title: String,
    val description: String,
    val count: Int,
    val completed: Boolean,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id:Int?
) : Serializable
