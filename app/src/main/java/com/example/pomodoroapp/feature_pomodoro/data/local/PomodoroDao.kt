package com.example.pomodoroapp.feature_pomodoro.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pomodoroapp.feature_pomodoro.data.local.dto.LocalPomodoroItem


@Dao
interface PomodoroDao {

    @Query("SELECT * FROM pomodoro")
    fun getAllPomodoroItems(): LiveData<List<LocalPomodoroItem>>

    @Query("SELECT *  FROM pomodoro WHERE id = :id")
    suspend fun getSinglePomodoroItemById(id:Int): LocalPomodoroItem?

    @Query("UPDATE pomodoro SET title = :title, description = :description WHERE id = :id  ")
    suspend fun update(id: Int?, title: String?, description:String?)

    @Update
    suspend fun updatePomodoros(pomodoro: LocalPomodoroItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllPomodorosItems(pomodoros: List<LocalPomodoroItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPomodoroItem(pomodoro: LocalPomodoroItem): Long

    @Delete
    suspend fun deletePomodoroItem(pomodoro: LocalPomodoroItem)
}