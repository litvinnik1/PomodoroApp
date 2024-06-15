package com.example.pomodoroapp.feature_pomodoro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pomodoroapp.feature_pomodoro.data.local.dto.LocalPomodoroItem

@Database(
    entities = [LocalPomodoroItem::class],
    version = 3,
    exportSchema = false
)
abstract class PomodoroDatabase: RoomDatabase() {
    abstract val dao: PomodoroDao

    companion object{
        const val DATABASE_NAME = "pomodoro_db"
    }
}