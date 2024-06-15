package com.example.pomodoroapp.feature_pomodoro.domain.use_case

import com.example.pomodoroapp.feature_pomodoro.domain.model.PomodoroItem
import com.example.pomodoroapp.feature_pomodoro.domain.repo.PomodoroRepo
import com.example.pomodoroapp.feature_pomodoro.domain.util.InvalidPomodoroItemException
import javax.inject.Inject

class PomodoroUseCases @Inject constructor(
    private val repo: PomodoroRepo
) {

    suspend fun addPomodoroItem(pomodoro: PomodoroItem){
        if(pomodoro.title.isBlank() || pomodoro.description.isBlank()){
            throw InvalidPomodoroItemException("Both the title and description must be populated.")
        }
        repo.addPomodoroItem(pomodoro)
    }
    suspend fun updatePomodoroItem(pomodoro: PomodoroItem) {
        if(pomodoro.title.isBlank() || pomodoro.description.isBlank()) {
            throw InvalidPomodoroItemException("Both the title and description must be populated.")
        }
        repo.updatePomodoroItem(pomodoro)
    }
    suspend fun deletePomodoroItem(pomodoro: PomodoroItem) {
        repo.deletePomodoroItem(pomodoro)
    }
    suspend fun toggleCompletePomodoroItem(pomodoro: PomodoroItem) {
        repo.updatePomodoroItem(pomodoro.copy(completed = !pomodoro.completed))
    }
    suspend fun getPomodoroItemById(id: Int): PomodoroItem? {
        return repo.getSinglePomodoroItemById(id)
    }
    suspend fun getPomodoroItems(){
        repo.getAllPomodoros()
    }
}
sealed class PomodoroUseCaseResult {
    data class Success(val pomodoroItems: List<PomodoroItem>): PomodoroUseCaseResult()
    data class Error(val message: String): PomodoroUseCaseResult()
}