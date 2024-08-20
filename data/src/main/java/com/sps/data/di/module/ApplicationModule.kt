package com.sps.data.di.module

import android.content.Context
import androidx.room.Room
import com.sps.data.local.room.AppDatabase
import com.sps.data.local.room.repository.LocalRepository
import com.sps.data.local.room.repository.LocalRepositoryImpl
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
    fun provideTaskDao(db: AppDatabase): com.sps.data.local.room.dao.TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(taskDao: com.sps.data.local.room.dao.TaskDao): LocalRepository {
        return LocalRepositoryImpl(taskDao)
    }
}