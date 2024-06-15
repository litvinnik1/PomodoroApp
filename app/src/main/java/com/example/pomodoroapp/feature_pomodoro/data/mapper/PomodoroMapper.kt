package com.example.pomodoroapp.feature_pomodoro.data.mapper

import com.example.pomodoroapp.feature_pomodoro.data.local.dto.LocalPomodoroItem
import com.example.pomodoroapp.feature_pomodoro.domain.model.PomodoroItem

//fun PomodoroItem.toLocalPomodoroItem(): LocalPomodoroItem{
//    return LocalPomodoroItem(
//        title = title,
//        description = description,
//        count = count,
//        completed = completed,
//        date = date,
//        id = id
//    )
//}
//
//fun LocalPomodoroItem.toPomodoroItem(): PomodoroItem{
//    return PomodoroItem(
//        title = title,
//        description = description,
//        count = count,
//        completed = completed,
//        date = date,
//        id = id
//    )
//}
//
//fun List<PomodoroItem>.toLocalPomodoroItem(): List<LocalPomodoroItem>{
//    return this.map { pomodoro ->
//        LocalPomodoroItem(
//            title = pomodoro.title,
//            description = pomodoro.title,
//            count = pomodoro.count,
//            completed = pomodoro.completed,
//            date = pomodoro.date,
//            id = pomodoro.id
//        )
//    }
//}
//
//fun List<LocalPomodoroItem>.toPomodoroItemFromLocal(): List<PomodoroItem>{
//    return this.map { pomodoro ->
//        PomodoroItem(
//            title = pomodoro.title,
//            description = pomodoro.description,
//            count = pomodoro.count,
//            completed = pomodoro.completed,
//            date = pomodoro.date,
//            id = pomodoro.id
//        )
//    }
//}