package com.sps.todoapp.di.module

import android.content.Context
import androidx.room.Room
import com.sps.todoapp.data.local.room.AppDatabase
import com.sps.todoapp.data.local.room.dao.TaskDao
import com.sps.todoapp.data.local.room.repository.LocalRepository
import com.sps.todoapp.data.local.room.repository.LocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "task_database"
        ).build()
    }

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(taskDao: TaskDao): LocalRepository {
        return LocalRepositoryImpl(taskDao)
    }
}