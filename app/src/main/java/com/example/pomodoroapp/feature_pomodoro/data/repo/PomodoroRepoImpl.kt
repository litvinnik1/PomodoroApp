package com.example.pomodoroapp.feature_pomodoro.data.repo

import androidx.lifecycle.LiveData
import com.example.pomodoroapp.feature_pomodoro.data.di.IoDispatcher
import com.example.pomodoroapp.feature_pomodoro.data.local.PomodoroDao
import com.example.pomodoroapp.feature_pomodoro.data.local.dto.LocalPomodoroItem
//import com.example.pomodoroapp.feature_pomodoro.data.mapper.toLocalPomodoroItem
//import com.example.pomodoroapp.feature_pomodoro.data.mapper.toPomodoroItem
//import com.example.pomodoroapp.feature_pomodoro.data.mapper.toPomodoroItemFromLocal
import com.example.pomodoroapp.feature_pomodoro.domain.model.PomodoroItem
import com.example.pomodoroapp.feature_pomodoro.domain.repo.PomodoroRepo
import kotlinx.coroutines.CoroutineDispatcher

class PomodoroRepoImpl(
    private val dao: PomodoroDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    val allPomodoros: LiveData<List<LocalPomodoroItem>> = dao.getAllPomodoroItems()

    suspend fun addPomodoroItem(pomodoro: LocalPomodoroItem){
        dao.addPomodoroItem(pomodoro)
    }
    suspend fun delete(pomodoro: LocalPomodoroItem){
        dao.deletePomodoroItem(pomodoro)
    }
    suspend fun getSinglePomodoroItemById(id: Int): LocalPomodoroItem? {
        return dao.getSinglePomodoroItemById(id)
    }

    suspend fun update(pomodoro:LocalPomodoroItem){
        dao.update(pomodoro.id, pomodoro.title, pomodoro.description)
    }
    suspend fun updatePomodoro(pomodoro:LocalPomodoroItem){
        dao.updatePomodoros(pomodoro)
    }


//    override suspend fun getAllPomodoros(): LiveData<List<PomodoroItem>>{
//        getAllPomodoros()
//        return dao.getAllPomodoroItems().toPomodoroItemFromLocal()
//    }
//
//    private fun isCacheEmpty(): Boolean {
//        var empty = true
//        if(dao.getAllPomodoroItems().isNotEmpty()) empty = false
//        return empty
//    }
//
//    override suspend fun getSinglePomodoroItemById(id: Int): PomodoroItem? {
//        return dao.getSinglePomodoroItemById(id)?.toPomodoroItem()
//    }
//
//    override suspend fun addPomodoroItem(pomodoro: PomodoroItem) {
//        val newId = dao.addPomodoroItem(pomodoro.toLocalPomodoroItem())
//        val id = newId.toInt()
//    }
//    override suspend fun updatePomodoroItem(pomodoro: PomodoroItem) {
//        dao.addPomodoroItem(pomodoro.toLocalPomodoroItem())
//    }
//    override suspend fun deletePomodoroItem(pomodoro: PomodoroItem) {
//        dao.deletePomodoroItem(pomodoro.toLocalPomodoroItem())
//    }
}