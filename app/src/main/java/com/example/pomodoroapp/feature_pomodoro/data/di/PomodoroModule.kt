package com.example.pomodoroapp.feature_pomodoro.data.di

import android.content.Context
import androidx.room.Room
import com.example.pomodoroapp.feature_pomodoro.data.local.PomodoroDao
import com.example.pomodoroapp.feature_pomodoro.data.local.PomodoroDatabase
import com.example.pomodoroapp.feature_pomodoro.data.repo.PomodoroRepoImpl
import com.example.pomodoroapp.feature_pomodoro.domain.repo.PomodoroRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PomodoroModule {

    @Provides
    fun providesRoomDao(database: PomodoroDatabase): PomodoroDao{
        return database.dao
    }


    @Singleton
    @Provides
    fun providesRoomDb(
        @ApplicationContext appContext: Context
    ): PomodoroDatabase{
        return Room.databaseBuilder(
            appContext.applicationContext,
            PomodoroDatabase::class.java,
            "pomodoro_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesPomodoroRepo(db: PomodoroDatabase, @IoDispatcher dispatcher: CoroutineDispatcher): PomodoroRepoImpl {
        return PomodoroRepoImpl(db.dao, dispatcher)
    }

}