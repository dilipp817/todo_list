package com.sps.todoapp.di.module

import android.content.Context
import androidx.room.Room
import com.sps.todoapp.data.local.room.AppDatabase
import com.sps.todoapp.data.local.room.dao.TaskDao
import com.sps.todoapp.data.local.room.repository.LocalRepository
import com.sps.todoapp.data.local.room.repository.LocalRepositoryImpl
import com.sps.todoapp.data.remote.ApiHelper
import com.sps.todoapp.data.remote.ApiHelperImpl
import com.sps.todoapp.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideBaseUrl() = "https://5e510330f2c0d300147c034c.mockapi.io/"

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (true) {  //TODO replace true with BuildConfig.DEBUG
        val loggingInspector = HttpLoggingInterceptor()
        loggingInspector.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(loggingInspector)
            .build()
    }
    else {
        OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

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